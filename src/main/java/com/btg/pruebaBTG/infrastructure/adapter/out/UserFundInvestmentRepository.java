package com.btg.pruebaBTG.infrastructure.adapter.out;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.btg.pruebaBTG.domain.model.entities.UserFundInvestment;
import com.btg.pruebaBTG.domain.model.enums.UserFundInvestmentStatus;

@Repository
public interface UserFundInvestmentRepository extends MongoRepository<UserFundInvestment, String> {
    Optional<UserFundInvestment> findByUserIdAndFundId(String userId, String fundId);
    Optional<UserFundInvestment> findByUserIdAndFundIdAndStatus(String userId, String fundId, UserFundInvestmentStatus status);
}
