package xyz.dma.soft.api.response.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.LessonEntity;
import xyz.dma.soft.api.response.StandardResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LessonUpdateResponse extends StandardResponse {
    private LessonEntity lessonEntity;
}
