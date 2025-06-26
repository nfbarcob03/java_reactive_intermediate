package com.example.webfluxintro.service;

import com.example.webfluxintro.model.Producto;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaProductoListener {
 
    public AuditoriaProductoListener(ProductoEventBus bus, DatabaseClient dbClient) {
        bus.flujoEventos()
           .flatMap(evento -> {
               Producto p = evento.getProducto();
               String desc = String.format("Producto %s: %s, Precio: %.2f",
                       evento.getTipo(), p.getNombre(), p.getPrecio());
 
               return dbClient.sql("INSERT INTO auditoria (entidad, tipo_evento, descripcion) VALUES (:entidad, :tipo, :desc)")
                       .bind("entidad", "Producto")
                       .bind("tipo", evento.getTipo())
                       .bind("desc", desc)
                       .then(); // Mono<Void>
           })
           .subscribe(
               null,
               err -> System.err.println("Error auditando producto: " + err.getMessage())
           );
    }
}