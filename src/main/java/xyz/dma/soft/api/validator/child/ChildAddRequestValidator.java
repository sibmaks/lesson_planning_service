package xyz.dma.soft.api.validator.child;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import xyz.dma.soft.api.entity.CourseSchedulingInfo;
import xyz.dma.soft.api.request.child.ChildAddRequest;
import xyz.dma.soft.api.validator.ARequestValidator;
import xyz.dma.soft.core.constraint.ConstraintContextBuilder;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.repository.CourseRepository;


@Component
@AllArgsConstructor
public class ChildAddRequestValidator extends ARequestValidator<ChildAddRequest> {
    private final CourseRepository courseRepository;

    @Override
    public IConstraintContext validate(ChildAddRequest request) {
        ConstraintContextBuilder context = new ConstraintContextBuilder()
                .assertConstraintViolation(0, isNull(request.getChildInfo()), ConstraintType.EMPTY, "childInfo")
                .assertConstraintViolation(1, isEmpty(request.getChildInfo().getName()), ConstraintType.EMPTY, "childInfo", "name")
                .assertConstraintViolation(1, isEmpty(request.getChildInfo().getParentName()), ConstraintType.EMPTY, "childInfo", "parentName")
                .assertConstraintViolation(1, isEmpty(request.getChildInfo().getPhone()), ConstraintType.EMPTY, "childInfo", "phone");
        if (request.getCourseSchedulingInfos() != null && !request.getCourseSchedulingInfos().isEmpty()) {
            int i = 0;
            for (CourseSchedulingInfo courseSchedulingInfo : request.getCourseSchedulingInfos()) {
                context
                        .assertConstraintViolation(0, not(inRange(courseSchedulingInfo.getDayOfWeek(), 1, 7)), ConstraintType.INVALID, String.format("courseSchedulingInfos[%d]", i), "dayOfWeek")

                        .assertConstraintViolation(0, isNull(courseSchedulingInfo.getTimeStart()), ConstraintType.EMPTY, String.format("courseSchedulingInfos[%d]", i), "timeStart")
                        .assertConstraintViolation(1, not(isValidTime(courseSchedulingInfo.getTimeStart())), ConstraintType.INVALID, String.format("courseSchedulingInfos[%d]", i), "timeStart")

                        .assertConstraintViolation(0, isNull(courseSchedulingInfo.getTimeEnd()), ConstraintType.EMPTY, String.format("courseSchedulingInfos[%d]", i), "timeEnd")
                        .assertConstraintViolation(1, not(isValidTime(courseSchedulingInfo.getTimeEnd())), ConstraintType.INVALID, String.format("courseSchedulingInfos[%d]", i), "timeEnd")
                        .assertConstraintViolation(2, not(timeStartBeforeEnd(courseSchedulingInfo.getTimeStart(), courseSchedulingInfo.getTimeEnd())), ConstraintType.INVALID, String.format("courseSchedulingInfos[%d]", i), "timeStart")

                        .assertConstraintViolation(0, isNull(courseSchedulingInfo.getCourseInfo()), ConstraintType.EMPTY, String.format("courseSchedulingInfos[%d]", i), "courseInfo")
                        .assertConstraintViolation(1, isNull(courseSchedulingInfo.getCourseInfo().getId()), ConstraintType.EMPTY, String.format("courseSchedulingInfos[%d]", i), "courseInfo", "id")
                        .assertConstraintViolation(2, () -> !courseRepository.existsById(courseSchedulingInfo.getCourseInfo().getId()), ConstraintType.INVALID, String.format("courseSchedulingInfos[%d]", i), "courseInfo", "id");
                i++;
            }
        }
        return context.build();
    }
}