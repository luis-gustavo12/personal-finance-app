package com.github.Finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.Finance.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
}
