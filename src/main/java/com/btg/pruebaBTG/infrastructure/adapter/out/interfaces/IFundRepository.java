package com.btg.pruebaBTG.infrastructure.adapter.out.interfaces;

import java.util.Optional;

import com.btg.pruebaBTG.domain.model.entities.Fund;

public interface IFundRepository {
    Optional<Fund> findById(String id);
}