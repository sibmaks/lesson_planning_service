package xyz.dma.soft.controller.page.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import xyz.dma.soft.api.entity.LessonEntity;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.*;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@Service
public class LessonPageController implements IPageController {
    private final PageInfoService pageInfoService;
    private final LessonService lessonService;
    private final SchedulingService schedulingService;
    private final LocalizationService localizationService;

    @Override
    public String handle(Model model, HttpServletRequest request, SessionInfo sessionInfo) {
        String mode = request.getParameter("mode");
        if("update".equals(mode)) {
            return handleUpdate(model, request, sessionInfo);
        }
        return "redirect:/home/";
    }

    public String handleUpdate(Model model, HttpServletRequest request, SessionInfo sessionInfo) {
        Long lessonId = Long.valueOf(request.getParameter("lesson"));
        String lessonDate = request.getParameter("lessonDate");
        LessonEntity lesson = lessonService.get(lessonId);

        PageInfo pageInfo = pageInfoService.getPreparedPageInfo(model, sessionInfo, getName());
        model.addAttribute("page_title", localizationService.getTranslated(sessionInfo, "ui.title.lesson.edit"));
        model.addAttribute("page_header", localizationService.getTranslated(sessionInfo, "ui.header.lesson.edit"));
        model.addAttribute("lesson", lesson);
        model.addAttribute("session_locale", sessionInfo.getLocale());
        model.addAttribute("childrenScheduling", schedulingService.get(
                lesson.getCourseInfo().getId(),
                lesson.getDayOfWeek(),
                lessonDate
        ));
        return pageInfo.getTemplatePath();
    }

    @Override
    public String getName() {
        return "lesson";
    }
}
