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

    @GetMapping("/v1")
    public String test(){
        return "Error en el logado. No detectado rol.";
    }

    @GetMapping("/jugador")
    public String jugador(){
        return "Eres un jugador";
    }

    @GetMapping("/arbitro")
    public String arbitro(){
        return "Eres un árbitro.";
    }

    @GetMapping("/manager")
    public String teamAdmin(){
        return "Eres un manager de equipo";
    }

    @GetMapping("/admin")
    public String admin(){
        return "Eres un administrador";
    }
}
