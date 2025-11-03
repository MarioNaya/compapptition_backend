package com.compapptition.backend.repository;

import com.compapptition.backend.entity.Competiciones;
import com.compapptition.backend.entity.Competiciones.EstadoCompeticion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompeticionRepository extends JpaRepository<Competiciones, Long> {
    List<Competiciones> findByActivoTrue();
    List<Competiciones> findByEstadoAndActivoTrue(EstadoCompeticion estado);
    boolean existsByCompetitionName(String competitionName);
}