package xyz.dma.soft.api.validator.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.lesson.LessonStopRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.LessonRepository;

@Component
@AllArgsConstructor
public class LessonStopRequestValidator extends ARequestValidator<LessonStopRequest> {
    private final LessonRepository lessonRepository;

    @Override
    public IConstraintContext validate(LessonStopRequest request) {
        ConstraintContextBuilder context = new ConstraintContextBuilder()

                .assertConstraintViolation(0, isNull(request.getLessonId()), ConstraintType.EMPTY, "lessonId")
                .assertConstraintViolation(1, () -> !lessonRepository.existsById(request.getLessonId()), ConstraintType.INVALID, "lessonId");

        if(request.getLessonEndDate() != null && !request.getLessonEndDate().isEmpty()) {
                context.assertConstraintViolation(
                        not(dateStartBeforeEnd(lessonRepository.findFirstById(request.getLessonId()).getLessonStartDate(),
                                request.getLessonEndDate())), ConstraintType.INVALID, "lessonEndDate");
        }

        return context.build();
    }
}