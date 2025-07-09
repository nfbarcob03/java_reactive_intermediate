package co.com.bancolombia.mongo.adapter;

import co.com.bancolombia.model.box.Box;
import co.com.bancolombia.model.box.gateways.BoxRepository;
import co.com.bancolombia.mongo.helper.AdapterOperations;
import co.com.bancolombia.mongo.model.BoxData;
import co.com.bancolombia.mongo.repository.MongoDBBoxRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class MongoBoxRepositoryAdapter extends AdapterOperations<Box, BoxData, String, MongoDBBoxRepository>
 implements BoxRepository
{
    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public MongoBoxRepositoryAdapter(MongoDBBoxRepository repository, ObjectMapper mapper, ReactiveMongoTemplate reactiveMongoTemplate) {
        super(repository, mapper, d -> mapper.map(d, Box.class));
        this.reactiveMongoTemplate = reactiveMongoTemplate;
    }

    public Mono<Box> findById(String id){
        return repository.findById(id).map(this::toEntity);
    }

    public Mono<Box> save(Box box){
        BoxData boxObject = new BoxData();
        boxObject.setName(box.getName());
        boxObject.setClosedAt(box.getClosedAt());
        boxObject.setId(box.getId());
        boxObject.setClosingAmount(box.getClosingAmount());
        boxObject.setOpenedAt(box.getOpenedAt());
        boxObject.setStatus(box.getStatus());
        boxObject.setCurrentBalance(box.getCurrentBalance());
        boxObject.setClosedAt(box.getClosedAt());
        return repository.save(boxObject).map(this::toEntity);
    }

    @Override
    public Flux<Box> findAllWithFilters(String status, String responsible, String openingDate, String closedAt, String currentBalance) {
        Query query = new Query();

        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status.toUpperCase()));
        }
        if (responsible != null) {
            query.addCriteria(Criteria.where("responsible").is(responsible));
        }
        if (openingDate != null) {
            query.addCriteria(Criteria.where("openedAt").is(openingDate));
        }
        if (closedAt != null) {
            query.addCriteria(Criteria.where("closedAt").is(closedAt));
        }
        if (currentBalance != null) {
            query.addCriteria(Criteria.where("currentBalance").is(currentBalance));
        }

        return reactiveMongoTemplate.find(query, BoxData.class)
                .map(this::toEntity);
    }

}
