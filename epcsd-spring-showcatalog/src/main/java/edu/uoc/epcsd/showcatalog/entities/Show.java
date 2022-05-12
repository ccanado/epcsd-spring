package edu.uoc.epcsd.showcatalog.entities;

import edu.uoc.epcsd.showcatalog.vo.Performance;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "image")
    private String image;

    @Column(name = "price")
    private Double price;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "onSaleDate")
    private String onSaleDate;

    @Column(name = "status")
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ElementCollection(targetClass = Performance.class)
    @JoinTable(name = "performance")
    @JoinColumn(name = "show_id", referencedColumnName = "id")
    private Set performances;

    public void addPerformance(Performance performance) {
        this.performances.add(performance);
    }

    public static enum Status {
        CREATED,
        OPENED,
        CANCELLED
    }
}
