package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.entity.UserInfoEntity;
import xyz.dma.soft.api.response.user.LoginResponse;
import xyz.dma.soft.domain.user.AuthInfo;
import xyz.dma.soft.domain.user.User;
import xyz.dma.soft.domain.user.UserRole;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.exception.ServiceException;
import xyz.dma.soft.repository.UserRepository;
import xyz.dma.soft.repository.UserRoleRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Transactional
    public Pair<String, LoginResponse> login(String login, String password, String remoteAddress) {
        User user = userRepository.findFirstByLogin(login.toLowerCase());
        if(user == null || !BCrypt.checkpw(password, user.getPassword())) {
            throw ServiceException.builder()
                    .code(ApiResultCode.USER_NOT_FOUND)
                    .build();
        }
        SessionInfo session = sessionService.createSession(user, true);
        if(user.getAuthInfo() == null) {
            user.setAuthInfo(new AuthInfo());
            user.getAuthInfo().setUser(user);
        }
        user.getAuthInfo().setLastAuthDate(LocalDateTime.now());
        user.getAuthInfo().setLastAuthIp(remoteAddress);
        userRepository.save(user);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserInfo(new UserInfoEntity(user));
        loginResponse.setRoleInfos(RoleService.buildRoleInfos(user.getUserRoles()));
        return Pair.of(session.getId(), loginResponse);
    }

    @Transactional
    public void setPassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findFirstById(userId);
        if(!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw ServiceException.builder()
                    .code(ApiResultCode.PASSWORD_MISMATCH)
                    .build();
        }
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        userRepository.save(user);
    }

    @Transactional
    public LoginResponse register(String login, String password, UserInfoEntity userInfoEntity, List<String> roles) {
        if(userRepository.existsByLogin(login.toLowerCase())) {
            throw ServiceException.builder()
                    .code(ApiResultCode.ALREADY_EXISTS)
                    .build();
        }

        User user = User.builder()
                .login(login)
                .password(BCrypt.hashpw(password, BCrypt.gensalt()))
                .registrationDate(LocalDateTime.now())
                .build();

        xyz.dma.soft.domain.user.UserInfo domainUserInfo = new xyz.dma.soft.domain.user.UserInfo(userInfoEntity);
        domainUserInfo.setUser(user);
        user.setUserInfo(domainUserInfo);

        List<UserRole> userRoles = new ArrayList<>();
        for(String role : roles) {
            userRoles.add(userRoleRepository.findFirstByName(role));
        }
        user.setUserRoles(userRoles);

        user = userRepository.save(user);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserInfo(new UserInfoEntity(user));
        loginResponse.setRoleInfos(RoleService.buildRoleInfos(user.getUserRoles()));
        return loginResponse;
    }

    public User getUser(SessionInfo sessionInfo) {
        return userRepository.findFirstById(sessionInfo.getUserId());
    }
}
