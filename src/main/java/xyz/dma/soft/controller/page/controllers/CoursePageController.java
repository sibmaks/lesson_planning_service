package xyz.dma.soft.controller.page.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.CourseService;
import xyz.dma.soft.service.PageInfoService;

@AllArgsConstructor
@Service
public class CoursePageController implements IPageController {
    private final PageInfoService pageInfoService;
    private final CourseService courseService;

    @Override
    public String handle(Model model, SessionInfo sessionInfo) {
        PageInfo pageInfo = pageInfoService.getPreparedPageInfo(model, sessionInfo, getName());
        model.addAttribute("courses", courseService.getAll());
        return pageInfo.getTemplatePath();
    }

    @Override
    public String getName() {
        return "course";
    }
}
