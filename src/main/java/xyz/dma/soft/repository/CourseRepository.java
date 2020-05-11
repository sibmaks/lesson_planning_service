package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.Course;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Long> {
    Course findFirstByName(String name);

    boolean existsById(Long id);

    Course findFirstById(Long id);

    List<Course> findAllByOrderById();
}
