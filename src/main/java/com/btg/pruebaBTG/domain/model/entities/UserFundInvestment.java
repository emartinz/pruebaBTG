package com.btg.pruebaBTG.domain.model.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.btg.pruebaBTG.domain.model.enums.UserFundInvestmentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_fund_investments")
public class UserFundInvestment {
    @Id
    private String id;
    private String userId;
    private String fundId;
    private double investmentAmount;
    private UserFundInvestmentStatus status = UserFundInvestmentStatus.ACTIVE;
}
