package xyz.dma.soft.service;

import org.springframework.stereotype.Service;
import xyz.dma.soft.domain.user.User;
import xyz.dma.soft.domain.user.UserAction;
import xyz.dma.soft.domain.user.UserRole;
import xyz.dma.soft.entity.SessionInfo;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class SessionService {
    private final Lock sessionByLoginModificationLock;
    private final Map<String, SessionInfo> sessionInfoMapBySessionId;
    private final Map<Long, List<SessionInfo>> sessionInfoMapByUserId;

    public SessionService() {
        sessionInfoMapBySessionId = new ConcurrentHashMap<>();
        sessionInfoMapByUserId = new ConcurrentHashMap<>();
        sessionByLoginModificationLock = new ReentrantLock();
    }

    public boolean isSessionValid(String sessionId) {
        SessionInfo sessionInfo = sessionInfoMapBySessionId.get(sessionId);
        return sessionInfo != null && sessionInfo.getUserId() != null;
    }

    public boolean isAuthorized(String sessionId) {
        SessionInfo sessionInfo = sessionInfoMapBySessionId.get(sessionId);
        return isAuthorized(sessionInfo);
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
        sessionInfoMapBySessionId.put(sessionInfo.getId(), sessionInfo);
        try {
            sessionByLoginModificationLock.lock();
            if (!sessionInfoMapByUserId.containsKey(user.getId())) {
                sessionInfoMapByUserId.put(user.getId(), new CopyOnWriteArrayList<>());
            }
            sessionInfoMapByUserId.get(user.getId()).add(sessionInfo);
        } finally {
            sessionByLoginModificationLock.unlock();
        }
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
        return sessionInfoMapBySessionId.get(sessionId);
    }

    public void deleteSession(String sessionId) {
        SessionInfo sessionInfo = sessionInfoMapBySessionId.get(sessionId);
        if(sessionInfo == null) {
            return;
        }
        sessionInfo.setAuthorized(false);
        sessionInfoMapBySessionId.remove(sessionId);
        Long id = sessionInfo.getUserId();
        try {
            sessionByLoginModificationLock.lock();
            if (sessionInfoMapByUserId.containsKey(id)) {
                sessionInfoMapByUserId.get(id).remove(sessionInfo);
                if(sessionInfoMapByUserId.get(id).isEmpty()) {
                    sessionInfoMapByUserId.remove(id);
                }
            }
        } finally {
            sessionByLoginModificationLock.unlock();
        }
    }

    public void deleteAllExpectCurrent(SessionInfo currentSessionInfo) {
        String sessionId = currentSessionInfo.getId();
        Long id = currentSessionInfo.getUserId();
        try {
            sessionByLoginModificationLock.lock();
            if (sessionInfoMapByUserId.containsKey(id)) {
                List<SessionInfo> sessionInfos = sessionInfoMapByUserId.get(id);
                for(int i = 0; i < sessionInfos.size(); i++) {
                    SessionInfo sessionInfo = sessionInfos.get(i);
                    if(!sessionId.equals(sessionInfo.getId())) {
                        sessionInfos.remove(sessionInfo);
                        sessionInfoMapBySessionId.remove(sessionInfo.getId());
                        i--;
                    }
                }
            }
        } finally {
            sessionByLoginModificationLock.unlock();
        }
    }
}
