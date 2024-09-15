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

}