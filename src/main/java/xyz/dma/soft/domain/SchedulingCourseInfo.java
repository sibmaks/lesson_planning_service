package xyz.dma.soft.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;

@Data
@Entity
@Builder
@Table(name = "scheduling_course_info")
@AllArgsConstructor
@NoArgsConstructor
public class SchedulingCourseInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "scheduling_course_info_id_gen")
    @SequenceGenerator(name = "scheduling_course_info_id_gen", sequenceName = "scheduling_course_info_id_seq")
    private long id;
    @Column(name = "day_of_week")
    private int dayOfWeek;
    @Column(name = "time_start")
    private LocalTime timeStart;
    @Column(name = "time_end")
    private LocalTime timeEnd;
}
