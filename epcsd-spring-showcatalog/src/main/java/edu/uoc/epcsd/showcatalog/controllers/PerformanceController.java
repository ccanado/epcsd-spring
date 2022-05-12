package edu.uoc.epcsd.showcatalog.controllers;

import edu.uoc.epcsd.showcatalog.entities.Performance;
import edu.uoc.epcsd.showcatalog.repositories.PerformanceRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/performance")
public class PerformanceController {

    @Autowired
    private PerformanceRepository performanceRepository;

    // Create new performance
    @RequestMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public Long createPerformance(Performance performance) {
        log.info("Creating performance: " + performance);
        Performance newPerformance = performanceRepository.save(performance);
        return newPerformance.getId();
    }

    // delete performance
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePerformance(@PathVariable Long id) {
        log.info("Deleting performance: " + id);
        performanceRepository.deleteById(id);
    }
}
