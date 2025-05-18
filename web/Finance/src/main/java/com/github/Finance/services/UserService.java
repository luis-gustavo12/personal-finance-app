package com.github.Finance.services;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.Finance.dtos.RegisterForm;
import com.github.Finance.models.Role;
import com.github.Finance.models.User;
import com.github.Finance.repositories.UserRepository;

@Service
public class UserService {
    
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder;
    private final RoleService roleService;

    public UserService (UserRepository repository, BCryptPasswordEncoder encoder, RoleService roleService) {
        this.repository = repository;
        this.encoder = encoder;
        this.roleService = roleService;
    }


    public User createUser (RegisterForm form) {

        User user = new User();
        user.setEmail(form.email());
        user.setFirstName(form.firstName());
        user.setLastName(form.lastName());
        user.setPassword( encoder.encode(form.password()) );
        user.setRole( roleService.findRepositoryById(Role.ROLE_USER)  );
        
        return repository.save(user);

    }


}
