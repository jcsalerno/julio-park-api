package com.julio.park_api.service;
import com.julio.park_api.entity.Usuario;
import com.julio.park_api.repository.UsuarioRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;

    /*@Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {  // Injeção explícita no construtor
        this.usuarioRepository = usuarioRepository;
    }*/


    @Transactional
    public Usuario salvar(Usuario usuario) {
        System.out.println("Salvando usuário: " + usuario);
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new   RuntimeException("Usuario não encontrado")
        );
    }
}
