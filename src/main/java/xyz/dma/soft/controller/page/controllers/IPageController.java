package xyz.dma.soft.controller.page.controllers;

import org.springframework.ui.Model;
import xyz.dma.soft.entity.SessionInfo;

public interface IPageController {
    String handle(Model model, SessionInfo sessionInfo);

    String getName();
}
