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
    private Integer owner_id;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;

    public RentalModel(Rental rental) {
        this.id = rental.getId();
        this.name = rental.getName();
        this.surface = rental.getSurface();
        this.price = rental.getPrice();
        this.picture = rental.getPicture();
        this.description = rental.getDescription();
        this.owner_id = rental.getOwner_id();
        this.created_at = rental.getCreated_at();
        this.updated_at = rental.getUpdated_at();
    }

    // Getters and setters

}
