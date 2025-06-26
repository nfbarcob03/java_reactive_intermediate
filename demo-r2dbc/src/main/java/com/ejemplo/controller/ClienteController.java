package com.ejemplo.controller;
import com.ejemplo.model.Cliente;
import com.ejemplo.service.ClienteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "API para gestionar clientes")
public class ClienteController {
    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<Cliente> obtenerClientes() {
        return service.listar();
    }

    @PostMapping
    public Mono<Cliente> guardarCliente(@Valid @RequestBody Cliente cliente) {
        return service.guardar(cliente);
    }
}
