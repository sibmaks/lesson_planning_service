package xyz.dma.soft.api.request.lesson;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.dma.soft.api.entity.LessonEntity;
import xyz.dma.soft.api.request.StandardRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class LessonAddRequest extends StandardRequest {
    private LessonEntity lessonEntity;
}
