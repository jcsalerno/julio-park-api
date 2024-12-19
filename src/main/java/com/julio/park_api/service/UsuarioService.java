package com.julio.park_api.service;
import com.julio.park_api.entity.Usuario;
import com.julio.park_api.repository.UsuarioRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {

        if(!novaSenha.equals(confirmaSenha)) {
            throw new RuntimeException("Nova senha não confere com confirmação de senha");
        }

        Usuario user = buscarPorId(id);
        if(!user.getPassword().equals(senhaAtual)) {
            throw new RuntimeException("Sua senha não confere");
        }

        user.setPassword(novaSenha);
        return user;

    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
}
