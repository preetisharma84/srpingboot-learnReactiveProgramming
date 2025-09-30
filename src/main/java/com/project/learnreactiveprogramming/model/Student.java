package com.project.learnreactiveprogramming.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document
public class Student {

    @Id
    @JsonIgnore
    private String id;

    @Pattern(regexp = "^MAGIC.*$", message = "EnrollmentId must start with MAGIC")
    private String enrollmentId;

    @Valid
    @NotNull(message = "Address cannot be null")
    private Address address;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Date of birth cannot be blank")
    private String dob;

    @NotBlank(message = "Branch cannot be blank")
    private String branch;

    public Student() {}

    public Student(Address address, String name, String dob, String branch) {
        this.enrollmentId = UUID.randomUUID().toString();        //Ex. MAGIC0001
        this.address = address;
        this.name = name;
        this.dob = dob;
        this.branch = branch;
    }

    public String getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(String enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", enrollmentId='" + enrollmentId + '\'' +
                ", address=" + address +
                ", name='" + name + '\'' +
                ", dob='" + dob + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}