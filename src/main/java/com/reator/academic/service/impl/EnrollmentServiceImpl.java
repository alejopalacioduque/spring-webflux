package com.reator.academic.service.impl;

import com.reator.academic.documment.Enrollment;
import com.reator.academic.repository.IEnrollmentRepo;
import com.reator.academic.service.IEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EnrollmentServiceImpl implements IEnrollmentService {

    private IEnrollmentRepo enrollmentRepo;

    @Autowired
    public EnrollmentServiceImpl(IEnrollmentRepo enrollmentRepo) {
        this.enrollmentRepo = enrollmentRepo;
    }

    @Override
    public Mono<Enrollment> register(Enrollment enrollment) {
        return enrollmentRepo.save(enrollment);
    }

    @Override
    public Mono<Enrollment> modify(Enrollment enrollment) {
        return enrollmentRepo.save(enrollment);
    }

    @Override
    public Flux<Enrollment> list() {
        return enrollmentRepo.findAll();
    }

    @Override
    public Mono<Enrollment> listById(String id) {
        return enrollmentRepo.findById(id);
    }

    @Override
    public Mono<Void> delete(String s) {
        return enrollmentRepo.deleteById(s);
    }
}
