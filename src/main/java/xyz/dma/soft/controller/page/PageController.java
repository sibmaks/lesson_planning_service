package xyz.dma.soft.controller.page;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.LocalizationService;
import xyz.dma.soft.service.PageInfoService;
import xyz.dma.soft.service.SessionService;

@Controller
public class PageController extends BasePageController {

    public PageController(SessionService sessionService, PageInfoService pageInfoService,
                          LocalizationService localizationService) {
        super(sessionService, pageInfoService, localizationService);
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(Model model) {
        SessionInfo sessionInfo = getCurrentSession();
        if(sessionInfo == null || !sessionInfo.isAuthorized()) {
            putPAgeInfoAttributes(model, "rus", "login");
            return "login";
        }
        return "redirect:/home/";
    }

    @RequestMapping(path = "/home/", method = RequestMethod.GET)
    public String home(Model model) {
        SessionInfo sessionInfo = getCurrentSession();
        if(sessionInfo == null || !sessionInfo.isAuthorized()) {
            putPAgeInfoAttributes(model, "rus", "login");
            return "login";
        }
        return "redirect:/scheduling/";
    }

    @RequestMapping(path = "/{page}/", method = RequestMethod.GET)
    public String page(@PathVariable String page, Model model) {
        SessionInfo sessionInfo = getCurrentSession();
        if(sessionInfo == null || !sessionInfo.isAuthorized()) {
            return "redirect:/";
        }
        putPAgeInfoAttributes(model, sessionInfo.getLanguageIso3(), page);
        return page + "/index";
    }
}
