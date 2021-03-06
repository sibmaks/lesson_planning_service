package xyz.dma.soft.domain.user;

import lombok.*;
import xyz.dma.soft.api.entity.UserInfoEntity;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_info")
@EqualsAndHashCode(exclude = "user")
@ToString(exclude = "user")
public class UserInfo implements Serializable {
    @Id
    @Column(name = "id")
    private Long id;

    @OneToOne
    @MapsId
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    public UserInfo(UserInfoEntity userInfoEntity) {
        this.firstName = userInfoEntity.getFirstName();
        this.lastName = userInfoEntity.getLastName();
    }
}
