package com.github.Finance.repositories;

import com.github.Finance.models.RecoverPasswordToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoverPasswordTokenRepository extends JpaRepository<RecoverPasswordToken, Long> {

    RecoverPasswordToken findByToken(String token);



}
