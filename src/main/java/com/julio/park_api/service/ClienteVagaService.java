package com.julio.park_api.service;
import com.julio.park_api.entity.ClienteVaga;
import com.julio.park_api.exception.EntityNotFoundException;
import com.julio.park_api.repository.ClienteVagaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Repository
public class ClienteVagaService {

    private final ClienteVagaRepository repository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga) {
        return repository.save(clienteVaga);
    }

    @Transactional(readOnly = true)
    public ClienteVaga buscarPorRecibo(String recibo) {
        return repository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
                () -> new EntityNotFoundException(
                        String.format(
                                "Recibo '%s' não encontrado no sistema ou check-out já relizado.", recibo
                        )
                )
        );
    }


    @Transactional(readOnly = true)
    public long getTotalDeVezesEstacionamentoCompleto(String cpf) {
        return repository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }
}
