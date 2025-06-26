package com.example.webfluxintro.repository;

import com.example.webfluxintro.model.Tarea;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// org.springframework.stereotype.Repository; // Opcional, Spring Data lo detecta



// @Repository // No es estrictamente necesario para interfaces de Spring Data

public interface TareaRepository extends ReactiveCrudRepository<Tarea, String> {

    // Tarea es el tipo de la entidad, String es el tipo del @Id

}