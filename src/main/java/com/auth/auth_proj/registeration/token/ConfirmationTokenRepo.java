package com.auth.auth_proj.registeration.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationTokenRepo extends JpaRepository<ConfirmationToken,Long>{

}
