package com.reactor.academic.repository;

import com.reactor.academic.documment.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IStudentRepo extends ReactiveMongoRepository<Student, String> {
}
