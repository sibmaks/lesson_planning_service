package xyz.dma.soft.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "child_scheduling_course_info")
@AllArgsConstructor
@NoArgsConstructor
public class ChildSchedulingCourseInfo implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "child_scheduling_course_info_id_gen")
    @SequenceGenerator(name = "child_scheduling_course_info_id_gen", sequenceName = "child_scheduling_course_info_id_seq")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Course course;
    @ManyToOne(fetch = FetchType.EAGER)
    private ChildInfo childInfo;
    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<SchedulingCourseInfo> schedulingCourseInfoList;
}
