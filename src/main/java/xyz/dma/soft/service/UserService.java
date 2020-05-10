package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.entity.UserInfoEntity;
import xyz.dma.soft.api.response.user.LoginResponse;
import xyz.dma.soft.api.response.user.RegisterResponse;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserService {
    private final SessionService sessionService;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final LocalizationService localizationService;

    @Transactional
    public Pair<String, LoginResponse> login(String login, String password, String remoteAddress,
                                             String countryIso3, String languageIso3) {
        User user = userRepository.findFirstByLogin(login.toLowerCase());
        if(user == null || !BCrypt.checkpw(password, user.getPassword())) {
            throw ServiceException.builder()
                    .code(ApiResultCode.USER_NOT_FOUND)
                    .message(localizationService.getTranslation(countryIso3, languageIso3,
                            "ui.text.error.user_not_found").getTranslation())
                    .systemMessage(localizationService.getTranslation(null, "eng",
                            "ui.text.error.user_not_found").getTranslation())
                    .build();
        }
        Map<String, String> attributes = new HashMap<>();
        attributes.put(SessionInfo.ATTR_COUNTRY_ISO3, countryIso3);
        attributes.put(SessionInfo.ATTR_LANGUAGE_ISO3, languageIso3);
        SessionInfo session = sessionService.createSession(user, attributes, true);
        if(user.getAuthInfo() == null) {
            user.setAuthInfo(new AuthInfo());
            user.getAuthInfo().setUser(user);
        }
        user.getAuthInfo().setLastAuthDate(LocalDateTime.now());
        user.getAuthInfo().setLastAuthIp(remoteAddress);
        userRepository.save(user);
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setStartPageUrl("/home/");
        loginResponse.setUserInfo(new UserInfoEntity(user));
        loginResponse.setRoleInfos(RoleService.buildRoleInfos(user.getUserRoles()));
        return Pair.of(session.getId(), loginResponse);
    }

    @Transactional
    public void setPassword(SessionInfo sessionInfo, String oldPassword, String newPassword) {
        User user = userRepository.findFirstById(sessionInfo.getUserId());
        if(!BCrypt.checkpw(oldPassword, user.getPassword())) {
            throw ServiceException.builder()
                    .code(ApiResultCode.PASSWORD_MISMATCH)
                    .message(localizationService
                            .getTranslation(sessionInfo.getCountryIso3(), sessionInfo.getLanguageIso3(),
                                    "ui.text.error.passwords_mismatch").getTranslation())
                    .systemMessage(localizationService
                            .getTranslation(null, "eng",
                                    "ui.text.error.passwords_mismatch").getTranslation())
                    .build();
        }
        user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
        userRepository.save(user);
    }

    @Transactional
    public RegisterResponse register(SessionInfo sessionInfo, String login, String password,
                                     UserInfoEntity userInfoEntity, List<String> roles) {
        if(userRepository.existsByLogin(login.toLowerCase())) {
            throw ServiceException.builder()
                    .code(ApiResultCode.ALREADY_EXISTS)
                    .message(localizationService
                            .getTranslation(sessionInfo.getCountryIso3(), sessionInfo.getLanguageIso3(),
                                    "ui.text.error.already_exists").getTranslation())
                    .systemMessage(localizationService
                            .getTranslation(null, "eng",
                                    "ui.text.error.already_exists").getTranslation())
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

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserInfo(new UserInfoEntity(user));
        registerResponse.setRoleInfos(RoleService.buildRoleInfos(user.getUserRoles()));
        return registerResponse;
    }

    public User getUser(SessionInfo sessionInfo) {
        return userRepository.findFirstById(sessionInfo.getUserId());
    }
}
