package co.com.bancolombia.model.movement.gateways;

import co.com.bancolombia.model.movement.Movement;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface ValidateMovementRepository {
    Mono<Boolean> validateMovement(Movement movement, String boxId, Set<String> movementIds);
}
