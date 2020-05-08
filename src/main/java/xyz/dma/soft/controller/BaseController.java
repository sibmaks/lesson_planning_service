package xyz.dma.soft.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.SessionService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@AllArgsConstructor
public abstract class BaseController {
    protected final SessionService sessionService;

    public HttpServletRequest getCurrentHttpRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            return ((ServletRequestAttributes)requestAttributes).getRequest();
        }
        log.debug("Not called in the context of an HTTP request");
        return null;
    }

    public SessionInfo getCurrentSession() {
        HttpServletRequest request = getCurrentHttpRequest();
        String sessionId = request == null ? null : request.getHeader(ICommonConstants.X_USER_SESSION_ID_HEADER);
        if(sessionId == null && request != null && request.getCookies() != null) {
            for(Cookie cookie : request.getCookies()) {
                if(ICommonConstants.X_USER_SESSION_ID_HEADER.equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }
        return sessionId == null || sessionId.isEmpty() ? null : sessionService.getSessionInfo(sessionId);
    }
}
