package xyz.dma.soft.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.domain.Lesson;

import java.io.Serializable;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonEntity implements Serializable {

    private Long id;
    private CourseInfo courseInfo;
    private int dayOfWeek;
    private String timeStart;
    private String timeEnd;
    private String lessonStartDate;
    private String lessonEndDate;
    private List<ChildInfoEntity> children;
    private UserInfoEntity teacher;

    public LessonEntity(Lesson lesson) {
        this.id = lesson.getId();
        this.dayOfWeek = lesson.getDayOfWeek();
        this.timeStart = lesson.getTimeStart().format(ICommonConstants.TIME_FORMATTER);
        this.timeEnd = lesson.getTimeEnd().format(ICommonConstants.TIME_FORMATTER);
        this.lessonStartDate = lesson.getLessonStartDate().format(ICommonConstants.DATE_FORMATTER);
        this.lessonEndDate = lesson.getLessonEndDate() == null ? null : lesson.getLessonEndDate().format(ICommonConstants.DATE_FORMATTER);
        this.courseInfo = new CourseInfo(lesson.getCourse());
        this.teacher = new UserInfoEntity(lesson.getTeacher());
        this.children = lesson.getChildren().stream().map(ChildInfoEntity::new).collect(toList());
    }
}
