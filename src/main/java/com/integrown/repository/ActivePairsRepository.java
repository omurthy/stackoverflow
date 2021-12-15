package com.integrown.repository;

import com.integrown.domain.ActivePairs;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB reactive repository for the ActivePairs entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActivePairsRepository extends ReactiveMongoRepository<ActivePairs, String> {


}
