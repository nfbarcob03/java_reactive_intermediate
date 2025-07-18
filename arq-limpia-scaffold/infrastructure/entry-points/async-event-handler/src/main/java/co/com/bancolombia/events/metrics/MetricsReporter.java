package co.com.bancolombia.events.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.commons.communications.Message;
import org.reactivecommons.async.commons.ext.CustomReporter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class MetricsReporter implements CustomReporter {
    private final MeterRegistry registry;

    public MetricsReporter(MeterRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void reportMetric(String type, String handlerPath, Long duration, boolean success) {
        String status = success ? "success" : "error";
        registry.timer("async_operation_flow_duration",
                        "exception", "", "type",
                        type, "operation", handlerPath,
                        "status", status);
                  // .record(Duration.ofMillis(duration));
    }

    @Override
    public Mono<Void> reportError(Throwable ex, Message rawMessage, Command<?> message, boolean redelivered) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> reportError(Throwable ex, Message rawMessage, DomainEvent<?> message, boolean redelivered) {
        return Mono.empty();
    }

    @Override
    public Mono<Void> reportError(Throwable ex, Message rawMessage, AsyncQuery<?> message, boolean redelivered) {
        return Mono.empty();
    }
}
