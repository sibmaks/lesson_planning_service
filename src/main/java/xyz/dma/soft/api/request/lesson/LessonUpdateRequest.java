package xyz.dma.soft.api.request.lesson;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.dma.soft.api.entity.LessonEntity;
import xyz.dma.soft.api.request.StandardRequest;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class LessonUpdateRequest extends StandardRequest {
    private Long id;
    private LessonEntity lessonEntity;
    private Long courseId;
    private int dayOfWeek;
    private String timeStart;
    private String timeEnd;
    private String lessonStartDate;
    private List<Long> children;
}
