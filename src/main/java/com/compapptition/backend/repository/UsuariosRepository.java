package com.compapptition.backend.repository;

import com.compapptition.backend.entity.Usuarios;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuariosRepository extends CrudRepository<Usuarios, Long> {

    Usuarios findByUserName(String usuario);

}
