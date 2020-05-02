package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.Course;

public interface CourseRepository extends CrudRepository<Course, Long> {
    Course findFirstByName(String name);
}
