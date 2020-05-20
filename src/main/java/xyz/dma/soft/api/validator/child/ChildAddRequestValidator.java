package xyz.dma.soft.api.validator.child;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.entity.CourseInfo;
import xyz.dma.soft.api.entity.CourseSchedulingInfo;
import xyz.dma.soft.api.request.child.ChildAddRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.impl.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.CourseRepository;


@Component
@AllArgsConstructor
public class ChildAddRequestValidator extends ARequestValidator<ChildAddRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(ChildAddRequest request) {
        ConstraintContextBuilder context = new ConstraintContextBuilder();

        ILineConstraintValidator<ChildAddRequest> constraintValidator = context.line(request);
        constraintValidator
                .chain()
                    .map(ChildAddRequest::getChildInfo, "childInfo")
                    .validate(this::notNull)
                    .line()
                        .validate(it -> notEmpty(it.getName()), "name")
                        .validate(it -> notEmpty(it.getParentName()), "parentName")
                        .validate(it -> notEmpty(it.getPhone()), "phone");

        ILineConstraintValidator<CourseSchedulingInfo> courseSchedulingInfoValidator = constraintValidator
                .chain()
                    .filter(it -> notEmpty(it.getCourseSchedulingInfos()))
                        .flatMap(ChildAddRequest::getCourseSchedulingInfos, "courseSchedulingInfos")
                        .validate(this::notNull)
                        .line()
                            .validate(it -> inRange(it.getDayOfWeek(), 1, 7), "dayOfWeek");

        courseSchedulingInfoValidator
                .chain()
                    .map(CourseSchedulingInfo::getCourseInfo, "courseInfo")
                    .validate(this::notNull)
                    .map(CourseInfo::getId, "id")
                    .validate(this::notNull)
                    .validate(it -> courseRepository.existsById(it) ? null : ConstraintType.INVALID);

        IChainConstraintValidator<CourseSchedulingInfo> timeChainConstraint = courseSchedulingInfoValidator.chain();
        ILineConstraintValidator<CourseSchedulingInfo> timeBorderChainConstraint = timeChainConstraint.line();

        timeBorderChainConstraint
                .chain()
                    .map(CourseSchedulingInfo::getTimeStart, "timeStart")
                    .validate(this::notEmpty)
                    .validate(this::isValidTime);

        timeBorderChainConstraint
                .chain()
                    .map(CourseSchedulingInfo::getTimeEnd, "timeEnd")
                    .validate(this::notEmpty)
                    .validate(this::isValidTime);

        timeChainConstraint
                .validate(it -> timeStartBeforeEnd(it.getTimeStart(), it.getTimeEnd()), "timeStart");

        return context.build();
    }
}