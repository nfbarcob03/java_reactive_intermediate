package com.example.webfluxintro.repository;

import com.example.webfluxintro.model.Producto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {}

