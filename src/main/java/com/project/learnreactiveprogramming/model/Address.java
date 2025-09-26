package com.project.learnreactiveprogramming.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Address {

    @NotNull
    @NotBlank(message = "City cannot be blank")
    private String city;
    @NotNull
    @NotBlank(message = "State cannot be blank")
    private String state;
    @NotNull
    @NotBlank(message = "Pincode cannot be blank")
    private String pincode;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
