package co.com.bancolombia.events.handlers;

import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.impl.config.annotations.EnableEventListeners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

@EnableEventListeners
public class EventsHandler {
    private static final Logger log = LoggerFactory.getLogger(EventsHandler.class);

    public EventsHandler(/*SampleUseCase sampleUseCase*/) {
        //this.sampleUseCase = sampleUseCase;
    }

    public Mono<Void> handleEventA(DomainEvent<Object> event) {
        log.info("Event received: {} -> {}", event.getName(), event.getData());
        return Mono.empty();
    }

    public Mono<Void> handleEventUpdateName(DomainEvent<Object> event) {
        log.info("Event update name box received: {} -> {}", event.getName(), event.getData());
        return Mono.empty();
    }

    public Mono<Void> handleEventDeletedBox(DomainEvent<Object> event) {
        log.info("Event deleted box received: {} -> {}", event.getName(), event.getData());
        return Mono.empty();
    }

    public Mono<Void> handleEventErrorValidationMovement(DomainEvent<Object> event) {
        log.info("Event error movement validation received: {} -> {}", event.getName(), event.getData());
        return Mono.empty();
    }

    public Mono<Void> handleEventMovementUpload(DomainEvent<Object> event) {
        log.info("Event upload movement received: {} -> {}", event.getName(), event.getData());
        return Mono.empty();
    }
}
