package co.com.bancolombia.api;

import co.com.bancolombia.model.ErrorResponse;
import co.com.bancolombia.model.box.UploadBoxReport;
import co.com.bancolombia.usecase.uploadmovements.UploadMovementsUseCase;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.ByteBuffer;
import java.util.Map;

@Component
public class MovementHandler {

    private final UploadMovementsUseCase uploadMovementsUseCase;

    public MovementHandler(UploadMovementsUseCase uploadMovementsUseCase) {
        this.uploadMovementsUseCase = uploadMovementsUseCase;
    }

    public Mono<ServerResponse> uploadCSV(ServerRequest serverRequest) {
        String boxId = serverRequest.pathVariable("boxId");
        return serverRequest.multipartData().flatMap(
                parts -> {
                    var filePart = parts.toSingleValueMap().get("file");
                    if (filePart == null) {
                        return ServerResponse.badRequest()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(Map.of("error", "File is missing"));
                    }
                    Flux<ByteBuffer> fileContent = filePart.content()
                            .map(dataBuffer -> {
                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bytes);
                                return ByteBuffer.wrap(bytes);
                            });
                    return uploadMovementsUseCase.uploadMovementCSV(boxId, fileContent)
                            .flatMap(result -> {
                                if (result instanceof UploadBoxReport) {
                                    return ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue(result);
                                }
                                return ServerResponse.badRequest()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(result);
                            })
                            .onErrorResume(e -> ServerResponse.badRequest()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(Map.of("error", e.getMessage())));
                });
    }

}
