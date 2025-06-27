package org.example.config;

import org.example.handler.TransactionHandler;
import org.example.service.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(TransactionHandler transactionHandler) {
        return org.springframework.web.reactive.function.server.RouterFunctions
                .route()
                .POST("/cash-in", transactionHandler::cashIn)
                .POST("/cash-out", transactionHandler::cashOut)
                //.POST("/cash-out-reply", transactionHandler::cashOutReply)
                .GET("/tx/{id}", transactionHandler::findById)
                .build();
    }
    // rutas funcionales
}