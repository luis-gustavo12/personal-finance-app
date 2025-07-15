package com.github.Finance.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.github.Finance.services.UserService;

@Configuration
public class SecurityConfig {

    private final UserService userService;

    public SecurityConfig(UserService userService) {
        this.userService = userService;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        httpSecurity
            .authorizeHttpRequests(
               requests -> requests
               .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
               .requestMatchers("/register/**", "/register", "/register/").permitAll()
               .requestMatchers("/recover/**").permitAll()
               .anyRequest().authenticated()
            )

            
            .formLogin(
                (form) -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login/submit")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/dashboard", true)
                    .permitAll()
            )
            ;

        return httpSecurity.build();

    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        var provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(encoder());
        provider.setUserDetailsService(userService);
        return provider;
    }


}
