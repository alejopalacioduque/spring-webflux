package com.reactor.academic.service.impl;

import com.reactor.academic.documment.Student;
import com.reactor.academic.repository.IStudentRepo;
import com.reactor.academic.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class StudentServiceImpl implements IStudentService {

    private IStudentRepo studentRepo;

    @Autowired
    public StudentServiceImpl(IStudentRepo studentRepo) {
        this.studentRepo = studentRepo;
    }

    @Override
    public Mono<Student> register(Student t) { return studentRepo.save(t); }

    @Override
    public Mono<Student> modify(Student t) {
        return studentRepo.save(t);
    }

    @Override
    public Flux<Student> list() {
        return studentRepo.findAll();
    }

    @Override
    public Mono<Student> listById(String v) {
        return studentRepo.findById(v);
    }

    @Override
    public Mono<Void> delete(String v) {
        return studentRepo.deleteById(v);
    }
}
