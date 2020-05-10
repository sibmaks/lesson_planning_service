package xyz.dma.soft.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.SessionService;

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
        return sessionService.getCurrentSession(getCurrentHttpRequest());
    }
}
