package co.com.bancolombia.events.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.api.domain.DomainEvent;
import reactor.test.StepVerifier;

import java.util.UUID;

class EventsHandlerTest {
    private EventsHandler eventsHandler;

    @BeforeEach
    void setUp() {
        eventsHandler = new EventsHandler();
    }

    @Test
    void handleEventATest() {
        DomainEvent<Object> event = new DomainEvent<>("EVENT", UUID.randomUUID().toString(), "Data");
        StepVerifier.create(eventsHandler.handleEventA(event)).expectComplete().verify();
    }
}
