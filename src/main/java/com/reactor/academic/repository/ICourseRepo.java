package com.reactor.academic.repository;

import com.reactor.academic.documment.Course;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICourseRepo extends ReactiveMongoRepository<Course, String> {
}
