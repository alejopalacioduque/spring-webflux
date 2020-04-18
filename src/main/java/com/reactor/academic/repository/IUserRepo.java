package com.reactor.academic.repository;

import com.reactor.academic.documment.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface IUserRepo extends ReactiveMongoRepository<User, String> {

    Mono<User> findOneByUserId(String userId);
}
