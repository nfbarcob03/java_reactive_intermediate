package com.ejemplo.service;
import com.ejemplo.model.Cliente;
import com.ejemplo.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteService {
    private final ClienteRepository repo;

    public ClienteService(ClienteRepository repo) {
        this.repo = repo;
    }

    public Flux<Cliente> listar() {
        return repo.findAll();
    }

    public Mono<Cliente> guardar(Cliente cliente) {
        return repo.save(cliente);
    }
}
