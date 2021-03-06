package xyz.dma.soft.api.request.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.request.StandardRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CourseAddRequest extends StandardRequest {
    private String name;
}
