package com.julio.park_api.web.controller;
import com.julio.park_api.entity.Usuario;
import com.julio.park_api.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }



    @PostMapping
    public ResponseEntity<Usuario> create(@Validated @RequestBody Usuario usuario) {


        Usuario user = usuarioService.salvar(usuario);


        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
