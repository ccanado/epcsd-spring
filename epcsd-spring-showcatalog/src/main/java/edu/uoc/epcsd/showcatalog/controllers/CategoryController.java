package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dto.CategoryDto;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.exceptions.CategoryAlreadyExistsException;
import edu.uoc.epcsd.showcatalog.services.CategoryService;
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
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create Category", description = "Creation of a new category by providing its name and description")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Category created successfully"),
        @ApiResponse(responseCode = "400", description = "Category creation bad request", content = @Content),
        @ApiResponse(responseCode = "409", description = "Category conflict by already existing name", content = @Content)
    })
    public Long createCategory(@RequestBody CategoryDto categoryDto) {
        log.info("Creating category: {}", categoryDto);
        Optional<Category> category = categoryService.findByName(categoryDto.getName());

        if (category.isPresent()) {
            throw new CategoryAlreadyExistsException(String.format("Category with name '%s' already exists", categoryDto.getName()));
        } else {
            Category newCategory = categoryService.createCategory(categoryDto);
            return newCategory.getId();
        }
    }
}
