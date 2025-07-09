package co.com.bancolombia.mongo.adapter;


import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.gateways.MovementRepository;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import co.com.bancolombia.mongo.model.MovementData;
import co.com.bancolombia.mongo.repository.MongoDBMovementRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Repository
public class MongoMovementRepositoryAdapter extends AdapterOperations<Movement, MovementData, String, MongoDBMovementRepository>
 implements MovementRepository
{

    public MongoMovementRepositoryAdapter(MongoDBMovementRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, Movement.MovementBuilder.class).build());
    }
    @Override
    public Mono<Movement> save(Movement movement) {
        MovementData movementData = MovementData.builder()
                .id(movement.getId())
                .boxId(movement.getBoxId())
                .amount(movement.getAmount())
                .type(movement.getType())
                .date(movement.getDate())
                .build();
        return repository.save(movementData)
                .map(this::toEntity);
    }
}
