package xyz.dma.soft.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.domain.user.User;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoEntity implements Serializable {
    private long userId;
    private String firstName;
    private String lastName;

    public UserInfoEntity(User user) {
        this(user.getId(), user.getUserInfo());
    }

    public UserInfoEntity(Long userId, xyz.dma.soft.domain.user.UserInfo userInfo) {
        this.userId = userId;
        this.firstName = userInfo == null ? null : userInfo.getFirstName();
        this.lastName = userInfo == null ? null : userInfo.getLastName();
    }
}
