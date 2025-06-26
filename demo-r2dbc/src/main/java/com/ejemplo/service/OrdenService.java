package com.ejemplo.service;

import com.ejemplo.model.Orden;
import com.ejemplo.repository.OrdenRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrdenService {
    private final OrdenRepository repo;

    public OrdenService(OrdenRepository repo) {
        this.repo = repo;
    }

    public Flux<Orden> listar() {
        return repo.findAll();
    }

    public Flux<Orden> listarPorCliente(Long clienteId) {
        return repo.findByClienteId(clienteId);
    }

    public Mono<Orden> guardar(Orden orden) {
        return repo.save(orden);
    }
}
