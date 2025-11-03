package com.compapptition.backend.service;

import com.compapptition.backend.DTO.auth.CompeticionContextoDTO;
import com.compapptition.backend.entity.Usuarios;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateToken(Usuarios usuario, List<CompeticionContextoDTO> competiciones, Long competicionActual) {
        try {
            Map<String, Object> claims = new HashMap<>();
            claims.put("userId", usuario.getId());
            claims.put("username", usuario.getUserNombre());
            claims.put("email", usuario.getEmail());

            // ObjectMapper maneja el enum automáticamente
            String competicionesJson = objectMapper.writeValueAsString(competiciones);
            claims.put("competiciones", competicionesJson);

            if (competicionActual != null) {
                claims.put("competicionActual", competicionActual);
            }

            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(usuario.getUserNombre())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al generar token", e);
        }
    }

    public List<CompeticionContextoDTO> getCompeticionesFromToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            String competicionesJson = claims.get("competiciones", String.class);

            if (competicionesJson == null) {
                return Collections.emptyList();
            }

            // ObjectMapper deserializa el enum automáticamente
            return objectMapper.readValue(
                    competicionesJson,
                    new TypeReference<List<CompeticionContextoDTO>>() {}
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error al parsear competiciones del token", e);
        }
    }

    public Long getCompeticionActualFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        Object competicionActual = claims.get("competicionActual");

        if (competicionActual == null) {
            return null;
        }

        if (competicionActual instanceof Integer) {
            return ((Integer) competicionActual).longValue();
        }

        return (Long) competicionActual;
    }

    public String updateCompeticionActual(String oldToken, Long nuevaCompeticionId) {
        Claims claims = getAllClaimsFromToken(oldToken);

        List<CompeticionContextoDTO> competiciones = getCompeticionesFromToken(oldToken);
        boolean tieneAcceso = competiciones.stream()
                .anyMatch(c -> c.getCompeticionId().equals(nuevaCompeticionId));

        if (!tieneAcceso) {
            throw new RuntimeException("No tienes acceso a esta competición");
        }

        claims.put("competicionActual", nuevaCompeticionId);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.getSubject())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}