package xyz.dma.soft.api.validator.course;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.course.CourseUpdateRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;

import static java.util.Objects.isNull;

@Component
public class CourseUpdateRequestValidator extends ARequestValidator<CourseUpdateRequest> {

    @Override
    public IConstraintContext validate(CourseUpdateRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isNull(request.getId()), "empty", "id");
        addConstraint(context, isEmpty(request.getName()), "empty", "name");
        return context;
    }
}