package com.example.webfluxintro.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

@RestController
public class TrasnferController {

    private static final Set<String> LISTA_BLANCA = Set.of("JUAN", "PEDRO", "LAURA");

    @PostMapping("/procesar-transferencias")
    public Mono<TransferenciaResponse> procesarTransferencias(@RequestBody Mono<TransferenciaRequest> requestMono) {
        return requestMono.flatMap(request -> {
            Flux<String> resultados = Flux.fromIterable(request.getTransferencias())
                    .map(transferencia -> {
                        String destinatario = transferencia.getDestinatario().toUpperCase();
                        if (!LISTA_BLANCA.contains(destinatario)) {
                            return "❌ Transferencia rechazada: " + transferencia.getDestinatario() + " no es un destinatario autorizado.";
                        }
                        if (transferencia.getMonto() <= 0) {
                            return "❌ Transferencia rechazada: El monto debe ser mayor a 0.";
                        }
                        return "✅ Transferencia aprobada a " + transferencia.getDestinatario() + " por $" + transferencia.getMonto();
                    });

            return resultados.collectList().map(messages -> {
                TransferenciaResponse response = new TransferenciaResponse();
                response.setTransferencias(messages);
                return response;
            });
        });
    }
}