package xyz.dma.soft.controller.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import xyz.dma.soft.api.request.localization.TranslationsGetRequest;
import xyz.dma.soft.api.response.StandardResponse;
import xyz.dma.soft.api.response.localization.TranslationsGetResponse;
import xyz.dma.soft.controller.BaseController;
import xyz.dma.soft.service.LocalizationService;
import xyz.dma.soft.service.SessionService;

@RestController
@RequestMapping(path = "/v3/translation/", consumes = {MediaType.APPLICATION_JSON_VALUE})
public class TranslationsController extends BaseController {
    private final LocalizationService localizationService;

    public TranslationsController(SessionService sessionService, LocalizationService localizationService) {
        super(sessionService);
        this.localizationService = localizationService;
    }

    @RequestMapping(path = "get", method = RequestMethod.POST)
    public StandardResponse get(@RequestBody TranslationsGetRequest request) {
        return new TranslationsGetResponse(localizationService.getTranslations(request.getCountryIso3(),
                request.getLanguageIso3(), request.getCodes()));
    }
}
