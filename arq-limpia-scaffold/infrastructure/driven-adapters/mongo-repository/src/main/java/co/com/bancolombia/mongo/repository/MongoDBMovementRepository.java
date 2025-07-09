package co.com.bancolombia.mongo.repository;

import co.com.bancolombia.mongo.model.MovementData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;

public interface MongoDBMovementRepository extends ReactiveMongoRepository<MovementData, String>, ReactiveQueryByExampleExecutor<MovementData> {
}
