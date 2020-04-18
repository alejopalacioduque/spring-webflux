package com.reactor.academic.service.impl;

import com.reactor.academic.documment.Course;
import com.reactor.academic.repository.ICourseRepo;
import com.reactor.academic.service.ICourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CourseServiceImpl implements ICourseService {

    private ICourseRepo courseRepo;

    @Autowired
    public CourseServiceImpl(ICourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    @Override
    public Mono<Course> register(Course t) { return courseRepo.save(t); }

    @Override
    public Mono<Course> modify(Course t) {
        return courseRepo.save(t);
    }

    @Override
    public Flux<Course> list() {
        return courseRepo.findAll();
    }

    @Override
    public Mono<Course> listById(String v) {
        return courseRepo.findById(v);
    }

    @Override
    public Mono<Void> delete(String v) {
        return courseRepo.deleteById(v);
    }

}
