package xyz.dma.soft.controller.page.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.ChildService;
import xyz.dma.soft.service.CourseService;
import xyz.dma.soft.service.PageInfoService;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@Service
public class ChildPageController implements IPageController {
    private final PageInfoService pageInfoService;
    private final ChildService childService;
    private final CourseService courseService;

    @Override
    public String handle(Model model, HttpServletRequest request, SessionInfo sessionInfo) {
        PageInfo pageInfo = pageInfoService.getPreparedPageInfo(model, sessionInfo, getName());
        model.addAttribute("children", childService.getAll());
        model.addAttribute("courses", courseService.getAll());
        return pageInfo.getTemplatePath();
    }

    @Override
    public String getName() {
        return "child";
    }
}
