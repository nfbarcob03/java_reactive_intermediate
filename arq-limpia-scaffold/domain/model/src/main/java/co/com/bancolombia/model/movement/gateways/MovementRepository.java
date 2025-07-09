package co.com.bancolombia.model.movement.gateways;

import co.com.bancolombia.model.movement.Movement;
import reactor.core.publisher.Mono;

public interface MovementRepository {
    Mono<Movement> save(Movement movement);
}
