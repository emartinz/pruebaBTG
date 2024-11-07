package com.btg.pruebaBTG.infrastructure.adapter.out.interfaces;

import java.util.List;

import com.btg.pruebaBTG.domain.model.entities.Transaction;

public interface ITransactionRepository {
    Transaction save(Transaction transaction);
    List<Transaction> findByUserId(String userId);
}