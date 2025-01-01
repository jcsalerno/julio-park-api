package com.julio.park_api.repository;

import com.julio.park_api.entity.Cliente;
import com.julio.park_api.entity.ClienteVaga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteVagaRepository extends JpaRepository<ClienteVaga, Long> {
    Optional<ClienteVaga> findByReciboAndDataSaidaIsNull(String recibo);

    long countByClienteCpfAndDataSaidaIsNotNull(String cpf);
}
