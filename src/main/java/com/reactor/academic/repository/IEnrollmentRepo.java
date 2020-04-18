package com.reactor.academic.repository;

import com.reactor.academic.documment.Enrollment;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IEnrollmentRepo extends ReactiveMongoRepository<Enrollment, String> {
}
