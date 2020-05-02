package xyz.dma.soft.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_info")
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

    public UserInfo(xyz.dma.soft.api.entity.UserInfo userInfo) {
        this.firstName = userInfo.getFirstName();
        this.lastName = userInfo.getLastName();
    }
}
