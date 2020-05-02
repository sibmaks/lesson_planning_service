package xyz.dma.soft.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "user_action")
@AllArgsConstructor
@NoArgsConstructor
public class UserAction implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_action_id_gen")
    @SequenceGenerator(name = "user_action_id_gen", sequenceName = "user_action_id_seq")
    private long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
}
