package org.example.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.awt.image.DataBuffer;
import java.time.Instant;
import java.util.Map;

public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex){
            mapper.registerModule(new JavaTimeModule());
        HttpStatus status = ex instanceof IllegalArgumentException?HttpStatus.BAD_REQUEST:HttpStatus.INTERNAL_SERVER_ERROR;
        Map<String,Object> error = Map.of(
                "timestamp", Instant.now(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", ex.getMessage()
        );
        byte [] bytes = new byte[0];
        try{
            bytes = mapper.writeValueAsBytes(error);
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }

        ServerHttpResponse response = (ServerHttpResponse) exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        DataBufferFactory dataBufferFactory = response.bufferFactory();
        return response.writeWith(Mono.just(dataBufferFactory.wrap(bytes)));

    }
    // manejo de errores global
}