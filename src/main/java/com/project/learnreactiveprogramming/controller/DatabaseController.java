package com.project.learnreactiveprogramming.controller;

import com.project.learnreactiveprogramming.ApiResponse;
import com.project.learnreactiveprogramming.model.Address;
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
        Query query = Query.query(Criteria.where("enrollmentId").is(enrollmentId));
        return reactiveMongoTemplate.findOne(query, Student.class).log()
                .map(student -> ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new ApiResponse<>(HttpStatus.OK.value(), "Success Response", student))
                ).switchIfEmpty(Mono.just(ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null))
                ));
    }

    @DeleteMapping("/student/{enrollmentId}")
    public Mono<ResponseEntity<ApiResponse<Object>>> deleteStudentByEnrollmentId(@PathVariable String enrollmentId) {
        Query query = Query.query(Criteria.where("enrollmentId").is(enrollmentId));
        return reactiveMongoTemplate.exists(query, Student.class)
                .flatMap(isExist-> {
                    if (!isExist) {
                        return Mono.just(ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No record found", null)));
                    }
                    return reactiveMongoTemplate.remove(query, Student.class)
                            .thenReturn(ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Record deleted successfully", null)));
                });
    }

    @PatchMapping("/student/{enrollmentId}")
    public Mono<ResponseEntity<ApiResponse<Object>>> updateStudentByEnrollmentId(@PathVariable String enrollmentId, @RequestBody Student updateStudent) {
        Query query = Query.query(Criteria.where("enrollmentId").is(enrollmentId));
        return reactiveMongoTemplate.exists(query, Student.class)
                .flatMap(isExist-> {
                    if (!isExist) {
                        return Mono.just(ResponseEntity
                                .status(HttpStatus.NOT_FOUND)
                                .body(new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No student found", null)));
                    }

                    return reactiveMongoTemplate.findOne(query, Student.class)
                            .flatMap(existingStudent -> {
                                boolean isUpdated = false;
                                if (updateStudent.getName() != null) {
                                    existingStudent.setName(updateStudent.getName());
                                    isUpdated = true;
                                }
                                if (updateStudent.getDob() != null) {
                                    existingStudent.setDob(updateStudent.getDob());
                                    isUpdated = true;
                                }
                                if (updateStudent.getBranch() != null) {
                                    existingStudent.setBranch(updateStudent.getBranch());
                                    isUpdated = true;
                                }
                                if (updateStudent.getAddress() != null) {
                                    Address updatedAddress = getAddress(updateStudent, existingStudent);
                                    existingStudent.setAddress(updatedAddress);
                                    isUpdated = true;
                                }

                                if (!isUpdated) {
                                    return Mono.just(ResponseEntity
                                            .status(HttpStatus.NO_CONTENT)
                                            .body(new ApiResponse<>(HttpStatus.NO_CONTENT.value(), "No content found", null)));
                                }


                                return reactiveMongoTemplate.save(existingStudent)
                                        .thenReturn(ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Record updated successfully", null)));

                            });
                });
    }

    private Address getAddress(Student updateStudent, Student existingStudent) {
        Address existingAddress = existingStudent.getAddress();
        Address updatedAddress = updateStudent.getAddress();

        if (updatedAddress.getCity() != null) {
            existingAddress.setCity(updatedAddress.getCity());
        }
        if (updatedAddress.getState() != null) {
            existingAddress.setState(updatedAddress.getState());
        }
        if (updatedAddress.getPincode() != null) {
            existingAddress.setPincode(updatedAddress.getPincode());
        }
        return existingAddress;
    }


}