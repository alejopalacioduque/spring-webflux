package com.reator.academic.repository;

import com.reator.academic.documment.Course;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ICourseRepo extends ReactiveMongoRepository<Course, String> {
}
