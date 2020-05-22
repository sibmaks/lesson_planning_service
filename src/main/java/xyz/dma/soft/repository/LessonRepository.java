package xyz.dma.soft.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.dma.soft.domain.Lesson;
import xyz.dma.soft.domain.user.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface LessonRepository extends CrudRepository<Lesson, Long> {

    @Query("select l from Lesson l where l.course.id = ?1 and l.teacher = ?2 and " +
            "l.lessonStartDate <= ?4 and (l.lessonEndDate is null or l.lessonEndDate >= ?3)")
    List<Lesson> getAllByCourse_IdAndTeacherAndLessonStartDateAfterAndLessonEndDateBefore(Long courseId,
                                                                                          User teacher,
                                                                                          LocalDate startDate,
                                                                                          LocalDate endDate);


    @Query("select l from Lesson l where l.course.id = ?1 and " +
            "l.lessonStartDate <= ?3 and (l.lessonEndDate is null or l.lessonEndDate >= ?2)")
    List<Lesson> getAllByCourse_IdAndLessonStartDateAfterAndLessonEndDateBefore(Long courseId,
                                                                                LocalDate startDate,
                                                                                LocalDate endDate);

    @Query("select l from Lesson l where l.course.id = ?1 and " +
            "l.dayOfWeek = ?2 and " +
            "l.lessonStartDate <= ?3 and (l.lessonEndDate is null or l.lessonEndDate >= ?3)")
    List<Lesson> getAllByCourseAndDayOfWeekAndLessonDate(Long courseId, int dayOfWeek, LocalDate lessonDate);

    boolean existsById(Long id);

    boolean existsByCourse_IdAndTeacherAndDayOfWeekAndTimeStartAndTimeEnd(
            Long courseId,
            User teacher,
            int dayOfWeek,
            LocalTime startTime,
            LocalTime endTime
    );

    Lesson findFirstById(Long id);
}
