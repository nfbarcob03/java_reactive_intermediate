package org.example.service;

import org.example.dto.CashRequestDto;
import org.example.dto.TransactionDto;
import org.example.model.Transaction;
import org.example.model.TransactionStatus;
import org.example.model.TransactionType;
import org.example.repo.TransactionRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.Instant;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final LedgerClient ledgerClient;
    private final Publisher publisher;

    public TransactionService(TransactionRepository transactionRepository, LedgerClient ledgerClient, Publisher publisher) {
        this.transactionRepository = transactionRepository;
        this.ledgerClient = ledgerClient;
        this.publisher = publisher;
    }

    public Mono<TransactionDto> cashIn(CashRequestDto req){
        Transaction tx = Transaction.builder().amount(req.amount())
                .type(TransactionType.CASH_IN)
                .currency(req.currency()).type(TransactionType.CASH_IN)
                .status(TransactionStatus.PENDING)
                .createdAt(Instant.now()).build();

        return transactionRepository.save(tx)
                .flatMap(ledgerClient::postEntry)
                .map(this::toDto)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorResume(e -> rollback(tx, e));
    }


    public Mono<TransactionDto> cashOut(CashRequestDto req){
        Transaction tx = Transaction.builder().amount(req.amount())
                .type(TransactionType.CASH_OUT)
                .currency(req.currency()).type(TransactionType.CASH_OUT)
                .status(TransactionStatus.PENDING)
                .createdAt(Instant.now()).build();

        return transactionRepository.save(tx)
                .flatMap(ledgerClient::postEntry)
                .map(this::toDto)
                .retryWhen(Retry.fixedDelay(3, Duration.ofSeconds(1)))
                .onErrorResume(e -> rollback(tx, e));
    }

    public Mono<TransactionDto> findById(String id) {
        return transactionRepository.findById(id)
                .map(this::toDto);
                //.switchIfEmpty(Mono.error(new ChangeSetPersister.NotFoundException()));
    }

    private Mono<TransactionDto> rollback(Transaction tx, Throwable e) {
        tx.setStatus(TransactionStatus.FAILED);
        return transactionRepository.save(tx).then(Mono.error(e));
    }

    private TransactionDto toDto(Transaction transaction) {
        return TransactionDto.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .type(transaction.getType())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}