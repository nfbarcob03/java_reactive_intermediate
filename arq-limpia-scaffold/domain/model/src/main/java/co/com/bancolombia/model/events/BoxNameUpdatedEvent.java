package co.com.bancolombia.model.events;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BoxNameUpdatedEvent {
    private String boxId;
    private String oldName;
    private String newName;
    private LocalDateTime updatedAt;
    private String responsibleUser;
}