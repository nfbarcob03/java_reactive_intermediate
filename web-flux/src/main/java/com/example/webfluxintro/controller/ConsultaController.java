package com.example.webfluxintro.controller;

import com.example.webfluxintro.dto.ProductoVentasDTO;
import com.example.webfluxintro.model.Orden;
import com.example.webfluxintro.model.Producto;
import com.example.webfluxintro.service.ConsultaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {
 
    private final ConsultaService consultaService;
 
    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping("/ordenes/cliente/{clienteId}")
    public Flux<Orden> getOrdenesPorClienteYFecha(
            @PathVariable Long clienteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {
        return consultaService.ordenesRealizadasPorFecha(clienteId, desde, hasta);
    }

    @GetMapping("/ordenes/estado/{estado}")
    public Flux<Orden> getOrdenesPorEstado(
            @PathVariable String estado) {
        return consultaService.ordenesConEstadoEspecifico(estado);
    }

    @GetMapping("/ventas/por-cliente/")
    public Flux<String> getTotalVentasPorCliente() {
        return consultaService.totalDeVentaOrdenes();
    }

    @GetMapping("/ventas/por-producto/")
    public Flux<String> getTotalVentasPorProducto() {
        return consultaService.totalVentasPorProducto();
    }

    @GetMapping("/ventas/orden-detalle/{idOrden}")
    public Mono<String> getTotalVentasPorProducto(@PathVariable Long idOrden) {
        return consultaService.ordenYDetallesPorId(idOrden);
    }


    @GetMapping("/productos/mas-comprados-filtrados")
    public Flux<ProductoVentasDTO> getProductosMasComprados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort,
        @RequestParam(defaultValue = "") String keyword){
        return consultaService.findProductosMasCompradosFiltrados(page, size, sort, keyword);
    }

    @PostMapping("/productos/crear")
    public Mono<Producto> crearProducto(@RequestBody Producto producto) {
        return consultaService.create(producto);
    }
}