package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.dto.ShowDto;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import edu.uoc.epcsd.showcatalog.services.CategoryService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/show")
public class ShowController {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Long createShow(@RequestBody ShowDto showDto) {
        log.info("Creating show {}", showDto);
        Optional<Category> category = categoryService.findById(showDto.getCategoryId());

        if (category.isPresent()) {
            Show newShow = new Show();
            newShow.setCategory(category.get());
            newShow.setName(showDto.getName());
            newShow.setDescription(showDto.getDescription());
            newShow.setImage(showDto.getImage());
            newShow.setPrice(showDto.getPrice());
            newShow.setDuration(showDto.getDuration());
            newShow.setCapacity(showDto.getCapacity());
            newShow.setStatus(Show.Status.CREATED);
            showRepository.save(newShow);
            categoryService.notifyNewShow(newShow);
            return newShow.getId();
        } else {
            log.error("Category {} not found", showDto.getCategoryId());
            throw new IllegalArgumentException("Category not found");
        }
    }
}
