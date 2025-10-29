package com.compapptition.backend.config;

import com.compapptition.backend.entity.Usuarios;
import com.compapptition.backend.security.CustomAuthenticationSuccesHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccesHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/test").permitAll()
                        .requestMatchers("/v1/jugador").hasRole("jugador")
                        .requestMatchers("/v1/arbitro").hasRole("arbitro")
                        .requestMatchers("/v1/manager").hasRole("manager")
                        .requestMatchers("/v1/admin").hasRole("admin")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .successHandler(successHandler)
                        .permitAll()
                )
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
