package co.com.bancolombia.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BoxMovementsUploadedEvent {
    private String boxId;
    private int totalLines;
    private int totalValid;
    private int totalInvalid;
    private String date;
    private String user;
}