package com.reator.academic.repository;

import com.reator.academic.documment.Enrollment;
import com.reator.academic.service.ICRUD;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IEnrollmentRepo extends ReactiveMongoRepository<Enrollment, String> {
}
