package com.project.learnreactiveprogramming.controller;

import com.project.learnreactiveprogramming.ApiResponse;
import com.project.learnreactiveprogramming.model.Student;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

//@RestController denotes that the functions in this class are going to be Rest APIs
@RestController
public class DatabaseController {

    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @PostMapping("/student")
    public Mono<ResponseEntity<ApiResponse<Void>>> createStudent(@Valid @RequestBody Student student) {
        //save student to database
        if (student.getEnrollmentId() == null || student.getEnrollmentId().isBlank()) {
            student.setEnrollmentId("MAGIC" + UUID.randomUUID().toString().replace("-", "").substring(0, 4));
        }

        return reactiveMongoTemplate.exists(Query.query(Criteria.where("enrollmentId").is(student.getEnrollmentId())), Student.class)
                .flatMap(isExist -> {
            if (isExist) {
                return Mono.just(ResponseEntity.badRequest()
                        .body(new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Invalid request payload", null)));
            }

            return reactiveMongoTemplate.save(student).
                    thenReturn(ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "", null)));
        });
    }

    @GetMapping("/student")
    public Mono<ResponseEntity<ApiResponse<Student>>> getStudentByEnrollmentId(@RequestParam String enrollmentId) {
        Criteria criteria = Criteria.where("enrollmentId").is(enrollmentId);
        Query query = Query.query(criteria);
        return reactiveMongoTemplate.findOne(query, Student.class)
                .map(student -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ApiResponse<>(HttpStatus.OK.value(), "Success Response", student))
                ).switchIfEmpty(Mono.just(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null))
                ));
    }
}
