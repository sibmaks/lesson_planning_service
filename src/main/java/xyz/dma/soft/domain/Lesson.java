package xyz.dma.soft.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import xyz.dma.soft.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "lesson")
@AllArgsConstructor
@NoArgsConstructor
public class Lesson implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_id_gen")
    @SequenceGenerator(name = "lesson_id_gen", sequenceName = "lesson_id_seq")
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;
    @Column(name = "day_of_week")
    private int dayOfWeek;

    @Column(name = "time_start")
    private LocalTime timeStart;
    @Column(name = "time_end")
    private LocalTime timeEnd;

    @Column(name = "lesson_start_date")
    private LocalDate lessonStartDate;
    @Column(name = "lesson_end_date")
    private LocalDate lessonEndDate;

    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ChildInfo> children;

    @ManyToOne(fetch = FetchType.EAGER)
    private User teacher;
}
