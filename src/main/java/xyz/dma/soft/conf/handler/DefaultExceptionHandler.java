package xyz.dma.soft.conf.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.entity.ResponseInfo;
import xyz.dma.soft.api.response.ConstraintResponse;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.entity.ConstraintType;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.exception.ConstraintException;
import xyz.dma.soft.exception.PageNotFoundException;
import xyz.dma.soft.exception.ServiceException;
import xyz.dma.soft.service.LocalizationService;
import xyz.dma.soft.service.PageInfoService;
import xyz.dma.soft.service.SessionService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {
    private static final String LOCALIZATION_TOKEN_FORMAT = "api.constraint.%s";

    private final PageInfoService pageInfoService;
    private final SessionService sessionService;
    private final LocalizationService localizationService;

    public DefaultExceptionHandler(PageInfoService pageInfoService, SessionService sessionService,
                                   LocalizationService localizationService) {
        this.pageInfoService = pageInfoService;
        this.sessionService = sessionService;
        this.localizationService = localizationService;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public StandardResponse handleException(Exception e) {
        log.error(e.getMessage(), e);
        ResponseInfo responseInfo = ResponseInfo.builder()
                .resultCode(ApiResultCode.UNEXPECTED_ERROR)
                .message(e.getLocalizedMessage())
                .systemMessage(e.getMessage())
                .build();
        return new StandardResponse(responseInfo);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public StandardResponse handleServiceException(ServiceException e) {
        ResponseInfo responseInfo = ResponseInfo.builder()
                .resultCode(e.getCode())
                .message(e.getMessage())
                .systemMessage(e.getSystemMessage())
                .build();
        return new StandardResponse(responseInfo);
    }

    @ExceptionHandler(ConstraintException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public StandardResponse handleConstraintException(HttpServletRequest request, ConstraintException e) {
        ResponseInfo responseInfo = ResponseInfo.builder()
                .resultCode(ApiResultCode.CONSTRAINT_EXCEPTION)
                .build();
        SessionInfo sessionInfo = sessionService.getCurrentSession(request);
        String countryIso3 = null;
        String languageIso3 = null;
        if(sessionInfo != null) {
            countryIso3 = sessionInfo.getCountryIso3();
            languageIso3 = sessionInfo.getLanguageIso3();
        }
        Map<String, String> localizedConstraints = new HashMap<>();
        for(Map.Entry<String, ConstraintType> constraintTypeEntry : e.getConstraintContext().getConstraints().entrySet()) {
            String message = localizationService.getTranslated(countryIso3, languageIso3,
                    String.format(LOCALIZATION_TOKEN_FORMAT, constraintTypeEntry.getValue().name().toLowerCase()));
            localizedConstraints.put(constraintTypeEntry.getKey(), message);
        }
        return new ConstraintResponse(responseInfo, localizedConstraints);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public StandardResponse handleError404(Exception e)   {
        ResponseInfo responseInfo = ResponseInfo.builder()
                .resultCode(ApiResultCode.PAGE_NOT_FOUND)
                .message(e.getLocalizedMessage())
                .systemMessage(e.getMessage())
                .build();
        return new StandardResponse(responseInfo);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public StandardResponse handleMissingHeaderException(MissingRequestHeaderException e)   {
        if(e.getHeaderName().equals(ICommonConstants.X_USER_SESSION_ID_HEADER)) {
            ResponseInfo responseInfo = ResponseInfo.builder()
                    .resultCode(ApiResultCode.UNAUTHORIZED)
                    .message(e.getLocalizedMessage())
                    .systemMessage(e.getMessage())
                    .build();
            return new StandardResponse(responseInfo);
        } else {
            return handleException(e);
        }
    }

    @ExceptionHandler(PageNotFoundException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public String handlePageNotFoundException(HttpServletRequest request, Model model)   {
        SessionInfo sessionInfo = sessionService.getCurrentSession(request);
        PageInfo pageInfo = pageInfoService.getPreparedPageInfo(model, sessionInfo, "404");
        return pageInfo.getTemplatePath();
    }
}
