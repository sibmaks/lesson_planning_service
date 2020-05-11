package xyz.dma.soft.api.validator.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.lesson.LessonAddRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.entity.ConstraintType;
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
                .addConstraint(0, () -> isNull(request.getCourseId()), ConstraintType.EMPTY, "courseId")
                .addConstraint(1, () -> !courseRepository.existsById(request.getCourseId()), ConstraintType.INVALID, "courseId");

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getLessonStartDate()), ConstraintType.EMPTY, "lessonStartDate")
                .addConstraint(1, () -> !isValidDate(request.getLessonStartDate()), ConstraintType.INVALID, "lessonStartDate");

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getTimeStart()), ConstraintType.EMPTY, "timeStart")
                .addConstraint(1, () -> !isValidTime(request.getTimeStart()), ConstraintType.INVALID, "timeStart")
                .addConstraint(0, () -> isNull(request.getTimeEnd()), ConstraintType.EMPTY, "timeEnd")
                .addConstraint(1, () -> !isValidTime(request.getTimeEnd()), ConstraintType.INVALID, "timeEnd")
                .addConstraint(2, () -> !timeStartBeforeEnd(request.getTimeStart(), request.getTimeEnd()), ConstraintType.INVALID, "timeStart");

        addConstraint(context, !inRange(request.getDayOfWeek(), 1, 7), ConstraintType.INVALID, "dayOfWeek");

        return context;
    }
}