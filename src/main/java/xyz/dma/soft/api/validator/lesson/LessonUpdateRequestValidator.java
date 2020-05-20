package xyz.dma.soft.api.validator.lesson;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.entity.CourseInfo;
import xyz.dma.soft.api.entity.LessonEntity;
import xyz.dma.soft.api.request.lesson.LessonUpdateRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.CourseRepository;
import xyz.dma.soft.repository.LessonRepository;

@Component
@AllArgsConstructor
public class LessonUpdateRequestValidator extends ARequestValidator<LessonUpdateRequest> {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @Override
    public IConstraintContext validate(LessonUpdateRequest request) {
        ConstraintContextBuilder contextBuilder = new ConstraintContextBuilder();
        ILineConstraintValidator<LessonEntity> constraintValidator = contextBuilder
                .chain(request)
                .map(LessonUpdateRequest::getLessonEntity, "lessonEntity")
                .validate(this::notNull)
                .line();

        constraintValidator
                .chain()
                .map(LessonEntity::getId, "id")
                .validate(this::notNull)
                .validate(it -> lessonRepository.existsById(it) ? null : ConstraintType.INVALID);

        constraintValidator
                .chain()
                .map(LessonEntity::getCourseInfo, "courseInfo")
                .validate(this::notNull)
                .map(CourseInfo::getId, "id")
                .validate(this::notNull)
                .validate(it -> courseRepository.existsById(it) ? null : ConstraintType.INVALID);

        constraintValidator
                .chain()
                .map(LessonEntity::getDayOfWeek, "dayOfWeek")
                .validate(it -> inRange(it, 1, 7));

        IChainConstraintValidator<LessonEntity> timeChainConstraint = constraintValidator.chain();
        ILineConstraintValidator<LessonEntity> timeBorderChainConstraint = timeChainConstraint.line();

        timeBorderChainConstraint
                .chain()
                .map(LessonEntity::getTimeStart, "timeStart")
                .validate(this::notEmpty)
                .validate(this::isValidTime);

        timeBorderChainConstraint
                .chain()
                .map(LessonEntity::getTimeEnd, "timeEnd")
                .validate(this::notEmpty)
                .validate(this::isValidTime);

        timeChainConstraint
                .validate(it -> timeStartBeforeEnd(it.getTimeStart(), it.getTimeEnd()), "timeStart");

        IChainConstraintValidator<LessonEntity> dateChainConstraint = constraintValidator.chain();
        ILineConstraintValidator<LessonEntity> dateBorderChainConstraint = dateChainConstraint.line();

        dateBorderChainConstraint
                .chain()
                .map(LessonEntity::getLessonStartDate, "lessonStartDate")
                .validate(this::notEmpty)
                .validate(this::isValidDate);

         dateBorderChainConstraint
                .chain()
                .map(LessonEntity::getLessonEndDate, "lessonEndDate")
                .filter(this::notEmpty)
                .validate(this::isValidDate);

        dateChainConstraint
                .filter(it -> notEmpty(it.getLessonEndDate()))
                .validate(it -> dateStartBeforeEnd(it.getLessonStartDate(), it.getLessonEndDate()), "lessonStartDate");

        return contextBuilder.build();
    }
}