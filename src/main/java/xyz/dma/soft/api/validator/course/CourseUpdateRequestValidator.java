package xyz.dma.soft.api.validator.course;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.course.CourseUpdateRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;

@Component
public class CourseUpdateRequestValidator extends ARequestValidator<CourseUpdateRequest> {

    @Override
    public IConstraintContext validate(CourseUpdateRequest request) {
        return new ConstraintContextBuilder()
                .assertConstraintViolation(isNull(request.getId()), ConstraintType.EMPTY, "id")
                .assertConstraintViolation(isEmpty(request.getName()), ConstraintType.EMPTY, "name")
                .build();
    }
}
