package com.julio.park_api.service;
import com.julio.park_api.entity.Usuario;
import com.julio.park_api.repository.UsuarioRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UsuarioService {


    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(UsuarioRepository usuarioRepository) {  // Injeção explícita no construtor
        this.usuarioRepository = usuarioRepository;
    }


    @Transactional
    public Usuario salvar(Usuario usuario) {
        System.out.println("Salvando usuário: " + usuario);
        return usuarioRepository.save(usuario);
    }

}
