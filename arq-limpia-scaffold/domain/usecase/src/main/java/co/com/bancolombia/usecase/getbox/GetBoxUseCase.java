package co.com.bancolombia.usecase.getbox;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.model.events.gateways.EventsGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class GetBoxUseCase {

    private final BoxRepository boxRepository;
    private final EventsGateway eventsGateway;

    public  Mono<Box> getBoxById(String boxId) {
        return boxRepository.findById(boxId);
    }

    public Flux<Box> listAllBoxes(String status, String responsible, String openingDate, String closedAt, String currentBalance) {
        return boxRepository.findAllWithFilters(status, responsible, openingDate, closedAt, currentBalance);
    }
}
