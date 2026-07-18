package com.auth.auth_proj.registeration.token;

import java.time.LocalDateTime;

import com.auth.auth_proj.model.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class ConfirmationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String token;

    private LocalDateTime createdAt;

    @Column(nullable = false)

    private LocalDateTime expiredAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(
        nullable = false,
        name = "user_id"
    )
    private Users user;

    public ConfirmationToken(
    String token,
     LocalDateTime createdAt,
     LocalDateTime expiredAt,
    Users users
    ){
        this.token=token;
        this.createdAt=createdAt;
        this.expiredAt=expiredAt;
        this.user= users;
     }
}
