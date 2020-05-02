package xyz.dma.soft.api.validator.course;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.course.CourseRemoveRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;

import static java.util.Objects.isNull;

@Component
public class CourseRemoveRequestValidator extends ARequestValidator<CourseRemoveRequest> {

    @Override
    public IConstraintContext validate(CourseRemoveRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isNull(request.getId()), "empty", "id");
        return context;
    }
}
