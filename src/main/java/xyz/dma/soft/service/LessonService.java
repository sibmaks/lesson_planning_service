package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.entity.ChildInfoEntity;
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
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LessonService {
    private final UserService userService;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final ChildInfoRepository childInfoRepository;

    public Map<Integer, List<LessonEntity>> get(SessionInfo sessionInfo, Long courseId, String fromDate, String toDate) {
        User user = userService.getUser(sessionInfo);

        LocalDate startDateVal = LocalDate.parse(fromDate, ICommonConstants.DATE_FORMATTER);
        LocalDate endDateVal = toDate == null || toDate.trim().isEmpty() ? LocalDate.now() :
                LocalDate.parse(toDate, ICommonConstants.DATE_FORMATTER);

        List<Lesson> lessons = lessonRepository.getAllByCourse_IdAndTeacherAndLessonStartDateAfterAndLessonEndDateBefore(
                courseId, user, startDateVal, endDateVal
        );

        Map<Integer, List<LessonEntity>> lessonEntities = new HashMap<>();

        for(Lesson lesson : lessons) {
            if(!lessonEntities.containsKey(lesson.getDayOfWeek())) {
                lessonEntities.put(lesson.getDayOfWeek(), new ArrayList<>());
            }
            lessonEntities.get(lesson.getDayOfWeek()).add(new LessonEntity(lesson));
        }

        return lessonEntities;
    }

    @Transactional
    public LessonEntity add(SessionInfo sessionInfo, Long courseId, int dayOfWeek, String timeStart, String timeEnd,
                      String lessonStartDate, String lessonEndDate, List<ChildInfoEntity> children) {
        List<Long> childrenIds = children == null ? null : children.stream().map(ChildInfoEntity::getId).collect(Collectors.toList());
        List<ChildInfo> childInfos = children == null || children.isEmpty() ? Collections.emptyList() :
                childInfoRepository.getAllByIdIn(childrenIds);

        User teacher = userService.getUser(sessionInfo);

        LocalTime timeStartVal = LocalTime.parse(timeStart, ICommonConstants.TIME_FORMATTER);
        LocalTime timeEndVal = LocalTime.parse(timeEnd, ICommonConstants.TIME_FORMATTER);

        if(lessonRepository.existsByCourse_IdAndTeacherAndDayOfWeekAndTimeStartAndTimeEnd(courseId, teacher, dayOfWeek,
                timeStartVal, timeEndVal)) {
            throw ServiceException.builder().code(ApiResultCode.ALREADY_EXISTS).build();
        }

        Lesson.LessonBuilder lesson = Lesson.builder()
                .course(courseRepository.findFirstById(courseId))
                .dayOfWeek(dayOfWeek)
                .timeStart(timeStartVal)
                .timeEnd(timeEndVal)
                .lessonStartDate(LocalDate.parse(lessonStartDate, ICommonConstants.DATE_FORMATTER))
                .teacher(teacher)
                .children(childInfos);

        if(lessonEndDate != null && !lessonEndDate.isEmpty()) {
            lesson.lessonEndDate(LocalDate.parse(lessonEndDate, ICommonConstants.DATE_FORMATTER));
        }

        return new LessonEntity(lessonRepository.save(lesson.build()));
    }

    @Transactional
    public LessonEntity update(SessionInfo sessionInfo, Long id, Long courseId, int dayOfWeek, String timeStart,
                               String timeEnd, String lessonStartDate, String lessonEndDate, List<ChildInfoEntity> children) {
        List<Long> childrenIds = children == null ? null : children.stream().map(ChildInfoEntity::getId).collect(Collectors.toList());
        List<ChildInfo> childInfos = children == null || children.isEmpty() ? Collections.emptyList() :
                childInfoRepository.getAllByIdIn(childrenIds);

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

        Lesson.LessonBuilder lessonBuilder = Lesson.builder()
                .id(id)
                .course(courseRepository.findFirstById(courseId))
                .dayOfWeek(dayOfWeek)
                .timeStart(timeStartVal)
                .timeEnd(timeEndVal)
                .lessonStartDate(LocalDate.parse(lessonStartDate, ICommonConstants.DATE_FORMATTER))
                .teacher(teacher)
                .children(childInfos);

        if(lessonEndDate != null && !lessonEndDate.isEmpty()) {
            lessonBuilder.lessonEndDate(LocalDate.parse(lessonEndDate, ICommonConstants.DATE_FORMATTER));
        }

        return new LessonEntity(lessonRepository.save(lessonBuilder.build()));
    }
}
