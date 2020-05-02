package xyz.dma.soft.conf.handler;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.entity.ResponseInfo;
import xyz.dma.soft.api.response.StandardResponse;

import java.util.Map;

@RestController("defaultErrorController")
public class DefaultErrorHandler implements ErrorController {
    private final ErrorAttributes errorAttributes;

    public DefaultErrorHandler(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    @ResponseStatus(HttpStatus.OK)
    public StandardResponse handleError(WebRequest request) {
        StandardResponse response = new StandardResponse();
        Map<String, Object> defaultAttributes = this.errorAttributes.getErrorAttributes(request, false);
        ResponseInfo responseInfo = response.getResponseInfo();
        responseInfo.setResultCode(ApiResultCode.UNEXPECTED_ERROR);
        responseInfo.setMessage((String) defaultAttributes.get("message"));
        responseInfo.setSystemMessage((String) defaultAttributes.get("error"));
        return response;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
