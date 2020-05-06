package xyz.dma.soft.api.validator.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.lesson.LessonUpdateRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.repository.CourseRepository;
import xyz.dma.soft.repository.LessonRepository;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class LessonUpdateRequestValidator extends ARequestValidator<LessonUpdateRequest> {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @Override
    public IConstraintContext validate(LessonUpdateRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getId()), "empty", "id")
                .addConstraint(1, () -> !lessonRepository.existsById(request.getId()), "invalid", "id");

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getCourseId()), "empty", "courseId")
                .addConstraint(1, () -> !courseRepository.existsById(request.getCourseId()), "invalid", "courseId");

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getLessonStartDate()), "empty", "lessonStartDate")
                .addConstraint(1, () -> !isValidDate(request.getLessonStartDate()), "invalid", "lessonStartDate");

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getTimeStart()), "empty", "timeStart")
                .addConstraint(1, () -> !isValidTime(request.getTimeStart()), "invalid", "timeStart")
                .addConstraint(0, () -> isNull(request.getTimeEnd()), "empty", "timeEnd")
                .addConstraint(1, () -> !isValidTime(request.getTimeEnd()), "invalid", "timeEnd")
                .addConstraint(2, () -> !timeStartBeforeEnd(request.getTimeStart(), request.getTimeEnd()), "invalid", "timeStart");

        addConstraint(context, !inRange(request.getDayOfWeek(), 1, 7), "invalid", "dayOfWeek");

        return context;
    }
}