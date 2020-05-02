package xyz.dma.soft.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.domain.user.User;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "course")
@AllArgsConstructor
@NoArgsConstructor
public class Course implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "course_id_gen")
    @SequenceGenerator(name = "course_id_gen", sequenceName = "course_id_seq")
    private long id;
    @Column(name = "name", unique = true)
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    private User author;
}
