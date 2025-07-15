package com.github.Finance.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "recover_password_tokens")
@Getter
@Setter
@NoArgsConstructor
public class RecoverPasswordToken {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String token;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;

}
