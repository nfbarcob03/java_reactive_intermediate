package org.example.handler;

import org.example.dto.CashRequestDto;
import org.example.service.LedgerClient;
import org.example.service.LedgerRequestReplyClient;
import org.example.service.TopicPublisher;
import org.example.service.TransactionService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {
    private final TransactionService transactionService;
    private final LedgerRequestReplyClient ledgerClient;

    private final TopicPublisher topicPublisher;

    public TransactionHandler(TransactionService transactionService, LedgerClient ledgerClient, LedgerRequestReplyClient ledgerClient1, TopicPublisher topicPublisher) {
        this.transactionService = transactionService;
        this.ledgerClient = ledgerClient1;
        this.topicPublisher = topicPublisher;
    }


    public Mono<ServerResponse> cashIn(ServerRequest req){
        return req.bodyToMono(CashRequestDto.class)
                .flatMap(transactionService::cashIn)
                .flatMap(tx->topicPublisher.publishTransaction(tx).thenReturn(tx))
                .flatMap(transactionDto -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(transactionDto));
    }


    public Mono<ServerResponse> cashOut(ServerRequest req){
        return req.bodyToMono(CashRequestDto.class)
                .flatMap(transactionService::cashOut)
                .flatMap(tx->topicPublisher.publishTransaction(tx).thenReturn(tx))
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

    public Mono<ServerResponse> cashOutReply(ServerRequest request) {
        return request.bodyToMono(CashRequestDto.class)
                .flatMap(transactionService::cashOut)
                .flatMap(ledgerClient::sendTransaction) // Envia por AMQP y espera respuesta
                .flatMap(tx -> ServerResponse.ok().bodyValue(tx));
    }
}