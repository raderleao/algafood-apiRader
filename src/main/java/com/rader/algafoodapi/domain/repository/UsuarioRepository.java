package com.rader.algafoodapi.domain.repository;

import com.rader.algafoodapi.domain.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends CustomJpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}
