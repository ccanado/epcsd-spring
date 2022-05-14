package edu.uoc.epcsd.notification.pojos;

import lombok.*;

@ToString
@Getter
@Setter
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Performance {

    private String date;

    private String time;

    private String streamingURL;

    private Integer remainingSeats;

    private Status status;

    public static enum Status {
        CREATED,
        OPENED,
        CANCELLED
    }
}
