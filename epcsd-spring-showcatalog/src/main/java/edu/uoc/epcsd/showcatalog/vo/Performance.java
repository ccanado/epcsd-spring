package edu.uoc.epcsd.showcatalog.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
public class Performance {

    @Column(name="date")
    private String date;

    @Column(name="time")
    private String time;

    @Column(name="streamingURL")
    private String streamingURL;

    @Column(name="remainingSeats")
    private Integer remainingSeats;

    @Column(name="status")
    private Status status;

    public static enum Status {
        CREATED
    }
}
