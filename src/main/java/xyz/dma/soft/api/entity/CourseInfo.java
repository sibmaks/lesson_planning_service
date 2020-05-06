package xyz.dma.soft.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.domain.Course;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseInfo implements Serializable {
    private Long id;
    private String name;
    private UserInfoEntity author;

    public CourseInfo(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.author = new UserInfoEntity(course.getAuthor());
    }
}
