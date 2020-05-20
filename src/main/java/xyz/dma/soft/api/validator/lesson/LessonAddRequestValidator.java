package xyz.dma.soft.api.validator.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.entity.CourseInfo;
import xyz.dma.soft.api.entity.LessonEntity;
import xyz.dma.soft.api.request.lesson.LessonAddRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.CourseRepository;

@Component
@AllArgsConstructor
public class LessonAddRequestValidator extends ARequestValidator<LessonAddRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(LessonAddRequest request) {
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();
        ILineConstraintValidator<LessonEntity> constraintValidator = contextBuilder
                .chain(request)
                    .map(LessonAddRequest::getLessonEntity, "lessonEntity")
                    .validate(this::notNull)
                    .line();

        constraintValidator
                .chain()
                    .map(LessonEntity::getCourseInfo, "courseInfo")
                    .validate(this::notNull)
                    .map(CourseInfo::getId, "id")
                    .validate(this::notNull)
                    .validate(it -> courseRepository.existsById(it) ? null : ConstraintType.INVALID);

        constraintValidator
                .chain()
                .map(LessonEntity::getLessonStartDate, "lessonStartDate")
                .validate(this::notNull)
                .validate(this::isValidDate);

        constraintValidator
                .chain()
                .map(LessonEntity::getDayOfWeek, "dayOfWeek")
                .validate(it -> inRange(it, 1, 7));

        IChainConstraintValidator<LessonEntity> timeChainConstraint = constraintValidator.chain();
        ILineConstraintValidator<LessonEntity> timeBorderChainConstraint = timeChainConstraint.line();

        timeBorderChainConstraint
                .chain()
                .map(LessonEntity::getTimeStart, "timeStart")
                .validate(this::notNull)
                .validate(this::isValidTime);

        timeBorderChainConstraint
                .chain()
                .map(LessonEntity::getTimeEnd, "timeEnd")
                .validate(this::notNull)
                .validate(this::isValidTime);

        timeChainConstraint
                .validate(it -> timeStartBeforeEnd(it.getTimeStart(), it.getTimeEnd()), "timeStart");

        return contextBuilder.build();
    }
}