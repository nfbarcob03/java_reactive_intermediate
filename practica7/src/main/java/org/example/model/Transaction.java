package org.example.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Data
@Document("transactions")
public class Transaction {
    @Id
    private String id;
    @Positive
    private BigDecimal amount;
    @NotBlank
    private String currency;
    private TransactionType type;
    private TransactionStatus status;
    private Instant createdAt;
}