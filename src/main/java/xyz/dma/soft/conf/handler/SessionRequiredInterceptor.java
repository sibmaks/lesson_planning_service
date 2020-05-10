package xyz.dma.soft.conf.handler;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.core.SessionRequired;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.exception.ServiceException;
import xyz.dma.soft.service.SessionService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@AllArgsConstructor
public class SessionRequiredInterceptor implements HandlerInterceptor {
    private final SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            if(handlerMethod.getMethod().isAnnotationPresent(SessionRequired.class)) {
                SessionInfo sessionInfo = sessionService.getCurrentSession(request);
                if(sessionInfo == null || !sessionInfo.isAuthorized()) {
                    throw ServiceException.builder().code(ApiResultCode.UNAUTHORIZED).build();
                }
                SessionRequired sessionRequired = handlerMethod.getMethod().getAnnotation(SessionRequired.class);
                String requiredAction = sessionRequired.requiredAction();
                if(!requiredAction.isEmpty() && !sessionInfo.getAllowedActions().contains(requiredAction)) {
                    throw ServiceException.builder().code(ApiResultCode.NOT_ALLOWED).build();
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }
}