package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dto.PerformanceDto;
import edu.uoc.epcsd.showcatalog.dto.ShowDto;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.exceptions.CategoryNotFoundException;
import edu.uoc.epcsd.showcatalog.exceptions.ShowNotFoundException;
import edu.uoc.epcsd.showcatalog.services.CategoryService;
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

import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/show")
public class ShowController {
    @Autowired
    private ShowService showService;
    @Autowired
    private CategoryService categoryService;

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
        Optional<Category> category = categoryService.findById(showDto.getCategoryId());

        if (category.isPresent()) {
            Show newShow = showService.createShow(showDto, category.get());
            categoryService.notifyNewShow(newShow);
            return newShow.getId();
        } else {
            throw new CategoryNotFoundException(String.format("Category with id %d not found", showDto.getCategoryId()));
        }
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
        Optional<Show> show = showService.findById(showId);

        if (show.isPresent()) {
            Performance newPerformance = showService.createPerformance(show.get(), performanceDto);
            return newPerformance;
        } else {
            throw new ShowNotFoundException(String.format("Show with id %d not found", showId));
        }
    }
}
