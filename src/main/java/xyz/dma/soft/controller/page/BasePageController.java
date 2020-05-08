package xyz.dma.soft.controller.page;

import org.springframework.ui.Model;
import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.service.LocalizationService;
import xyz.dma.soft.service.PageInfoService;
import xyz.dma.soft.service.SessionService;

public abstract class BasePageController extends BaseController {
    private final PageInfoService pageInfoService;
    private final LocalizationService localizationService;

    public BasePageController(SessionService sessionService, PageInfoService pageInfoService,
                              LocalizationService localizationService) {
        super(sessionService);
        this.pageInfoService = pageInfoService;
        this.localizationService = localizationService;
    }

    public void putPAgeInfoAttributes(Model model, String languageIso3, String name) {
        PageInfo pageInfo = pageInfoService.getPageInfo(name);

        model.addAttribute("codes", pageInfo.getCodes());
        model.addAttribute("language", languageIso3);
        model.addAttribute("translations", localizationService.getTranslations(null,
                languageIso3, pageInfo.getCodes()));
    }
}
