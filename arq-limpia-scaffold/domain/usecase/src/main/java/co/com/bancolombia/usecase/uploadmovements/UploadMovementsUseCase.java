package co.com.bancolombia.usecase.uploadmovements;

import co.com.bancolombia.model.movement.Movement;
import co.com.bancolombia.model.movement.gateways.MovementRepository;
import co.com.bancolombia.model.movement.gateways.RenderFileRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

@RequiredArgsConstructor
public class UploadMovementsUseCase {

    private  final MovementRepository movementRepository;
    private final RenderFileRepository renderFileRepository;

    public Flux<Movement> uploadMovementCSV(String boxId, Flux<ByteBuffer> fileBytes) {
        return fileBytes.map(ByteBuffer::array).flatMapSequential(bytes ->{
            return renderFileRepository.render(bytes);
        }).flatMap(movemen -> {
            if(movemen.getBoxId()==null || movemen.getBoxId().equals("")){
                movemen.setBoxId(boxId);
            }
            return Mono.just(movemen);
        }).flatMap(movement -> movementRepository.save(movement));
    }

}
