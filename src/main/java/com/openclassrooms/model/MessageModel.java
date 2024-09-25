package com.openclassrooms.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class MessageModel {

    private String message;
    private LocalDateTime created_at;
    @JsonProperty("updated_at")
    private LocalDateTime updated_at;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("rental_id")
    private Integer rentalId;

    // Getters and Setters

}