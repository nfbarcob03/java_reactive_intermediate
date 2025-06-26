package com.example.webfluxintro.repository;

import com.example.webfluxintro.model.DetalleOrden;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface DetalleOrdenRepository extends ReactiveCrudRepository<DetalleOrden, Long> {
    Flux<DetalleOrden> findByOrdenId(Long ordenId);
}