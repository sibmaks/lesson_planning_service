package xyz.dma.soft.conf.handler;

import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.entity.ResponseInfo;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.PageInfoService;
import xyz.dma.soft.service.SessionService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller("defaultErrorController")
public class DefaultErrorHandler implements ErrorController {
    private final ErrorAttributes errorAttributes;
    private final PageInfoService pageInfoService;
    private final SessionService sessionService;

    public DefaultErrorHandler(ErrorAttributes errorAttributes, PageInfoService pageInfoService,
                               SessionService sessionService) {
        this.errorAttributes = errorAttributes;
        this.pageInfoService = pageInfoService;
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/error", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public StandardResponse handleError(WebRequest request) {
        StandardResponse response = new StandardResponse();
        Map<String, Object> defaultAttributes = this.errorAttributes.getErrorAttributes(request, false);
        ResponseInfo responseInfo = response.getResponseInfo();
        responseInfo.setResultCode(ApiResultCode.UNEXPECTED_ERROR);
        responseInfo.setMessage((String) defaultAttributes.get("message"));
        responseInfo.setSystemMessage((String) defaultAttributes.get("error"));
        return response;
    }

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String handleErrorGET(HttpServletRequest request, Model model) {
        SessionInfo sessionInfo = sessionService.getCurrentSession(request);
        PageInfo pageInfo = pageInfoService.getPreparedPageInfo(model, sessionInfo, "404");
        return pageInfo.getTemplatePath();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
