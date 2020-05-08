package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.ChildSchedulingEntity;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.domain.*;
import xyz.dma.soft.repository.ChildSchedulingCourseInfoRepository;
import xyz.dma.soft.repository.LessonRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class SchedulingService {
    private final ChildSchedulingCourseInfoRepository childSchedulingCourseInfoRepository;
    private final LessonRepository lessonRepository;

    public Map<Integer, List<ChildSchedulingEntity>> get(Long courseId, String fromDate, String toDate) {
        List<ChildSchedulingCourseInfo> childSchedulingCourseInfos = childSchedulingCourseInfoRepository.findAllByCourseId(courseId);

        LocalDate startDateVal = LocalDate.parse(fromDate, ICommonConstants.DATE_FORMATTER);
        LocalDate endDateVal = toDate == null || toDate.trim().isEmpty() ? LocalDate.now() :
                LocalDate.parse(toDate, ICommonConstants.DATE_FORMATTER);

        List<Lesson> lessons = lessonRepository.getAllByCourse_IdAndLessonStartDateAfterAndLessonEndDateBefore(courseId,
                startDateVal, endDateVal);
        Map<ChildInfo, Map<Course, List<Lesson>>> childLessons = new HashMap<>();

        for(Lesson lesson : lessons) {
            for(ChildInfo childInfo : lesson.getChildren()) {
                if(!childLessons.containsKey(childInfo)) {
                    childLessons.put(childInfo, new HashMap<>());
                }
                Map<Course, List<Lesson>> courseLessons = childLessons.get(childInfo);
                if(!courseLessons.containsKey(lesson.getCourse())) {
                    courseLessons.put(lesson.getCourse(), new ArrayList<>());
                }
                courseLessons.get(lesson.getCourse()).add(lesson);
            }
        }

        Map<Integer, List<ChildSchedulingEntity>> dayOfWeekScheduling = new HashMap<>();
        for(ChildSchedulingCourseInfo childSchedulingCourseInfo : childSchedulingCourseInfos) {
            if(childLessons.containsKey(childSchedulingCourseInfo.getChildInfo()) &&
                childLessons.get(childSchedulingCourseInfo.getChildInfo()).containsKey(childSchedulingCourseInfo.getCourse())) {
                continue;
            }
            for(SchedulingCourseInfo schedulingCourseInfo : childSchedulingCourseInfo.getSchedulingCourseInfoList()) {
                if(!dayOfWeekScheduling.containsKey(schedulingCourseInfo.getDayOfWeek())) {
                    dayOfWeekScheduling.put(schedulingCourseInfo.getDayOfWeek(), new ArrayList<>());
                }
                ChildSchedulingEntity childSchedulingEntity = new ChildSchedulingEntity(childSchedulingCourseInfo, schedulingCourseInfo);
                dayOfWeekScheduling.get(schedulingCourseInfo.getDayOfWeek()).add(childSchedulingEntity);
            }
        }

        return dayOfWeekScheduling;
    }
}
