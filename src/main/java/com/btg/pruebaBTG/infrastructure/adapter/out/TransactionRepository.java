package com.btg.pruebaBTG.infrastructure.adapter.out;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.btg.pruebaBTG.domain.model.entities.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUserId(String userId);
    List<Transaction> findByUserIdAndType(String userId, String type);
}