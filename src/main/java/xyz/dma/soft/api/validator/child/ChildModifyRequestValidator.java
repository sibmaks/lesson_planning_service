package xyz.dma.soft.api.validator.child;

import org.springframework.stereotype.Component;
import xyz.dma.soft.api.entity.CourseSchedulingInfo;
import xyz.dma.soft.api.request.child.ChildModifyRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.core.impl.ConstraintContextImpl;

import static java.util.Objects.isNull;

@Component
public class ChildModifyRequestValidator extends ARequestValidator<ChildModifyRequest> {

    @Override
    public IConstraintContext validate(ChildModifyRequest request) {
        ConstraintContextImpl context = new ConstraintContextImpl();
        chainConstraint(context)
                .addConstraint(0, () -> isNull(request.getChildInfo()), "empty", "childInfo")
                .addConstraint(1, () -> isNull(request.getChildInfo().getId()), "empty", "childInfo", "id")
                .addConstraint(1, () -> isEmpty(request.getChildInfo().getName()), "empty", "childInfo", "name")
                .addConstraint(1, () -> isEmpty(request.getChildInfo().getParentName()), "empty", "childInfo", "parentName")
                .addConstraint(1, () -> isEmpty(request.getChildInfo().getPhone()), "empty", "childInfo", "phone");
        if(request.getCourseSchedulingInfos() != null && !request.getCourseSchedulingInfos().isEmpty()) {
            int i = 0;
            for(CourseSchedulingInfo courseSchedulingInfo : request.getCourseSchedulingInfos()) {
                addConstraint(context, !inRange(courseSchedulingInfo.getDayOfWeek(), 1, 7),"invalid", String.format("courseSchedulingInfos[%d]", i), "dayOfWeek");

                chainConstraint(context)
                        .addConstraint(0, () -> isNull(courseSchedulingInfo.getTimeStart()), "empty", String.format("courseSchedulingInfos[%d]", i), "timeStart")
                        .addConstraint(1, () -> !isValidTime(courseSchedulingInfo.getTimeStart()), "invalid", String.format("courseSchedulingInfos[%d]", i), "timeStart")
                        .addConstraint(0, () -> isNull(courseSchedulingInfo.getTimeEnd()), "empty", String.format("courseSchedulingInfos[%d]", i), "timeEnd")
                        .addConstraint(1, () -> !isValidTime(courseSchedulingInfo.getTimeEnd()), "invalid", String.format("courseSchedulingInfos[%d]", i), "timeEnd")
                        .addConstraint(2, () -> !startBeforeEnd(courseSchedulingInfo.getTimeStart(), courseSchedulingInfo.getTimeEnd()), "invalid", String.format("courseSchedulingInfos[%d]", i), "timeStart");

                chainConstraint(context)
                    .addConstraint(0, () -> isNull(courseSchedulingInfo.getCourseInfo()), "empty", String.format("courseSchedulingInfos[%d]", i), "courseInfo")
                    .addConstraint(1, () -> isNull(courseSchedulingInfo.getCourseInfo().getId()), "empty", String.format("courseSchedulingInfos[%d]", i), "courseInfo", "id");
                i++;
            }
        }
        return context;
    }
}