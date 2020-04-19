package com.reactor.academic.routes;


import com.reactor.academic.handler.CourseHandler;
import com.reactor.academic.handler.EnrollmentHandler;
import com.reactor.academic.handler.StudentHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routesCourses(CourseHandler handler) {
        return route(GET("/courses"), handler::list)
                .andRoute(GET("/courses/listById"), handler::listById)
                .andRoute(POST("/courses"), handler::register)
                .andRoute(PUT("/courses"), handler::modify)
                .andRoute(DELETE("/courses"), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> routesStudents(StudentHandler handler) {
        return route(GET("/students"), handler::list)
                .andRoute(GET("/students/sortedByAgeParallel"), handler::listParallel)
                .andRoute(GET("/students/listById"), handler::listById)
                .andRoute(POST("/students"), handler::register)
                .andRoute(PUT("/students"), handler::modify)
                .andRoute(DELETE("/students"), handler::delete);
    }

    @Bean
    public RouterFunction<ServerResponse> routesEnrollments(EnrollmentHandler handler) {
        return route(GET("/enrollments"), handler::list)
                .andRoute(POST("/enrollments"), handler::registrar);
    }
}

