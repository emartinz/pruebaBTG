package com.btg.pruebaBTG.infrastructure.adapter.out;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.btg.pruebaBTG.domain.model.entities.Fund;

@Repository
public interface FundRepository extends MongoRepository<Fund, String> {
    Optional<Fund> findById(String id);
    boolean existsByName(String name);
}
