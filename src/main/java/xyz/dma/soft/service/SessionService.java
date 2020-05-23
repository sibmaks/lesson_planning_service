package xyz.dma.soft.service;

import org.springframework.stereotype.Service;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.domain.user.User;
import xyz.dma.soft.domain.user.UserAction;
import xyz.dma.soft.domain.user.UserRole;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.storage.SessionStorage;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class SessionService {
    private final SessionStorage sessionStorage;

    public SessionService(SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    public boolean isAuthorized(SessionInfo sessionInfo) {
        return sessionInfo != null && sessionInfo.getUserId() != null && sessionInfo.isAuthorized();
    }

    public SessionInfo createSession(User user, Map<String, String> attributes, boolean authorized) {
        SessionInfo sessionInfo = SessionInfo.builder()
                .id(UUID.randomUUID().toString())
                .userId(user.getId())
                .attributes(attributes)
                .allowedActions(getAllowedActions(user))
                .authorized(authorized)
                .build();
        sessionStorage.put(sessionInfo.getId(), sessionInfo);
        return sessionInfo;
    }

    private List<String> getAllowedActions(User user) {
        List<String> allowedActions = new ArrayList<>();
        List<UserRole> userRoles = user.getUserRoles();
        if (userRoles != null) {
            userRoles.stream()
                    .filter(it -> Objects.nonNull(it.getAllowedActions()))
                    .map(UserRole::getAllowedActions)
                    .flatMap(Collection::stream).map(UserAction::getName).forEach(allowedActions::add);
        }
        return allowedActions;
    }

    public SessionInfo getSessionInfo(String sessionId) {
        return sessionStorage.get(sessionId);
    }

    public void deleteSession(String sessionId) {
        SessionInfo sessionInfo = sessionStorage.get(sessionId);
        if(sessionInfo == null) {
            return;
        }
        sessionInfo.setAuthorized(false);
        sessionStorage.remove(sessionId);
    }

    public void deleteAllExpect(SessionInfo currentSessionInfo) {
        String sessionId = currentSessionInfo.getId();
        Long id = currentSessionInfo.getUserId();
        List<SessionInfo> sessionInfos = sessionStorage.get(it -> it.getUserId().equals(id));
        for(SessionInfo sessionInfo : sessionInfos) {
            if(!sessionInfo.getId().equals(sessionId)) {
                sessionStorage.remove(sessionInfo.getId());
            }
        }
    }

    public SessionInfo getCurrentSession(HttpServletRequest request) {
        String sessionId = request == null ? null : request.getHeader(ICommonConstants.X_USER_SESSION_ID_HEADER);
        if(sessionId == null && request != null && request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(ICommonConstants.X_USER_SESSION_ID_HEADER.equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        return sessionId == null || sessionId.isEmpty() ? null : getSessionInfo(sessionId);
    }
}
