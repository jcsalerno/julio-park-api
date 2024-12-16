package com.julio.park_api.service;

import com.julio.park_api.entity.Usuario;
import com.julio.park_api.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    public void setUp() {
        // Inicializa os mocks antes de cada teste
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSalvarUsuario() {
        // Criação de um usuário de teste
        Usuario usuario = new Usuario();
        usuario.setUsername("johndoe");
        usuario.setPassword("senha123");
        usuario.setRole(Usuario.Role.ROLE_ADMIN);
        usuario.setDataCriacao(LocalDateTime.now());
        usuario.setDataModificacao(LocalDateTime.now());

        // Simula o comportamento do repositório
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Chama o método de salvar no serviço
        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        // Verifica se o usuário foi salvo corretamente
        assertNotNull(usuarioSalvo);
        assertEquals("johndoe", usuarioSalvo.getUsername());
        assertEquals("senha123", usuarioSalvo.getPassword());
        assertEquals(Usuario.Role.ROLE_ADMIN, usuarioSalvo.getRole());

        // Verifica se o método save do repositório foi chamado uma vez
        verify(usuarioRepository, times(1)).save(usuario);
    }
}
