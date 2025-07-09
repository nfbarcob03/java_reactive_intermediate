package co.com.bancolombia.util;

import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.gateways.RenderFileRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Component
public class RenderUtil implements RenderFileRepository {
    public Flux<Movement> render(byte[] bytes){
        Reader in = new InputStreamReader(new ByteArrayInputStream(bytes));
       try{
           Iterable<CSVRecord> records = CSVFormat.DEFAULT
                   .withFirstRecordAsHeader()
                   .parse(in);
           return Flux.fromIterable(records).flatMap(this::castRecordToMovement);
       }catch (IOException e){
           return Flux.error(new IllegalArgumentException("error parsiong CSV", e));
       }
    }

    private Mono<Movement> castRecordToMovement(CSVRecord record){
        try{
            String movementsID = record.get("movementId").trim();
            String boxId = record.get("boxId").trim();
            String type = record.get("type").trim();
            BigDecimal amount = new BigDecimal(record.get("amount").trim());
            String description = record.get("description").trim();
            String currency = record.get("currency").trim();
            LocalDateTime date = LocalDateTime.parse(record.get("date").trim());

            if(!type.equals(("INCOME")) && !type.equals("EXPENSE")) {
                return Mono.error(new IllegalArgumentException("Invalid type: " + type));
            }

            if(amount.compareTo(BigDecimal.ZERO)<=0){
                return Mono.error(new IllegalArgumentException("Amount must be greater than zero"));
            }

            Movement movement = Movement.builder()
                    .movementId(movementsID)
                    .boxId(boxId)
                    .type(type)
                    .amount(amount)
                    .description(description)
                    .currency(currency)
                    .date(date)
                    .build();
            return Mono.just(movement);
        } catch (Exception e) {
            return Mono.error(new IllegalArgumentException("Error parsing record: " + e.getMessage()));
        }
    }
}
