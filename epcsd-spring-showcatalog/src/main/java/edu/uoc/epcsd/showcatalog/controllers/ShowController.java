package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dto.PerformanceDto;
import edu.uoc.epcsd.showcatalog.dto.ShowDto;
import edu.uoc.epcsd.showcatalog.dto.StatusDto;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.services.ShowService;
import edu.uoc.epcsd.showcatalog.vo.Performance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/show")
public class ShowController {
    @Autowired
    private ShowService showService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Show", description = "Creation of a new show by providing all its attributes. The category must exist in the database. The status will set to CREATED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show created successfully"),
            @ApiResponse(responseCode = "400", description = "Show creation bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Category provided not found", content = @Content)
    })
    public Long createShow(@RequestBody ShowDto showDto) {
        log.info("Creating show {}", showDto);
        Show newShow = showService.createShow(showDto);
        return newShow.getId();
    }

    @PutMapping("/{showId}/performance")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Add Performance to a show", description = "Addition of a new performance for the show with the provided id. The show must exist. The status will set to CREATED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Performance added successfully"),
            @ApiResponse(responseCode = "400", description = "Performance addition bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Show provided not found", content = @Content)
    })
    public Performance createPerformance(@PathVariable Long showId, @RequestBody PerformanceDto performanceDto) {
        log.info("Creating performance {} for show {}", performanceDto, showId);
        Performance newPerformance = showService.createPerformance(showId, performanceDto);
        return newPerformance;
    }

    @PatchMapping("/{showId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update Show Status", description = "According to the provided boolean value, the status of the show will be updated by opening or canceling the show. The show must exist. Performances will inherit the state of the show")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show updated successfully"),
            @ApiResponse(responseCode = "400", description = "Show update bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Show provided not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Show update not allowed", content = @Content)
    })
    public Boolean updateShowStatus(@PathVariable Long showId, @RequestBody StatusDto statusDto) {
        if ( statusDto.getOn() ) {
            log.info("Opening show {}", showId);
            return showService.openShow(showId, statusDto);
        } else {
            log.info("Canceling show {}", showId);
            return showService.cancelShow(showId);
        }
    }

    @DeleteMapping("/{showId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete Show", description = "Deletion of the show with the provided id. The show must exist. Related Performances will be deleted")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Show deletion bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Show provided not found", content = @Content)
    })
    public void deleteShow(@PathVariable Long showId) {
        log.info("Deleting show {}", showId);
        showService.deleteShowById(showId);
    }
}
