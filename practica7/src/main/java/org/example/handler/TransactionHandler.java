package org.example.handler;

import org.example.dto.CashRequestDto;
import org.example.service.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {
    private final TransactionService transactionService;

    public TransactionHandler(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    public Mono<ServerResponse> cashIn(ServerRequest req){
        return req.bodyToMono(CashRequestDto.class)
                .flatMap(transactionService::cashIn)
                .flatMap(transactionDto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(transactionDto));
    }

    public Mono<ServerResponse> cashOut(ServerRequest req){
        return req.bodyToMono(CashRequestDto.class)
                .flatMap(transactionService::cashOut)
                .flatMap(transactionDto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(transactionDto));
    }

    public Mono<ServerResponse> findById(ServerRequest req){
        return transactionService.findById(req.pathVariable("id"))
                .flatMap(transactionDto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(transactionDto))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}