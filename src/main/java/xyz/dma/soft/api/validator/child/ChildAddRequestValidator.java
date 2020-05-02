package xyz.dma.soft.api.validator.child;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.entity.CourseSchedulingInfo;
import xyz.dma.soft.api.request.child.ChildAddRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;
import xyz.dma.soft.repository.CourseRepository;

import static java.util.Objects.isNull;

@Component
@AllArgsConstructor
public class ChildAddRequestValidator extends ARequestValidator<ChildAddRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(ChildAddRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getChildInfo()), "empty", "childInfo")
                .addConstraint(1, () -> isEmpty(request.getChildInfo().getName()), "empty", "childInfo", "name")
                .addConstraint(1, () -> isEmpty(request.getChildInfo().getParentName()), "empty", "childInfo", "parentName")
                .addConstraint(1, () -> isEmpty(request.getChildInfo().getPhone()), "empty", "childInfo", "phone");
        if (request.getCourseSchedulingInfos() != null && !request.getCourseSchedulingInfos().isEmpty()) {
            int i = 0;
            for (CourseSchedulingInfo courseSchedulingInfo : request.getCourseSchedulingInfos()) {
                addConstraint(context, !inRange(courseSchedulingInfo.getDayOfWeek(), 1, 7), "invalid", String.format("courseSchedulingInfos[%d]", i), "dayOfWeek");

                chainConstraint(context)
                        .addConstraint(0, () -> isNull(courseSchedulingInfo.getTimeStart()), "empty", String.format("courseSchedulingInfos[%d]", i), "timeStart")
                        .addConstraint(1, () -> !isValidTime(courseSchedulingInfo.getTimeStart()), "invalid", String.format("courseSchedulingInfos[%d]", i), "timeStart")
                        .addConstraint(0, () -> isNull(courseSchedulingInfo.getTimeEnd()), "empty", String.format("courseSchedulingInfos[%d]", i), "timeEnd")
                        .addConstraint(1, () -> !isValidTime(courseSchedulingInfo.getTimeEnd()), "invalid", String.format("courseSchedulingInfos[%d]", i), "timeEnd")
                        .addConstraint(2, () -> !startBeforeEnd(courseSchedulingInfo.getTimeStart(), courseSchedulingInfo.getTimeEnd()), "invalid", String.format("courseSchedulingInfos[%d]", i), "timeStart");

                chainConstraint(context)
                        .addConstraint(0, () -> isNull(courseSchedulingInfo.getCourseInfo()), "empty", String.format("courseSchedulingInfos[%d]", i), "courseInfo")
                        .addConstraint(1, () -> isNull(courseSchedulingInfo.getCourseInfo().getId()), "empty", String.format("courseSchedulingInfos[%d]", i), "courseInfo", "id")
                        .addConstraint(2, () -> !courseRepository.existsById(courseSchedulingInfo.getCourseInfo().getId()), "invalid", String.format("courseSchedulingInfos[%d]", i), "courseInfo", "id");
                i++;
            }
        }
        return context;
    }
}