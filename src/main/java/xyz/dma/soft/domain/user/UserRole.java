package xyz.dma.soft.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "user_role")
@AllArgsConstructor
@NoArgsConstructor
public class UserRole implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_role_id_gen")
    @SequenceGenerator(name = "user_role_id_gen", sequenceName = "user_role_id_seq")
    private long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "description")
    private String description;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<UserAction> allowedActions;
}
