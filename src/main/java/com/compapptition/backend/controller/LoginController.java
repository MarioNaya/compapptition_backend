package com.compapptition.backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/jugador")
    @PreAuthorize("hasRole('jugador')")
    public String jugador(){
        return "Eres un jugador";
    }

    @GetMapping("/arbitro")
    @PreAuthorize("hasRole('arbitro')")
    public String arbitro(){
        return "Eres un árbitro.";
    }

    @GetMapping("/team_manager")
    @PreAuthorize("hasRole('manager')")
    public String teamAdmin(){
        return "Eres un manager de equipo";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public String admin(){
        return "Eres un administrador";
    }
}
