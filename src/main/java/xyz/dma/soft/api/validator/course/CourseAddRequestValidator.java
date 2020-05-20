package xyz.dma.soft.api.validator.course;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.course.CourseAddRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.impl.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;

@Component
public class CourseAddRequestValidator extends ARequestValidator<CourseAddRequest> {

    @Override
    public IConstraintContext validate(CourseAddRequest request) {
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();
        contextBuilder
                .line(request)
                .validate(it -> notEmpty(it.getName()), "name");
        return contextBuilder.build();
    }
}
