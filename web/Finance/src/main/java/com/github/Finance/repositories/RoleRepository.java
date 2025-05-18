package com.github.Finance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.Finance.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    
}
