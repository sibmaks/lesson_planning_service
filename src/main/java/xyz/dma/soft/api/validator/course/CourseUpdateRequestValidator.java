package xyz.dma.soft.api.validator.course;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.course.CourseUpdateRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.CourseRepository;

@AllArgsConstructor
@Component
public class CourseUpdateRequestValidator extends ARequestValidator<CourseUpdateRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(CourseUpdateRequest request) {
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();
        contextBuilder
                .line(request)
                    .validate(it -> notEmpty(it.getName()), "name")
                    .chain()
                        .map(CourseUpdateRequest::getId, "id")
                        .validate(this::notNull)
                        .validate(it -> courseRepository.existsById(it) ? null : ConstraintType.INVALID);
        return contextBuilder.build();
    }
}
