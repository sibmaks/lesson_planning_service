package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.entity.LessonEntity;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.domain.ChildInfo;
import xyz.dma.soft.domain.Lesson;
import xyz.dma.soft.domain.user.User;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.exception.ServiceException;
import xyz.dma.soft.repository.ChildInfoRepository;
import xyz.dma.soft.repository.CourseRepository;
import xyz.dma.soft.repository.LessonRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class LessonService {
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final ChildInfoRepository childInfoRepository;

    public List<LessonEntity> get(SessionInfo sessionInfo, Long courseId, String fromDate, String toDate) {
        User user = userService.getUser(sessionInfo);

        LocalDate startDateVal = LocalDate.parse(fromDate, ICommonConstants.DATE_FORMATTER);
        LocalDate endDateVal = toDate == null || toDate.trim().isEmpty() ? LocalDate.now() :
                LocalDate.parse(toDate, ICommonConstants.DATE_FORMATTER);

        List<Lesson> lessons = lessonRepository.getAllByCourse_IdAndTeacherAndLessonStartDateAfterAndLessonEndDateBefore(
                courseId, user, startDateVal, endDateVal
        );

        List<LessonEntity> lessonEntities = new ArrayList<>();

        for(Lesson lesson : lessons) {
            lessonEntities.add(new LessonEntity(lesson));
        }

        return lessonEntities;
    }

    @Transactional
    public LessonEntity add(SessionInfo sessionInfo, Long courseId, int dayOfWeek, String timeStart, String timeEnd,
                      String lessonStartDate, List<Long> children) {
        List<ChildInfo> childInfos = children == null || children.isEmpty() ? Collections.emptyList() :
                childInfoRepository.getAllByIdIn(children);

        User teacher = userService.getUser(sessionInfo);

        LocalTime timeStartVal = LocalTime.parse(timeStart, ICommonConstants.TIME_FORMATTER);
        LocalTime timeEndVal = LocalTime.parse(timeEnd, ICommonConstants.TIME_FORMATTER);

        if(lessonRepository.existsByCourse_IdAndTeacherAndDayOfWeekAndTimeStartAndTimeEnd(courseId, teacher, dayOfWeek,
                timeStartVal, timeEndVal)) {
            throw ServiceException.builder().code(ApiResultCode.ALREADY_EXISTS).build();
        }

        Lesson lesson = Lesson.builder()
                .course(courseRepository.findFirstById(courseId))
                .dayOfWeek(dayOfWeek)
                .timeStart(timeStartVal)
                .timeEnd(timeEndVal)
                .lessonStartDate(LocalDate.parse(lessonStartDate, ICommonConstants.DATE_FORMATTER))
                .teacher(teacher)
                .children(childInfos)
                .build();

        return new LessonEntity(lessonRepository.save(lesson));
    }

    @Transactional
    public LessonEntity stop(SessionInfo sessionInfo, Long lessonId, String lessonEndDate) {
        User user = userService.getUser(sessionInfo);
        Lesson lesson = lessonRepository.findFirstById(lessonId);

        if(user.getId() != lesson.getTeacher().getId()) {
            throw ServiceException.builder().code(ApiResultCode.ACCESS_DENIED).build();
        }

        if(lessonEndDate == null || lessonEndDate.isEmpty()) {
            lesson.setLessonEndDate(LocalDate.now());
        } else {
            lesson.setLessonEndDate(LocalDate.parse(lessonEndDate, ICommonConstants.DATE_FORMATTER));
        }
        lesson = lessonRepository.save(lesson);
        return new LessonEntity(lesson);
    }

    public LessonEntity update(SessionInfo sessionInfo, Long id, Long courseId, int dayOfWeek, String timeStart,
                               String timeEnd, String lessonStartDate, List<Long> children) {
        List<ChildInfo> childInfos = children == null || children.isEmpty() ? Collections.emptyList() :
                childInfoRepository.getAllByIdIn(children);

        User teacher = userService.getUser(sessionInfo);

        LocalTime timeStartVal = LocalTime.parse(timeStart, ICommonConstants.TIME_FORMATTER);
        LocalTime timeEndVal = LocalTime.parse(timeEnd, ICommonConstants.TIME_FORMATTER);

        Lesson lesson = lessonRepository.findFirstById(id);

        if(!(courseId == lesson.getCourse().getId() && lesson.getTeacher().getId() == teacher.getId() &&
                dayOfWeek == lesson.getDayOfWeek() &&
                timeStartVal.equals(lesson.getTimeStart()) && timeEndVal.equals(lesson.getTimeEnd()))) {
            if(lessonRepository.existsByCourse_IdAndTeacherAndDayOfWeekAndTimeStartAndTimeEnd(courseId, teacher,
                    dayOfWeek, timeStartVal, timeEndVal)) {
                throw ServiceException.builder().code(ApiResultCode.ALREADY_EXISTS).build();
            }
        }

        lesson = Lesson.builder()
                .id(id)
                .course(courseRepository.findFirstById(courseId))
                .dayOfWeek(dayOfWeek)
                .timeStart(timeStartVal)
                .timeEnd(timeEndVal)
                .lessonStartDate(LocalDate.parse(lessonStartDate, ICommonConstants.DATE_FORMATTER))
                .teacher(teacher)
                .children(childInfos)
                .build();

        return new LessonEntity(lessonRepository.save(lesson));
    }
}