package edu.uoc.epcsd.showcatalog.services;

import edu.uoc.epcsd.showcatalog.dto.CategoryDto;
import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Show;
import edu.uoc.epcsd.showcatalog.exceptions.CategoryAlreadyExistsException;
import edu.uoc.epcsd.showcatalog.exceptions.CategoryDeleteNotAllowedException;
import edu.uoc.epcsd.showcatalog.exceptions.CategoryNotFoundException;
import edu.uoc.epcsd.showcatalog.repositories.CategoryRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private KafkaTemplate<String, Show> kafkaTemplate;

    public Iterable<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    public Optional<Category> findByName(String name) { return categoryRepository.findByName(name); }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    public void notifyNewShow(Show show) {
        log.info("Notifying new show '{}' by pub/sub topic 'show.add'", show.getName());
        kafkaTemplate.send("shows.add", show);
    }

    public Category createCategory(CategoryDto categoryDto) {
        Optional<Category> category = this.findByName(categoryDto.getName());
        if (category.isPresent()) {
            throw new CategoryAlreadyExistsException(String.format("Category with name '%s' already exists", categoryDto.getName()));
        } else {
            Category newCategory = new Category();
            newCategory.setName(categoryDto.getName());
            newCategory.setDescription(categoryDto.getDescription());
            this.save(newCategory);
            return newCategory;
        }
    }

    public Boolean deleteCategoryById(Long categoryId) {
        Optional<Category> category = this.findById(categoryId);
        if (category.isPresent()) {
            if (category.get().getShows().isEmpty()) {
                this.delete(categoryId);
                return true;
            } else {
                throw new CategoryDeleteNotAllowedException(String.format("Category with id '%s' cannot be deleted because it has related shows", categoryId));
            }
        } else {
            throw new CategoryNotFoundException(String.format("Category with id %d not found", categoryId));
        }
    }
}
