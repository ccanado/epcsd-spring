package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dto.PerformanceDto;
import edu.uoc.epcsd.showcatalog.dto.ShowDto;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.services.ShowService;
import edu.uoc.epcsd.showcatalog.vo.Performance;
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
            @ApiResponse(responseCode = "200", description = "Performance added successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Performance.class)) }),
            @ApiResponse(responseCode = "400", description = "Performance addition bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Show provided not found", content = @Content)
    })
    public Performance createPerformance(@PathVariable Long showId, @RequestBody PerformanceDto performanceDto) {
        log.info("Creating performance {} for show {}", performanceDto, showId);
        Performance newPerformance = showService.createPerformance(showId, performanceDto);
        return newPerformance;
    }

    @PatchMapping("/{showId}/open")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Open Show", description = "Open the show with the provided id. The show must exist. The status will set to OPENED if it is CREATED. The onSaleDate will set to the current date or informed date. Performances will inherit the state of the show")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show updated successfully"),
            @ApiResponse(responseCode = "400", description = "Show update bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Show provided not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Show update not allowed", content = @Content)
    })
    public Boolean openShow(@PathVariable Long showId, @RequestBody (required = false) String onSaleDate) {
        log.info("Opening show {}", showId);
        return showService.openShow(showId, onSaleDate);
    }

    @PatchMapping("/{showId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Cancel Show", description = "The status of the show will be updated to CANCELED if it is CREATED or OPENED. The show must exist. Performances will inherit the state of the show")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show updated successfully"),
            @ApiResponse(responseCode = "400", description = "Show update bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Show provided not found", content = @Content),
            @ApiResponse(responseCode = "405", description = "Show update not allowed", content = @Content)
    })
    public Boolean cancelShow(@PathVariable Long showId) {
        log.info("Canceling show {}", showId);
        return showService.cancelShow(showId);
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

    @GetMapping("/findByName/{name}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find Shows by Name", description = "Find shows containing in their name the provided string")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shows found successfully",
                    content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Show.class))) }),
            @ApiResponse(responseCode = "404", description = "Show provided not found", content = @Content)
    })
    public Iterable<Show> findShowsByName(@PathVariable String name) {
        log.info("Searching shows containing '{}' in their name", name);
        return showService.findByNameLike(name);
    }

    @GetMapping("/findByCategory/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find Shows by Category", description = "Find shows by the provided category id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Shows found successfully",
                    content = { @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Show.class))) }),
            @ApiResponse(responseCode = "404", description = "Show provided not found", content = @Content)
    })
    public Iterable<Show> findShowsByCategory(@PathVariable Long categoryId) {
        log.info("Searching shows by category {}", categoryId);
        return showService.findByCategoryId(categoryId);
    }

    @GetMapping("/{showId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find Show by Id", description = "Find show by the provided id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Show found successfully",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Show.class)) }),
            @ApiResponse(responseCode = "404", description = "Show provided not found", content = @Content)
    })
    public Show findShowById(@PathVariable Long showId) {
        log.info("Searching show {}", showId);
        return showService.findById(showId).get();
    }
}
