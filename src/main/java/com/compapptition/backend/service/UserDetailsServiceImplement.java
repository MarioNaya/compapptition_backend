package com.compapptition.backend.service;

import com.compapptition.backend.security.SecurityUser;
import com.compapptition.backend.entity.Usuarios;
import com.compapptition.backend.repository.UsuariosRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserDetailsServiceImplement implements UserDetailsService {

    private UsuariosRepository usuariosRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuarios user = usuariosRepository.findByUserName(username);
        if(user==null){
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return new SecurityUser(user);
    }
}
