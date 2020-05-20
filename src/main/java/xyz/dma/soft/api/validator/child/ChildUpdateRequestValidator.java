package xyz.dma.soft.api.validator.child;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.entity.ChildInfoEntity;
import xyz.dma.soft.api.entity.CourseInfo;
import xyz.dma.soft.api.entity.CourseSchedulingInfo;
import xyz.dma.soft.api.request.child.ChildUpdateRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.impl.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.ChildInfoRepository;
import xyz.dma.soft.repository.CourseRepository;

@Component
@AllArgsConstructor
public class ChildUpdateRequestValidator extends ARequestValidator<ChildUpdateRequest> {
    private final CourseRepository courseRepository;
    private final ChildInfoRepository childInfoRepository;

    @Override
    public IConstraintContext validate(ChildUpdateRequest request) {
        ConstraintContextBuilder context = new ConstraintContextBuilder();

        ILineConstraintValidator<ChildUpdateRequest> constraintValidator = context.line(request);
        constraintValidator
                .chain()
                .map(ChildUpdateRequest::getChildInfo, "childInfo")
                .validate(this::notNull)
                .line()
                    .validate(it -> notEmpty(it.getName()), "name")
                    .validate(it -> notEmpty(it.getParentName()), "parentName")
                    .validate(it -> notEmpty(it.getPhone()), "phone")
                    .chain()
                        .map(ChildInfoEntity::getId, "id")
                        .validate(this::notNull)
                        .validate(it -> childInfoRepository.existsById(it) ? null : ConstraintType.INVALID);

        ILineConstraintValidator<CourseSchedulingInfo> courseSchedulingInfoValidator = constraintValidator
                .chain()
                .filter(it -> notEmpty(it.getCourseSchedulingInfos()))
                .flatMap(ChildUpdateRequest::getCourseSchedulingInfos, "courseSchedulingInfos")
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