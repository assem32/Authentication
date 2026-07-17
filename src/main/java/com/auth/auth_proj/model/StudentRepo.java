package com.auth.auth_proj.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional( readOnly = true )
public interface StudentRepo extends JpaRepository<Users,Long>{
    Optional< Users> findByEmail (String email);
}
