package xyz.dma.soft.api.response.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.CourseInfo;
import xyz.dma.soft.api.response.StandardResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateCourseResponse extends StandardResponse {
    private CourseInfo courseInfo;
}
