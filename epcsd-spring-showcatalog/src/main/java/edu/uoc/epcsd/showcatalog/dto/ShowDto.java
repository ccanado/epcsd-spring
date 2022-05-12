package edu.uoc.epcsd.showcatalog.dto;

import lombok.Data;

@Data
public class ShowDto {
    private Long categoryId;
    private String name;
    private String description;
    private String image;
    private double price;
    private int capacity;
    private int duration;
}
