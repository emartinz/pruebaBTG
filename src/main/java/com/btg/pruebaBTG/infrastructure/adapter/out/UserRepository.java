package com.btg.pruebaBTG.infrastructure.adapter.out;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.btg.pruebaBTG.domain.model.entities.User;
import com.btg.pruebaBTG.infrastructure.adapter.out.interfaces.IUserRepository;

@Repository
public interface UserRepository extends MongoRepository<User, String>, IUserRepository {
    
}
