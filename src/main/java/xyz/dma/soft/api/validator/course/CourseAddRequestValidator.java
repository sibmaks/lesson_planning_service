package xyz.dma.soft.api.validator.course;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.course.CourseAddRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;

import static java.util.Objects.isNull;

@Component
public class CourseAddRequestValidator extends ARequestValidator<CourseAddRequest> {

    @Override
    public IConstraintContext validate(CourseAddRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        addConstraint(context, isEmpty(request.getName()), "empty", "name");
        return context;
    }
}
