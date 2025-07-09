package co.com.bancolombia.model.events;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoxDeletedEvent {
    private String boxId;
    private LocalDateTime deletedAt;
    private String responsibleUser;
}