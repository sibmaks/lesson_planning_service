package xyz.dma.soft.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xyz.dma.soft.controller.page.controllers.IPageController;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.exception.PageNotFoundException;
import xyz.dma.soft.service.PageInfoService;
import xyz.dma.soft.service.SessionService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PageController extends BasePageController {
    private final PageInfoService pageInfoService;
    private final Map<String, IPageController> pageControllerMap;

    public PageController(SessionService sessionService, PageInfoService pageInfoService,
                          List<IPageController> pageControllers) {
        super(sessionService);
        this.pageInfoService = pageInfoService;
        this.pageControllerMap = new HashMap<>();
        pageControllers.forEach(it -> this.pageControllerMap.put(it.getName(), it));
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(Model model) {
        SessionInfo sessionInfo = getCurrentSession();
        if(sessionInfo == null || !sessionInfo.isAuthorized()) {
            PageInfo pageInfo = pageInfoService.getPreparedPageInfo(model, sessionInfo, "login");
            return pageInfo.getTemplatePath();
        }
        return "redirect:/home/";
    }

    @RequestMapping(path = "/home/", method = RequestMethod.GET)
    public String home() {
        SessionInfo sessionInfo = getCurrentSession();
        if(sessionInfo == null || !sessionInfo.isAuthorized()) {
            return "redirect:/";
        }
        return "redirect:/scheduling/";
    }

    @RequestMapping(path = "/{page}/", method = RequestMethod.GET)
    public String page(@PathVariable String page, Model model, HttpServletRequest request) {
        SessionInfo sessionInfo = getCurrentSession();
        if(sessionInfo == null || !sessionInfo.isAuthorized()) {
            return "redirect:/";
        }
        if(pageControllerMap.containsKey(page)) {
            return pageControllerMap.get(page).handle(model, request, sessionInfo);
        }
        PageInfo pageInfo = pageInfoService.getPreparedPageInfo(model, sessionInfo, page);
        if(pageInfo == null) {
            throw new PageNotFoundException(page);
        }
        return pageInfo.getTemplatePath();
    }
}
