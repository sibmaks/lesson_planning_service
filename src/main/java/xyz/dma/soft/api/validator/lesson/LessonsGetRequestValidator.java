package xyz.dma.soft.api.validator.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.request.lesson.LessonsGetRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.repository.CourseRepository;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class LessonsGetRequestValidator extends ARequestValidator<LessonsGetRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(LessonsGetRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getCourseId()), "empty", "courseId")
                .addConstraint(1, () -> !courseRepository.existsById(request.getCourseId()), "invalid", "courseId");

        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getFromDate()), "empty", "fromDate")
                .addConstraint(1, () -> !isValidDate(request.getFromDate()), "invalid", "fromDate")
                .addConstraint(0, () -> isNull(request.getToDate()), "empty", "toDate")
                .addConstraint(1, () -> !isValidDate(request.getToDate()), "invalid", "toDate")
                .addConstraint(2, () -> !dateStartBeforeEnd(request.getFromDate(), request.getToDate()), "invalid", "fromDate");

        return context;
    }
}