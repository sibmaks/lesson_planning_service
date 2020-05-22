package xyz.dma.soft.controller.page.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.service.PageInfoService;
import xyz.dma.soft.service.RoleService;
import xyz.dma.soft.service.UserService;

import javax.servlet.http.HttpServletRequest;

@AllArgsConstructor
@Service
public class UserPageController implements IPageController {
    private final PageInfoService pageInfoService;
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public String handle(Model model, HttpServletRequest request, SessionInfo sessionInfo) {
        PageInfo pageInfo = pageInfoService.getPreparedPageInfo(model, sessionInfo, getName());
        model.addAttribute("users", userService.getAll(sessionInfo));
        model.addAttribute("roles", roleService.getAll());
        return pageInfo.getTemplatePath();
    }

    @Override
    public String getName() {
        return "user";
    }
}
