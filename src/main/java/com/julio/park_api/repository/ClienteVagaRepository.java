package com.julio.park_api.repository;

import com.julio.park_api.entity.Cliente;
import com.julio.park_api.entity.ClienteVaga;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteVagaRepository extends JpaRepository<ClienteVaga, Long> {
}
