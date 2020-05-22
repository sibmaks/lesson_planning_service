package xyz.dma.soft.controller.page.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.PageInfoService;
import xyz.dma.soft.service.ProfileService;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@Service
public class ProfilePageController implements IPageController {
    private final PageInfoService pageInfoService;
    private final ProfileService profileService;

    @Override
    public String handle(Model model, HttpServletRequest request, SessionInfo sessionInfo) {
        PageInfo pageInfo = pageInfoService.getPreparedPageInfo(model, sessionInfo, getName());
        model.addAttribute("userInfo", profileService.getUserInfo(sessionInfo.getUserId()));
        return pageInfo.getTemplatePath();
    }

    @Override
    public String getName() {
        return "profile";
    }
}
