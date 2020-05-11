package xyz.dma.soft.api.validator.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.lesson.LessonStopRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.LessonRepository;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class LessonStopRequestValidator extends ARequestValidator<LessonStopRequest> {
    private final LessonRepository lessonRepository;

    @Override
    public IConstraintContext validate(LessonStopRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getLessonId()), ConstraintType.EMPTY, "lessonId")
                .addConstraint(1, () -> !lessonRepository.existsById(request.getLessonId()), ConstraintType.INVALID, "lessonId");

        if(request.getLessonEndDate() != null && !request.getLessonEndDate().isEmpty()) {
                addConstraint(context,
                        !dateStartBeforeEnd(lessonRepository.findFirstById(request.getLessonId()).getLessonStartDate(),
                                request.getLessonEndDate()), ConstraintType.INVALID, "lessonEndDate");
        }

        return context;
    }
}