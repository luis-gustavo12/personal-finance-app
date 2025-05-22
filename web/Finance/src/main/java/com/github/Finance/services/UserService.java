package com.github.Finance.services;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.Finance.dtos.forms.RegisterForm;
import com.github.Finance.models.Role;
import com.github.Finance.models.User;
import com.github.Finance.repositories.UserRepository;


/**
 * Class responsible for every user interaction with the repositories.
 */

@Service
public class UserService implements UserDetailsService {
    
    private final UserRepository repository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final RoleService roleService;

    public UserService (UserRepository repository, RoleService roleService) {
        this.repository = repository;
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


    /**
     * 
     * Implementation in order to Spring Security handle authentication for us.
     * 
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        User user = repository.findByEmail(username).orElseThrow( () -> new UsernameNotFoundException("User wasn't found!!") );
        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority( String.format("ROLE_%s", user.getRole().getRole().toUpperCase()) ) )
        );

    }

    public User getUserById(Long id) {
        return repository.findById(id).orElseThrow(() -> new UsernameNotFoundException("User couldn't be found!!"));
    }

    public User getUserByEmail(String email) {
        return repository.findByEmail(email).orElse(null);
    }


}
