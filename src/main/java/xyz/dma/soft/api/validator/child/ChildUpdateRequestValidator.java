package xyz.dma.soft.api.validator.child;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.entity.CourseSchedulingInfo;
import xyz.dma.soft.api.request.child.ChildUpdateRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.ChildInfoRepository;
import xyz.dma.soft.repository.CourseRepository;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class ChildUpdateRequestValidator extends ARequestValidator<ChildUpdateRequest> {
    private final CourseRepository courseRepository;
    private final ChildInfoRepository childInfoRepository;

    @Override
    public IConstraintContext validate(ChildUpdateRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getChildInfo()), ConstraintType.EMPTY, "childInfo")
                .addConstraint(1, () -> isNull(request.getChildInfo().getId()), ConstraintType.EMPTY, "childInfo", "id")
                .addConstraint(1, () -> !childInfoRepository.existsById(request.getChildInfo().getId()), ConstraintType.INVALID, "childInfo", "id")
                .addConstraint(1, () -> isEmpty(request.getChildInfo().getName()), ConstraintType.EMPTY, "childInfo", "name")
                .addConstraint(1, () -> isEmpty(request.getChildInfo().getParentName()), ConstraintType.EMPTY, "childInfo", "parentName")
                .addConstraint(1, () -> isEmpty(request.getChildInfo().getPhone()), ConstraintType.EMPTY, "childInfo", "phone");
        if (request.getCourseSchedulingInfos() != null && !request.getCourseSchedulingInfos().isEmpty()) {
            int i = 0;
            for (CourseSchedulingInfo courseSchedulingInfo : request.getCourseSchedulingInfos()) {
                addConstraint(context, !inRange(courseSchedulingInfo.getDayOfWeek(), 1, 7), ConstraintType.INVALID, String.format("courseSchedulingInfos[%d]", i), "dayOfWeek");

                chainConstraint(context)
                        .addConstraint(0, () -> isNull(courseSchedulingInfo.getTimeStart()), ConstraintType.EMPTY, String.format("courseSchedulingInfos[%d]", i), "timeStart")
                        .addConstraint(1, () -> !isValidTime(courseSchedulingInfo.getTimeStart()), ConstraintType.INVALID, String.format("courseSchedulingInfos[%d]", i), "timeStart")
                        .addConstraint(0, () -> isNull(courseSchedulingInfo.getTimeEnd()), ConstraintType.EMPTY, String.format("courseSchedulingInfos[%d]", i), "timeEnd")
                        .addConstraint(1, () -> !isValidTime(courseSchedulingInfo.getTimeEnd()), ConstraintType.INVALID, String.format("courseSchedulingInfos[%d]", i), "timeEnd")
                        .addConstraint(2, () -> !timeStartBeforeEnd(courseSchedulingInfo.getTimeStart(), courseSchedulingInfo.getTimeEnd()), ConstraintType.INVALID, String.format("courseSchedulingInfos[%d]", i), "timeStart");

                chainConstraint(context)
                        .addConstraint(0, () -> isNull(courseSchedulingInfo.getCourseInfo()), ConstraintType.EMPTY, String.format("courseSchedulingInfos[%d]", i), "courseInfo")
                        .addConstraint(1, () -> isNull(courseSchedulingInfo.getCourseInfo().getId()), ConstraintType.EMPTY, String.format("courseSchedulingInfos[%d]", i), "courseInfo", "id")
                        .addConstraint(2, () -> !courseRepository.existsById(courseSchedulingInfo.getCourseInfo().getId()), ConstraintType.INVALID, String.format("courseSchedulingInfos[%d]", i), "courseInfo", "id");
                i++;
            }
        }
        return context;
    }
}