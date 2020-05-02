package xyz.dma.soft.repository;

import org.springframework.data.repository.CrudRepository;
import xyz.dma.soft.domain.SchedulingCourseInfo;

import java.time.LocalTime;

public interface SchedulingCourseInfoRepository extends CrudRepository<SchedulingCourseInfo, Long> {
    SchedulingCourseInfo findFirstByDayOfWeekAndTimeStartAndTimeEnd(int dayOfWeek, LocalTime timeStart, LocalTime timeEnd);
}
