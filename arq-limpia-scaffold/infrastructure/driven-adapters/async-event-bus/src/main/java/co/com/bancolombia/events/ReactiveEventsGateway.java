package co.com.bancolombia.events;

import co.com.bancolombia.model.events.gateways.EventsGateway;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import reactor.core.publisher.Mono;

import java.util.UUID;

import static reactor.core.publisher.Mono.from;

@EnableDomainEventBus
public class ReactiveEventsGateway implements EventsGateway {
    public static final String BOX_CREATED = "box.event.created";
    public static final String BOX_NAME_UPDATE = "box.event.name.update";
    public static final String BOX_NAME_DELETED = "box.event.deleted";
    public static final String MOVEMENT_VALIDATION_ERRORS = "movement.event.validation.error";

    public static final String MOVEMENT_UPLOAD = "movement.event.upload";
    private final DomainEventBus domainEventBus;

    public ReactiveEventsGateway(DomainEventBus domainEventBus) {
        this.domainEventBus = domainEventBus;
    }

    @Override
    public Mono<Void> emit(Object event) {
         return from(domainEventBus.emit(new DomainEvent<>(BOX_CREATED, UUID.randomUUID().toString(), event)));
    }

    @Override
    public Mono<Void> emitUpdateName(Object event) {
        return from(domainEventBus.emit(new DomainEvent<>(BOX_NAME_UPDATE, UUID.randomUUID().toString(), event)));
    }

    @Override
    public Mono<Void> emitDeletedBox(Object event) {
        return from(domainEventBus.emit(new DomainEvent<>(BOX_NAME_DELETED, UUID.randomUUID().toString(), event)));
    }

    @Override
    public Mono<Void> emitMovementsErrors(Object event) {
        return from(domainEventBus.emit(new DomainEvent<>(MOVEMENT_VALIDATION_ERRORS, UUID.randomUUID().toString(), event)));
    }

    @Override
    public Mono<Void> emitMovementUploadEvent(Object event) {
        return from(domainEventBus.emit(new DomainEvent<>(MOVEMENT_UPLOAD, UUID.randomUUID().toString(), event)));
    }
}
