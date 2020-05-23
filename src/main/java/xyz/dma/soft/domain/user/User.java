package xyz.dma.soft.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_gen")
    @SequenceGenerator(name = "user_id_gen", sequenceName = "user_id_seq")
    private long id;
    @Column(name = "login", unique = true, nullable = false)
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "registration_date")
    private LocalDateTime registrationDate;
    @Column(name = "blocked")
    private Boolean blocked;
    @Column(name = "blocked_date")
    private LocalDateTime blockedDate;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private AuthInfo authInfo;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserInfo userInfo;
    @ManyToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<UserRole> userRoles;
}
