package co.com.bancolombia.model.movement.gateways;

import co.com.bancolombia.model.movement.Movement;
import reactor.core.publisher.Flux;

public interface RenderFileRepository {
    Flux<Movement> render(byte[] bytes);
}
