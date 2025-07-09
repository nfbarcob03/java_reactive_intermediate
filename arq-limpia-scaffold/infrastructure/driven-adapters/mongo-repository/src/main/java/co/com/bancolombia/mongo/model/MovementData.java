package co.com.bancolombia.mongo.model;


import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document("Movements")
@Builder(toBuilder = true)
public class MovementData {

    @Id
    private String id;
    private String movementId;
    private String boxId;
    private LocalDateTime date;
    private String type;
    private BigDecimal amount;
    private String description;
    private String currency;



}
