package xyz.dma.soft.api.validator.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.lesson.LessonAddRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.CourseRepository;

@Component
@AllArgsConstructor
public class LessonAddRequestValidator extends ARequestValidator<LessonAddRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(LessonAddRequest request) {
        ConstraintContextBuilder context = new ConstraintContextBuilder()
                .assertConstraintViolation(0, isNull(request.getCourseId()), ConstraintType.EMPTY, "courseId")
                .assertConstraintViolation(1, () -> !courseRepository.existsById(request.getCourseId()), ConstraintType.INVALID, "courseId")

                .assertConstraintViolation(0, isNull(request.getLessonStartDate()), ConstraintType.EMPTY, "lessonStartDate")
                .assertConstraintViolation(1, not(isValidDate(request.getLessonStartDate())), ConstraintType.INVALID, "lessonStartDate")

                .assertConstraintViolation(0, isNull(request.getTimeStart()), ConstraintType.EMPTY, "timeStart")
                .assertConstraintViolation(1, not(isValidTime(request.getTimeStart())), ConstraintType.INVALID, "timeStart")
                .assertConstraintViolation(0, isNull(request.getTimeEnd()), ConstraintType.EMPTY, "timeEnd")
                .assertConstraintViolation(1, not(isValidTime(request.getTimeEnd())), ConstraintType.INVALID, "timeEnd")
                .assertConstraintViolation(2, not(timeStartBeforeEnd(request.getTimeStart(), request.getTimeEnd())), ConstraintType.INVALID, "timeStart")

                .assertConstraintViolation(not(inRange(request.getDayOfWeek(), 1, 7)), ConstraintType.INVALID, "dayOfWeek");

        return context.build();
    }
}