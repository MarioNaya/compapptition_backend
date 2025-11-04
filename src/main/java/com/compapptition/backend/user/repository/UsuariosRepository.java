package com.compapptition.backend.user.repository;

import com.compapptition.backend.user.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {

    Optional<Usuarios> findByUserName(String userName);

    Optional<Usuarios> findByEmail(String email);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    Optional<Usuarios> findByUserNameAndActivoTrue(String userName);
}
