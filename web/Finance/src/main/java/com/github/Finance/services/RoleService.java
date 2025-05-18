package com.github.Finance.services;

import org.springframework.stereotype.Service;

import com.github.Finance.models.Role;
import com.github.Finance.repositories.RoleRepository;

@Service
public class RoleService {
    
    private final RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }



    public Role findRepositoryById(Long id) {
        return repository.findById(id).orElse(null);
    }

}
