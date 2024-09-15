package com.openclassrooms.model;

import java.time.LocalDateTime;

import com.openclassrooms.entity.Rental;

import lombok.Data;

@Data
public class RentalModel {

    private Integer id;
    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;
    private Integer owner_id; // Add owner ID field
    private LocalDateTime created_at; // Add created_at field
    private LocalDateTime updated_at; // Add updated_at field

    // Constructor to map Rental entity to DTO
    public RentalModel(Rental rental) {
        this.id = rental.getId();
        this.name = rental.getName();
        this.surface = rental.getSurface();
        this.price = rental.getPrice();
        this.picture = rental.getPicture();
        this.description = rental.getDescription();
        this.owner_id = rental.getOwner_id(); // Map owner_id
        this.created_at = rental.getCreated_at(); // Map created_at
        this.updated_at = rental.getUpdated_at(); // Map updated_at
    }

    // Getters and setters

}
