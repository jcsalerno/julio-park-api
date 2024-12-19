package com.julio.park_api.web.controller;
import com.julio.park_api.entity.Usuario;
import com.julio.park_api.service.UsuarioService;


import com.julio.park_api.web.dto.UsuarioCreateDto;
import com.julio.park_api.web.dto.UsuarioSenhaDto;
import com.julio.park_api.web.dto.mapper.UsuarioMapper;
import com.julio.park_api.web.dto.mapper.UsuarioResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping
    public ResponseEntity<UsuarioResponseDto> create(@Validated @RequestBody UsuarioCreateDto createDto) {
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id) {
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioMapper.toDto(user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id,@RequestBody UsuarioSenhaDto dto) {
        Usuario user = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity <List <Usuario>> getAll() {
       List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok(users);
    }

}