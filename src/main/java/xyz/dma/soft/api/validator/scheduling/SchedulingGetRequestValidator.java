package xyz.dma.soft.api.validator.scheduling;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.scheduling.SchedulingGetRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.CourseRepository;

@Component
@AllArgsConstructor
public class SchedulingGetRequestValidator extends ARequestValidator<SchedulingGetRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(SchedulingGetRequest request) {
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();
        
        ILineConstraintValidator<SchedulingGetRequest> constraintValidator = contextBuilder.line(request);
        constraintValidator
                .chain()
                    .map(SchedulingGetRequest::getCourseId, "courseId")
                    .validate(this::notNull)
                    .validate(it -> courseRepository.existsById(it) ? null : ConstraintType.INVALID);

        IChainConstraintValidator<SchedulingGetRequest> dateChainConstraint = constraintValidator.chain();
        ILineConstraintValidator<SchedulingGetRequest> dateBorderChainConstraint = dateChainConstraint.line();

        dateBorderChainConstraint
                .chain()
                    .map(SchedulingGetRequest::getFromDate, "fromDate")
                    .validate(this::notNull)
                    .validate(this::isValidDate);

        dateBorderChainConstraint
                .chain()
                    .map(SchedulingGetRequest::getToDate, "toDate")
                    .validate(this::notNull)
                    .validate(this::isValidDate);

        dateChainConstraint
                .validate(it -> dateStartBeforeEnd(it.getFromDate(), it.getToDate()), "fromDate");

        return contextBuilder.build();
    }
}