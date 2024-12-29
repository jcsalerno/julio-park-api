package com.julio.park_api.service;

import com.julio.park_api.entity.Cliente;
import com.julio.park_api.exception.CpfUniqueViolationExcpetion;
import com.julio.park_api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public Cliente salvar(Cliente cliente) {
        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationExcpetion(String.format("CPF '%s' não pode ser cadastrado, já existe no sistema",
                    cliente.getCpf()));
        }
    }
}
