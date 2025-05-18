package com.github.Finance.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity
            .authorizeHttpRequests(
               requests -> requests
               .requestMatchers("/css/**").permitAll()
               .requestMatchers("/register/**").permitAll()
               .anyRequest().authenticated()
            )

            
            .formLogin(
                (form) -> form
                    .loginPage("/login")
                    .permitAll()
            )
            ;

        return httpSecurity.build();

    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    
}
