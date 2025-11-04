package com.compapptition.backend.user.security;

import com.compapptition.backend.user.entity.Usuarios;
import com.compapptition.backend.user.repository.UsuariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio que carga los detalles del usuario para Spring Security
 * Este servicio es usado por el AuthenticationProvider
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuariosRepository usuarioRepository;

    /**
     * Metodo principal que Spring Security llama para autenticar
     * Se ejecuta cuando el usuario hace login
     *
     * @param username El nombre de usuario ingresado en el login
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException si el usuario no existe
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Buscar usuario en la base de datos
        Usuarios usuario = usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username
                ));

        // Verificar que el usuario esté activo
        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException(
                    "Usuario inactivo: " + username
            );
        }

        // Convertir Usuarios (entidad) a SecurityUser (UserDetails)
        // En este punto no tenemos competiciones seleccionadas, solo autenticación básica
        return new SecurityUser(usuario);
    }

    /**
     * Metodo auxiliar para obtener la entidad Usuario directamente
     * Útil para el JwtAuthenticationFilter
     */
    public Usuarios loadUserEntityByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByUserName(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado: " + username
                ));
    }

    /**
     * Cargar usuario por email (alternativa)
     */
    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        Usuarios usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con email: " + email
                ));

        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException(
                    "Usuario inactivo: " + email
            );
        }

        return new SecurityUser(usuario);
    }

    /**
     * Cargar usuario por ID
     * Útil para operaciones internas
     */
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        Usuarios usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario no encontrado con ID: " + userId
                ));

        if (!usuario.getActivo()) {
            throw new UsernameNotFoundException(
                    "Usuario inactivo con ID: " + userId
            );
        }

        return new SecurityUser(usuario);
    }
}