package edu.uoc.epcsd.showcatalog.services;

import edu.uoc.epcsd.showcatalog.dto.PerformanceDto;
import edu.uoc.epcsd.showcatalog.dto.ShowDto;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import edu.uoc.epcsd.showcatalog.vo.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShowService {
    @Autowired
    private ShowRepository showRepository;

    public Iterable<Show> findAll() {
        return showRepository.findAll();
    }

    public Optional<Show> findById(Long id) {
        return showRepository.findById(id);
    }

    public Show save(Show show) { return showRepository.save(show); }

    public void delete(Long id) { showRepository.deleteById(id); }

    public Show createShow(ShowDto showDto, Category category) {
        Show newShow = new Show();
        newShow.setCategory(category);
        newShow.setName(showDto.getName());
        newShow.setDescription(showDto.getDescription());
        newShow.setImage(showDto.getImage());
        newShow.setPrice(showDto.getPrice());
        newShow.setDuration(showDto.getDuration());
        newShow.setCapacity(showDto.getCapacity());
        newShow.setStatus(Show.Status.CREATED);
        this.save(newShow);
        return newShow;
    }

    public Boolean openShow(Show show, String onSaleDate) {
        show.setStatus(Show.Status.OPENED);
        show.setOnSaleDate(onSaleDate);
        this.save(show);
        return true;
    }

    public Boolean cancelShow(Show show) {
        show.setStatus(Show.Status.CANCELLED);
        this.save(show);
        return true;
    }

    public Performance createPerformance(Show show, PerformanceDto performanceDto) {
        Performance newPerformance = new Performance();
        newPerformance.setDate(performanceDto.getDate());
        newPerformance.setTime(performanceDto.getTime());
        newPerformance.setStreamingURL(performanceDto.getStreamingURL());
        newPerformance.setRemainingSeats(show.getCapacity());
        newPerformance.setStatus(Performance.Status.CREATED);
        show.addPerformance(newPerformance);
        this.save(show);
        return newPerformance;
    }
}
