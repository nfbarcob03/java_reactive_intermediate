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
    private final DomainEventBus domainEventBus;

    public ReactiveEventsGateway(DomainEventBus domainEventBus) {
        this.domainEventBus = domainEventBus;
    }

    @Override
    public Mono<Void> emit(Object event) {
         return from(domainEventBus.emit(new DomainEvent<>(BOX_CREATED, UUID.randomUUID().toString(), event)));
    }
}
