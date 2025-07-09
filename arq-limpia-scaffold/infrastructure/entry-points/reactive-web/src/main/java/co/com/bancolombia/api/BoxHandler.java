package co.com.bancolombia.api;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.usecase.closebox.CloseBoxUseCase;
import co.com.bancolombia.usecase.createbox.CreateBoxUseCase;
import co.com.bancolombia.usecase.getbox.GetBoxUseCase;
import co.com.bancolombia.usecase.openbox.OpenBoxUseCase;
import co.com.bancolombia.usecase.updatebox.UpdateBoxUseCase;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Component
public class BoxHandler {

    private final CreateBoxUseCase createBoxUseCase;
    private final OpenBoxUseCase openBoxUseCase;
    private final CloseBoxUseCase closeBoxUseCase;
    private final GetBoxUseCase getBoxUseCase;
    private final UpdateBoxUseCase updateBoxUseCase;


    public BoxHandler(CreateBoxUseCase createBoxUseCase, OpenBoxUseCase openBoxUseCase, CloseBoxUseCase closeBoxUseCase, GetBoxUseCase getBoxUseCase, UpdateBoxUseCase updateBoxUseCase) {
        this.createBoxUseCase = createBoxUseCase;
        this.openBoxUseCase = openBoxUseCase;
        this.closeBoxUseCase = closeBoxUseCase;
        this.getBoxUseCase = getBoxUseCase;
        this.updateBoxUseCase = updateBoxUseCase;
    }

    public Mono<ServerResponse> createBox(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(Box.class).flatMap(box -> {
            return createBoxUseCase.createBox(box.getId(), box.getName());
        }).flatMap(currentBox -> ServerResponse.ok().body(BodyInserters.fromValue(currentBox)));
    }

    public Mono<ServerResponse> open(ServerRequest request){
        String id = request.pathVariable("id");
        //Se puede agregar un monto inicial de apertura
        return openBoxUseCase.openBox(id, BigDecimal.ZERO).flatMap(currentPet -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(currentPet)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> close(ServerRequest request){
        String id = request.pathVariable("id");
        return closeBoxUseCase.closeBox(id, BigDecimal.ZERO).flatMap(currentPet -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(currentPet)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> getBoxById(ServerRequest request){
        String id = request.pathVariable("id");
        return getBoxUseCase.getBoxById(id).flatMap(box -> ServerResponse.ok()
                        .body(BodyInserters.fromValue(box)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listAllBoxes(ServerRequest request) {
        String status = request.queryParam("status").orElse(null);
        String responsible = request.queryParam("responsible").orElse(null);
        String openingDate = request.queryParam("openedAt").orElse(null);
        String closedAt = request.queryParam("closedAt").orElse(null);
        String currentBalance = request.queryParam("currentBalance").orElse(null);

        return getBoxUseCase.listAllBoxes(status, responsible, openingDate, closedAt, currentBalance)
                .collectList()
                .flatMap(boxes -> ServerResponse.ok().body(BodyInserters.fromValue(boxes)))
                .switchIfEmpty(ServerResponse.noContent().build());
    }


    public Mono<ServerResponse> updateBoxName(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");
        return serverRequest.bodyToMono(Box.class)
                .flatMap(box -> {
                    if (box.getName() == null || box.getName().isEmpty()) {
                        return Mono.error(new IllegalArgumentException("Box \"name\" cannot be empty"));
                    }
                    return Mono.fromRunnable(() -> updateBoxUseCase.updateBoxName(id, box.getName()));
                })
                .then(ServerResponse.ok().build())
                .onErrorResume(e -> ServerResponse.badRequest().body(BodyInserters.fromValue(e.getMessage())));
    }

    public Mono<ServerResponse> deleteBox(ServerRequest serverRequest) {
        String id = serverRequest.pathVariable("id");

        return Mono.fromRunnable(() -> updateBoxUseCase.logicDeleteBox(id))
                .then(ServerResponse.ok().build())
                .onErrorResume(e -> ServerResponse.badRequest().body(BodyInserters.fromValue(e.getMessage())));
    }


}
