package xyz.dma.soft.controller.page;

import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.service.SessionService;

public abstract class BasePageController extends BaseController {
    public BasePageController(SessionService sessionService) {
        super(sessionService);
    }
}
