package edu.uoc.epcsd.notification.controllers;

import edu.uoc.epcsd.notification.pojos.User;
import edu.uoc.epcsd.notification.services.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@Log4j2
@RestController
@RequestMapping("/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping("/show/{showId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Notification Show Id", description = "Notification users likes category of a specific show")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users notifies successfully",
                    content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = User.class))) }),
            @ApiResponse(responseCode = "404", description = "Show provided not found", content = @Content)
    })
    public Iterable<User> getNotifications(@PathVariable("showId") Long showId) {
        log.info("Notification users likes category of show with id: {}", showId);
        return notificationService.notifyShowById(showId);
    }
}
