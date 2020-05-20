package xyz.dma.soft.api.validator.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.lesson.LessonsGetRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.impl.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.CourseRepository;

@Component
@AllArgsConstructor
public class LessonsGetRequestValidator extends ARequestValidator<LessonsGetRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(LessonsGetRequest request) {
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();

        ILineConstraintValidator<LessonsGetRequest> constraintValidator = contextBuilder.line(request);

        constraintValidator
                .chain()
                .map(LessonsGetRequest::getCourseId, "courseId")
                .validate(this::notNull)
                .validate(it -> !courseRepository.existsById(it) ? ConstraintType.INVALID : null);

        IChainConstraintValidator<LessonsGetRequest> dateChainConstraint = constraintValidator.chain();
        ILineConstraintValidator<LessonsGetRequest> dateBorderChainConstraint = dateChainConstraint.line();

        dateBorderChainConstraint
                .chain()
                .map(LessonsGetRequest::getFromDate, "fromDate")
                .validate(this::notNull)
                .validate(this::isValidDate);

        dateBorderChainConstraint
                .chain()
                .map(LessonsGetRequest::getToDate, "toDate")
                .validate(this::notNull)
                .validate(this::isValidDate);

        dateChainConstraint
                .validate(it -> dateStartBeforeEnd(it.getFromDate(), it.getToDate()), "fromDate");
        return contextBuilder.build();
    }
}