package co.com.bancolombia.usecase.uploadmovements;

import co.com.bancolombia.model.box.UploadBoxReport;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.gateways.MovementRepository;
import co.com.bancolombia.model.movement.gateways.RenderFileRepository;

import co.com.bancolombia.model.movement.gateways.ValidateMovementRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class UploadMovementsUseCase {

    private  final MovementRepository movementRepository;
    private final RenderFileRepository renderFileRepository;
    private final ValidateMovementRepository movementValidationRepository;
    private final BoxRepository boxRepository;

    public Mono<UploadBoxReport> uploadMovementCSV(String boxId, Flux<ByteBuffer> fileBytes) {
        Set<String> movementIds = new HashSet<>();

        return boxRepository.findById(boxId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Box ID does not exist: " + boxId)))
                .thenMany(fileBytes.map(ByteBuffer::array)
                        .flatMapSequential(renderFileRepository::render)
                        .doOnNext(movement -> movementIds.add(movement.getMovementId()))
                        .flatMap(movement -> movementValidationRepository.validateMovement(movement, boxId, movementIds)
                                .map(isValid -> new Object[]{movement, isValid})))
                .collectList()
                .flatMap(results -> {
                    int total = movementIds.size();
                    int success = (int) results.stream().filter(result -> (boolean) result[1]).count();
                    int failed = total - success;

                    // Save only valid movements
                    Flux.fromIterable(results)
                            .filter(result -> (boolean) result[1])
                            .map(result -> (Movement) result[0])
                            .flatMap(movementRepository::save)
                            .subscribe();

                    // Create and return the UploadBoxReport
                    UploadBoxReport report = new UploadBoxReport(
                            boxId,
                            total,
                            success,
                            failed,
                            LocalDateTime.now(),
                            "ralzate"
                    );
                    return Mono.just(report);
                });
    }
}
