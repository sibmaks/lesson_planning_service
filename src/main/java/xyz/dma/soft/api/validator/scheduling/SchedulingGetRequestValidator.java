package xyz.dma.soft.api.validator.scheduling;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.scheduling.SchedulingGetRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.CourseRepository;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class SchedulingGetRequestValidator extends ARequestValidator<SchedulingGetRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(SchedulingGetRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getCourseId()), ConstraintType.EMPTY, "courseId")
                .addConstraint(1, () -> !courseRepository.existsById(request.getCourseId()), ConstraintType.INVALID, "courseId");

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getFromDate()), ConstraintType.EMPTY, "fromDate")
                .addConstraint(1, () -> !isValidDate(request.getFromDate()), ConstraintType.INVALID, "fromDate")
                .addConstraint(0, () -> isNull(request.getToDate()), ConstraintType.EMPTY, "toDate")
                .addConstraint(1, () -> !isValidDate(request.getToDate()), ConstraintType.INVALID, "toDate")
                .addConstraint(2, () -> !dateStartBeforeEnd(request.getFromDate(), request.getToDate()), ConstraintType.INVALID, "fromDate");

        return context;
    }
}