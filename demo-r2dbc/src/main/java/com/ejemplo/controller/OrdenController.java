package com.ejemplo.controller;

import com.ejemplo.model.Orden;
import com.ejemplo.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ordenes")
@Tag(name = "Ordenes", description = "API para gestionar órdenes")
public class OrdenController {
    private final OrdenService service;

    public OrdenController(OrdenService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todas las órdenes")
    public Flux<Orden> obtenerOrdenes() {
        return service.listar();
    }

    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar órdenes por ID de cliente")
    public Flux<Orden> obtenerPorCliente(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @PostMapping
    @Operation(summary = "Guardar una nueva orden")
    public Mono<Orden> guardarOrden(@Valid @RequestBody Orden orden) {
        return service.guardar(orden);
    }
}
