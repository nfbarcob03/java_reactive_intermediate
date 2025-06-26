package org.example.repo;

import org.example.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

    // Define custom query methods if needed
    // For example:
    // Mono<Transaction> findByTransactionId(String transactionId);
    // Flux<Transaction> findByStatus(String status);
}
