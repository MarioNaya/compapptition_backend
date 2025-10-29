package com.compapptition.backend.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationSuccesHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = "/v1";


        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_admin"))) {
            redirectUrl = "/v1/admin";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_manager"))) {
            redirectUrl = "/v1/manager";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_arbitro"))) {
            redirectUrl = "/v1/arbitro";
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_jugador"))) {
            redirectUrl = "/v1/jugador";
        }

        response.sendRedirect(redirectUrl);
    }
}