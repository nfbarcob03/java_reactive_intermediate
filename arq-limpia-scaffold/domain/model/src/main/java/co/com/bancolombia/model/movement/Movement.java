package co.com.bancolombia.model.movement;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Movement {

    private String id;
    private String movementId;
    private String boxId;
    private LocalDateTime date;
    private String type;
    private BigDecimal amount;
    private String description;
    private String currency;



}
