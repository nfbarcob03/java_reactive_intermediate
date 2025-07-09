package co.com.bancolombia.model.events.gateways;

import reactor.core.publisher.Mono;

public interface EventsGateway {
    Mono<Void> emit(Object event);

    Mono<Void> emitUpdateName(Object event);
    Mono<Void> emitDeletedBox(Object event);

}
