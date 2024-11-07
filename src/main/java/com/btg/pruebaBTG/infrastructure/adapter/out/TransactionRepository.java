package com.btg.pruebaBTG.infrastructure.adapter.out;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.btg.pruebaBTG.domain.model.entities.Transaction;
import com.btg.pruebaBTG.infrastructure.adapter.out.interfaces.ITransactionRepository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String>, ITransactionRepository {
    List<Transaction> findByUserId(String userId);
}