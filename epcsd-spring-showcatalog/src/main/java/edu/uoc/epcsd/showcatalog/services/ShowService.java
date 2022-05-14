package edu.uoc.epcsd.showcatalog.services;

import edu.uoc.epcsd.showcatalog.dto.PerformanceDto;
import edu.uoc.epcsd.showcatalog.dto.ShowDto;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.exceptions.CategoryNotFoundException;
import edu.uoc.epcsd.showcatalog.exceptions.ShowNotFoundException;
import edu.uoc.epcsd.showcatalog.exceptions.ShowUpdateNotAllowedException;
import edu.uoc.epcsd.showcatalog.repositories.ShowRepository;
import edu.uoc.epcsd.showcatalog.vo.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Set;

@Service
public class ShowService {
    @Autowired
    private ShowRepository showRepository;
    @Autowired
    private CategoryService categoryService;

    public Optional<Show> findById(Long id) {
        Optional<Show> show = showRepository.findById(id);
        if (show.isPresent()) {
            return show;
        } else {
            throw new ShowNotFoundException(String.format("Show with id %d not found", id));
        }
    }

    public Iterable<Show> findByNameLike(String text) { return showRepository.findByNameLike(text); }

    public Iterable<Show> findByCategoryId(Long categoryId) {
        Optional<Category> category = categoryService.findById(categoryId);
        if (category.isPresent()) {
            return showRepository.findByCategory(category.get());
        } else {
            throw new CategoryNotFoundException(String.format("Category with id %d not found", categoryId));
        }
    }

    public Iterable<Performance> findPerformancesByShowId(Long showId) {
        Optional<Show> show = findById(showId);
        if (show.isPresent()) {
            return show.get().getPerformances();
        } else {
            throw new ShowNotFoundException(String.format("Show with id %d not found", showId));
        }
    }

    public Show save(Show show) { return showRepository.save(show); }

    public void delete(Long id) { showRepository.deleteById(id); }

    public Show createShow(ShowDto showDto) {
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
            this.save(newShow);
            categoryService.notifyNewShow(newShow);
            return newShow;
        } else {
            throw new CategoryNotFoundException(String.format("Category with id %d not found", showDto.getCategoryId()));
        }
    }

    public Boolean openShow(Long showId, String onSaleDate) {
        Optional<Show> show = this.findById(showId);
        if (show.isPresent()) {
            if (show.get().getStatus() == Show.Status.CREATED) {
                if (onSaleDate==null || onSaleDate.isEmpty() ) {
                    onSaleDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                }
                show.get().setStatus(Show.Status.OPENED);
                show.get().setOnSaleDate(onSaleDate);
                Set<Performance> performances = show.get().getPerformances();
                performances.forEach(performance -> {
                    performance.setStatus(Performance.Status.OPENED);
                });
                this.save(show.get());
                return true;
            } else {
                throw new ShowUpdateNotAllowedException(String.format("Show with id %d cannot be opened", showId));
            }
        } else {
            throw new ShowNotFoundException(String.format("Show with id %d not found", showId));
        }
    }

    public Boolean cancelShow(Long showId) {
        Optional<Show> show = this.findById(showId);
        if (show.isPresent()) {
            if (show.get().getStatus() == Show.Status.CREATED || show.get().getStatus() == Show.Status.OPENED) {
                show.get().setStatus(Show.Status.CANCELLED);
                Set<Performance> performances = show.get().getPerformances();
                performances.forEach(performance -> {
                    performance.setStatus(Performance.Status.CANCELLED);
                });
                this.save(show.get());
                return true;
            } else {
                throw new ShowUpdateNotAllowedException(String.format("Show with id %d cannot be cancelled", showId));
            }
        } else {
            throw new ShowNotFoundException(String.format("Show with id %d not found", showId));
        }
    }

    public Performance createPerformance(Long showId, PerformanceDto performanceDto) {
        Optional<Show> show = this.findById(showId);
        if (show.isPresent()) {
            Performance newPerformance = new Performance();
            newPerformance.setDate(performanceDto.getDate());
            newPerformance.setTime(performanceDto.getTime());
            newPerformance.setStreamingURL(performanceDto.getStreamingURL());
            newPerformance.setRemainingSeats(show.get().getCapacity());
            newPerformance.setStatus(Performance.Status.CREATED);
            show.get().addPerformance(newPerformance);
            this.save(show.get());
            return newPerformance;
        } else {
            throw new ShowNotFoundException(String.format("Show with id %d not found", showId));
        }
    }

    public Boolean deleteShowById(Long showId) {
        Optional<Show> show = this.findById(showId);
        if (show.isPresent()) {
            this.delete(showId);
            return true;
        } else {
            throw new ShowNotFoundException(String.format("Show with id %d not found", showId));
        }
    }
}
