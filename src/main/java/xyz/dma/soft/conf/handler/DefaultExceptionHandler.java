package xyz.dma.soft.conf.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
import xyz.dma.soft.exception.ConstraintException;
import xyz.dma.soft.exception.ServiceException;

@Slf4j
@ControllerAdvice
public class DefaultExceptionHandler {

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
    public StandardResponse handleConstraintException(ConstraintException e) {
        ResponseInfo responseInfo = ResponseInfo.builder()
                .resultCode(ApiResultCode.CONSTRAINT_EXCEPTION)
                .build();
        return new ConstraintResponse(responseInfo, e.getConstraintContext().getConstraints());
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
}
