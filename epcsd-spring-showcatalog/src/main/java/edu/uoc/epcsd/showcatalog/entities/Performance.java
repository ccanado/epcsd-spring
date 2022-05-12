package edu.uoc.epcsd.showcatalog.entities;

import lombok.*;
import lombok.extern.apachecommons.CommonsLog;

import javax.persistence.*;

@Entity
@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="showId")
    private Long showId;

    @Column(name="date")
    private String date;

    @Column(name="isPublic")
    private Boolean isPublic;

    @Column(name="capacity")
    private Integer capacity;



}
