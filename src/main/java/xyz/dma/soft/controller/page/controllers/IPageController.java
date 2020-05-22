package xyz.dma.soft.controller.page.controllers;

import org.springframework.ui.Model;
import xyz.dma.soft.entity.SessionInfo;

import javax.servlet.http.HttpServletRequest;

public interface IPageController {
    String handle(Model model, HttpServletRequest request, SessionInfo sessionInfo);

    String getName();
}
