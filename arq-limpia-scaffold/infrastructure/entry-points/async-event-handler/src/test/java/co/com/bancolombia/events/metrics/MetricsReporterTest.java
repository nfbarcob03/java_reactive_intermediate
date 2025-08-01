package co.com.bancolombia.events.metrics;

import io.micrometer.core.instrument.logging.LoggingMeterRegistry;
import io.micrometer.core.instrument.logging.LoggingRegistryConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.async.api.AsyncQuery;
import org.reactivecommons.async.commons.communications.Message;
import reactor.test.StepVerifier;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.core.instrument.Timer;
import static org.assertj.core.api.Assertions.assertThat;

public class MetricsReporterTest {

    private MetricsReporter metricsReporter;
    private Exception error;
    private Message message;

    @BeforeEach
    void setUp(){
        LoggingMeterRegistry loggingMeterRegistry = LoggingMeterRegistry
            .builder(LoggingRegistryConfig.DEFAULT)
            .build();

        metricsReporter = new MetricsReporter(loggingMeterRegistry);
        metricsReporter.reportMetric("type","path",10L,true);

        error = new IllegalArgumentException("Error");

        message = new Message() {
            @Override
            public byte[] getBody() {
                return new byte[0];
            }

            @Override
            public Properties getProperties() {
                return null;
            }
        };
    }

    @Test
    void reportErrorCommandTest() {
        Command<String> command = new Command<>("name", "commandID", "data");

        StepVerifier
            .create(metricsReporter.reportError(error, message, command, true))
            .verifyComplete();
    }

    @Test
    void reportErrorDomainEventTest() {
        DomainEvent<String> domainEvent = new DomainEvent<>("name", "eventID", "data");

        StepVerifier
            .create(metricsReporter.reportError(error, message, domainEvent, true))
            .verifyComplete();
    }

    @Test
    void reportErrorAsyncQueryTest() {
        AsyncQuery<String> asyncQuery = new AsyncQuery<>("resource", "data");

        StepVerifier
            .create(metricsReporter.reportError(error, message, asyncQuery, true))
            .verifyComplete();
    }
    
    @Test
    void reportMetricTest() {
        MeterRegistry registry = new SimpleMeterRegistry();
        MetricsReporter reporter = new MetricsReporter(registry);

        reporter.reportMetric("command", "handlerPath", 100L, true);
        reporter.reportMetric("event", "handlerPath", 200L, false);

        Timer successTimer = registry.find("async_operation_flow_duration")
                                    .tags("type", "command", "operation", "handlerPath", "status", "success")
                                    .timer();
        Timer errorTimer = registry.find("async_operation_flow_duration")
                                .tags("type", "event", "operation", "handlerPath", "status", "error")
                                .timer();

        assertThat(successTimer).isNotNull();
        assertThat(errorTimer).isNotNull();
    }
}