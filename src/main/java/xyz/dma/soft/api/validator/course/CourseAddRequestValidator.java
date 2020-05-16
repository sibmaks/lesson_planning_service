package xyz.dma.soft.api.validator.course;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.course.CourseAddRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;

@Component
public class CourseAddRequestValidator extends ARequestValidator<CourseAddRequest> {

    @Override
    public IConstraintContext validate(CourseAddRequest request) {
        return new ConstraintContextBuilder()
                .assertConstraintViolation(isEmpty(request.getName()), ConstraintType.EMPTY, "name")
                .build();
    }
}
