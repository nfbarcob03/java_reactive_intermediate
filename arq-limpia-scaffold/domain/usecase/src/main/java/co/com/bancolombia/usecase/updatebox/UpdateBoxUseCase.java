package co.com.bancolombia.usecase.updatebox;

import co.com.bancolombia.model.box.BoxStatus;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.BoxDeletedEvent;
import co.com.bancolombia.model.events.BoxNameUpdatedEvent;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class UpdateBoxUseCase {
    private final BoxRepository boxRepository;
    private final EventsGateway eventsGateway;


    public void updateBoxName(String boxId, String newName) {
        boxRepository.findById(boxId)
                .flatMap(box -> {
                    String oldName = box.getName();
                    box.setName(newName);
                    return boxRepository.save(box)
                            .flatMap(savedBox -> {
                                BoxNameUpdatedEvent event = BoxNameUpdatedEvent.builder()
                                        .boxId(boxId)
                                        .oldName(oldName)
                                        .newName(newName)
                                        .updatedAt(LocalDateTime.now())
                                        .responsibleUser(null) // Set the responsible user if available
                                        .build();
                                return eventsGateway.emitUpdateName(event).thenReturn(savedBox);
                            });
                })
                .subscribe();
    }

    public void logicDeleteBox(String boxId) {
        boxRepository.findById(boxId)
                .flatMap(box -> {
                    String oldName = box.getName();
                    box.setStatus(BoxStatus.DELETED);
                    return boxRepository.save(box)
                            .flatMap(savedBox -> {
                                BoxDeletedEvent event = BoxDeletedEvent.builder()
                                        .boxId(boxId)
                                        .deletedAt(LocalDateTime.now())
                                        .responsibleUser(null) // Set the responsible user if available
                                        .build();
                                return eventsGateway.emitDeletedBox(event).thenReturn(savedBox);
                            });
                })
                .subscribe();
    }
}
