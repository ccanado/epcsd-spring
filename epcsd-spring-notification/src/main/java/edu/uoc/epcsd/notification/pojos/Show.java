package edu.uoc.epcsd.notification.pojos;

import lombok.*;

import java.util.List;

@ToString(exclude = "performances")
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    private Long id;

    private String name;

    private String description;

    private String image;

    private Double price;

    private Integer duration;

    private Integer capacity;

    private String onSaleDate;

    private Status status;

    private Category category;

    private List<Performance> performances;

    public static enum Status {
        CREATED,
        OPENED,
        CANCELLED
    }
}
