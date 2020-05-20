package xyz.dma.soft.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.domain.ChildSchedulingCourseInfo;
import xyz.dma.soft.domain.SchedulingCourseInfo;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChildSchedulingEntity implements Serializable {
    private Long id;
    private CourseInfo courseInfo;
    private int dayOfWeek;
    private String timeStart;
    private String timeEnd;
    private ChildInfoEntity child;

    public ChildSchedulingEntity(ChildSchedulingCourseInfo childSchedulingCourseInfo, SchedulingCourseInfo schedulingCourseInfo) {
        this.id = childSchedulingCourseInfo.getId();
        this.courseInfo = new CourseInfo(childSchedulingCourseInfo.getCourse());
        this.dayOfWeek = schedulingCourseInfo.getDayOfWeek();
        this.timeStart = schedulingCourseInfo.getTimeStart().format(ICommonConstants.TIME_FORMATTER);
        this.timeEnd = schedulingCourseInfo.getTimeEnd().format(ICommonConstants.TIME_FORMATTER);
        this.child = new ChildInfoEntity(childSchedulingCourseInfo.getChildInfo());
    }
}
