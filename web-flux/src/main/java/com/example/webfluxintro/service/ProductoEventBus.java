package com.example.webfluxintro.service;

import com.example.webfluxintro.model.ProductoEvento;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component

public class ProductoEventBus {
 
    private final Sinks.Many<ProductoEvento> sink = Sinks.many().multicast().onBackpressureBuffer();
 
    public void publicarEvento(ProductoEvento evento) {

        sink.tryEmitNext(evento);

    }
 
    public Flux<ProductoEvento> flujoEventos() {

        return sink.asFlux();

    }

}
 