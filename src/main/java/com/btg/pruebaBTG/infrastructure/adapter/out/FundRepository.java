package com.btg.pruebaBTG.infrastructure.adapter.out;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.btg.pruebaBTG.domain.model.entities.Fund;
import com.btg.pruebaBTG.infrastructure.adapter.out.interfaces.IFundRepository;

@Repository
public interface FundRepository extends MongoRepository<Fund, String>, IFundRepository {

}
