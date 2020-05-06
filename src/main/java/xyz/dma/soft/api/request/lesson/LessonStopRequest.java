package xyz.dma.soft.api.request.lesson;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.dma.soft.api.request.StandardRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class LessonStopRequest extends StandardRequest {
    private Long lessonId;
    private String lessonEndDate;
}
