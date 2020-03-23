package com.reator.academic.repository;

import com.reator.academic.documment.Student;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IStudentRepo extends ReactiveMongoRepository<Student, String> {
}
