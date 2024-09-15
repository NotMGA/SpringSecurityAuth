package com.openclassrooms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MessageModel {

    private String message;

    @JsonProperty("user_id") // Map "user_id" from the JSON to "userId"
    private Integer userId;

    @JsonProperty("rental_id") // Map "rental_id" from the JSON to "rentalId"
    private Integer rentalId;

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRentalId() {
        return rentalId;
    }

    public void setRentalId(Integer rentalId) {
        this.rentalId = rentalId;
    }
}