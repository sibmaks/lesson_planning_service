package xyz.dma.soft.api.validator.scheduling;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.scheduling.SchedulingGetRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.CourseRepository;

@Component
@AllArgsConstructor
public class SchedulingGetRequestValidator extends ARequestValidator<SchedulingGetRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(SchedulingGetRequest request) {
        ConstraintContextBuilder context = new ConstraintContextBuilder()

                .assertConstraintViolation(0, isNull(request.getCourseId()), ConstraintType.EMPTY, "courseId")
                .assertConstraintViolation(1, () -> !courseRepository.existsById(request.getCourseId()), ConstraintType.INVALID, "courseId")

                .assertConstraintViolation(0, isNull(request.getFromDate()), ConstraintType.EMPTY, "fromDate")
                .assertConstraintViolation(1, not(isValidDate(request.getFromDate())), ConstraintType.INVALID, "fromDate")
                .assertConstraintViolation(0, isNull(request.getToDate()), ConstraintType.EMPTY, "toDate")
                .assertConstraintViolation(1, not(isValidDate(request.getToDate())), ConstraintType.INVALID, "toDate")
                .assertConstraintViolation(2, not(dateStartBeforeEnd(request.getFromDate(), request.getToDate())), ConstraintType.INVALID, "fromDate");

        return context.build();
    }
}