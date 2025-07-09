package co.com.bancolombia.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Bean
    public RouterFunction<ServerResponse> boxRouterFunction(BoxHandler boxHandler) {
        return route(GET("/api/getBoxById/{id}"), boxHandler::getBoxById)
                .andRoute(PUT("/api/updateBoxName/{id}"), boxHandler::updateBoxName)
                .andRoute(DELETE("/api/deleteBox/{id}"), boxHandler::deleteBox)
                .andRoute(GET("/api/getBoxes"), boxHandler::listAllBoxes)
                .andRoute(PUT("/api/close/{id}"), boxHandler::close)
                .andRoute(PUT("/api/open/{id}"), boxHandler::open)
                .and(route(POST("/api"), boxHandler::createBox));
    }

    @Bean
    public RouterFunction<ServerResponse> movementsRouterFunction(MovementHandler movementHandler) {
        return(route(POST("/api/boxes/{boxId}/movements/upload"), movementHandler::uploadCSV));
    }
}
