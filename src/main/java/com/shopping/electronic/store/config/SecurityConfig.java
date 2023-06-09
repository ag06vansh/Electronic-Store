package com.shopping.electronic.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails adminUser = User.builder()
            .username("Vansh")
            .password(passwordEncoder().encode("Vanya@123"))
            .roles("ADMIN")
            .build();
        UserDetails normalUser = User.builder()
            .username("Ananya")
            .password(passwordEncoder().encode("Ananya@123"))
            .roles("NORMAL")
            .build();
        return new InMemoryUserDetailsManager(adminUser, normalUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
