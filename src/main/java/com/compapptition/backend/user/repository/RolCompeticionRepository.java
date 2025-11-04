package com.compapptition.backend.user.repository;

import com.compapptition.backend.user.entity.RolCompeticion;
import com.compapptition.backend.user.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolCompeticionRepository extends JpaRepository<RolCompeticion, Long> {

    List<RolCompeticion> findByUsuarioAndActivoTrue(Usuarios usuario);

    Optional<RolCompeticion> findByUsuarioIdAndCompeticionIdAndActivoTrue(
            Long usuarioId,
            Long competicionId
    );

    List<RolCompeticion> findByCompeticionIdAndActivoTrue(Long competicionId);

    boolean existsByUsuarioIdAndCompeticionIdAndActivoTrue(
            Long usuarioId,
            Long competicionId
    );

    @Query("SELECT rc FROM RolCompeticion rc " +
            "JOIN FETCH rc.competicion c " +
            "WHERE rc.usuario.id = :usuarioId AND rc.activo = true")
    List<RolCompeticion> findByUsuarioIdWithCompeticion(@Param("usuarioId") Long usuarioId);
}