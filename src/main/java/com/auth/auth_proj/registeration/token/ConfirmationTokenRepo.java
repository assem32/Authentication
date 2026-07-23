package com.auth.auth_proj.registeration.token;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationToken,Long>{

    Optional<ConfirmationToken> findByToken(String token);


    @Modifying
    @Transactional
    @Query("UPDATE ConfirmationToken c "+"SET c.confirmedAt=?1 "+"Where c.token=?2")
    int updateConfirmaionAt (LocalDateTime confirmedAt , String token);

}
