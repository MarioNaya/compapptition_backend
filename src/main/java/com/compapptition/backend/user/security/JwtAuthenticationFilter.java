package com.compapptition.backend.user.security;

import com.compapptition.backend.user.DTO.auth.CompeticionContextoDTO;
import com.compapptition.backend.user.entity.Usuarios;
import com.compapptition.backend.user.repository.UsuariosRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuariosRepository usuarioRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // 1. Extraer el header Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Si no hay header o no empieza con "Bearer ", continuar sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 3. Extraer el token (quitar "Bearer ")
            String jwt = authHeader.substring(7);

            // 4. Validar el token
            if (jwtService.validateToken(jwt)) {
                String username = jwtService.getUsernameFromToken(jwt);

                // 5. Si el usuario no está ya autenticado en el contexto
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 6. Cargar usuario desde BD
                    Usuarios usuario = usuarioRepository.findByUserName(username)
                            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                    // 7. Extraer competiciones y competición actual del token
                    List<CompeticionContextoDTO> competiciones =
                            jwtService.getCompeticionesFromToken(jwt);
                    Long competicionActual =
                            jwtService.getCompeticionActualFromToken(jwt);

                    // 8. Crear SecurityUser con toda la información
                    UserDetails userDetails = new SecurityUser(
                            usuario,
                            competiciones,
                            competicionActual
                    );

                    // 9. Crear el objeto de autenticación
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()  // Roles/Authorities
                            );

                    // 10. Añadir detalles adicionales de la petición
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 11. Establecer la autenticación en el contexto de Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Log del error (opcional)
            System.err.println("Error en autenticación JWT: " + e.getMessage());
        }

        // 12. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}