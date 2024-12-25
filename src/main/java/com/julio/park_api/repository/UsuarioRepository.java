package com.julio.park_api.repository;

import com.julio.park_api.entity.Usuario;
import com.julio.park_api.service.UsuarioService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository  extends JpaRepository <Usuario, Long> {
    Optional <Usuario>findByUsername(String username);

    @Query("select u.role from Usuario u where u.username like :username")
    Usuario.Role findRoleByUsername(String username);
}
