package xyz.dma.soft.api.validator.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.lesson.LessonAddRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.repository.CourseRepository;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class LessonAddRequestValidator extends ARequestValidator<LessonAddRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(LessonAddRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();

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