package org.example.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import org.example.model.Transaction;
import reactor.core.publisher.Mono;

public class LedgerClient {
    private final WebClient client;
    private static final Logger log = LoggerFactory.getLogger(LedgerClient.class);

    public LedgerClient(WebClient client) {
        this.client = client;
    }

    public Mono<Transaction> postEntry(Transaction transaction) {
        log.info("POST /ledger/entriees - Request body {}", transaction);
        return client.post().uri("/ledger/entries")
                .bodyValue(transaction)
                .retrieve()
                .bodyToMono(Transaction.class)
                .doOnSuccess(response -> log.info("Response from /ledger/entries - Response body {}", response))
                .doOnError(error -> log.error("Error posting to /ledger/entries - Error occurred: {}", error.getMessage()));
    }
}