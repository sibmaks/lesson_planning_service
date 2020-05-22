package xyz.dma.soft.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.domain.user.User;
import xyz.dma.soft.domain.user.UserInfo;
import xyz.dma.soft.domain.user.UserRole;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoEntity implements Serializable {
    private long userId;
    private String login;
    private String firstName;
    private String lastName;
    private List<String> roles;
    private String roleLine;
    private String lastIpAuth;
    private String lastDateTimeAuth;

    public UserInfoEntity(User user) {
        this(user.getId(), user.getUserInfo());
    }

    public UserInfoEntity(Long userId, UserInfo userInfo) {
        this.userId = userId;
        this.firstName = userInfo == null ? null : userInfo.getFirstName();
        this.lastName = userInfo == null ? null : userInfo.getLastName();
    }

    public UserInfoEntity(User user, boolean extended) {
        this.userId = user.getId();
        if(user.getUserInfo() != null) {
            this.firstName = user.getUserInfo().getFirstName();
            this.lastName = user.getUserInfo().getLastName();
        }
        if(extended) {
            this.login = user.getLogin();
            if(user.getUserRoles() != null) {
                this.roles = user.getUserRoles().stream().map(UserRole::getName).collect(Collectors.toList());
                this.roleLine = String.join(", ", roles);
            }
            if(user.getAuthInfo() != null) {
                this.lastIpAuth = user.getAuthInfo().getLastAuthIp();
                this.lastDateTimeAuth = user.getAuthInfo().getLastAuthDate() == null ? null : user.getAuthInfo().getLastAuthDate().format(ICommonConstants.DATE_TIME_FORMATTER);
            }
        }
    }
}
