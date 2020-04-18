package com.reactor.academic.repository;

import com.reactor.academic.documment.Rol;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IRolRepo extends ReactiveMongoRepository<Rol, String> {

}
