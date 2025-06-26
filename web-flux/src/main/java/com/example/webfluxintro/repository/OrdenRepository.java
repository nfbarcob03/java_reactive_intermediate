package com.example.webfluxintro.repository;

import com.example.webfluxintro.model.Orden;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface OrdenRepository extends ReactiveCrudRepository<Orden, Long> {}