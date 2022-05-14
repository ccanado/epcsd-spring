package edu.uoc.epcsd.showcatalog.repositories;

import edu.uoc.epcsd.showcatalog.entities.Category;
import edu.uoc.epcsd.showcatalog.entities.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {
    @Query("SELECT s.id, s.name FROM Show s WHERE s.name LIKE %:text%")
    Iterable<Show> findByNameLike(@Param("text")String text);

    //@Query(value = "SELECT * FROM Show s WHERE s.category_id=?1", nativeQuery = true)
    //@Query("SELECT s.id, s.name FROM Show s WHERE s.capacity=?1")
    //Iterable<Show> findByCategoryId(Long categoryId);
    @Query("SELECT s.id, s.name FROM Show s WHERE s.category=:category")
    Iterable<Show> findByCategory(@Param("category") Category category);
}
