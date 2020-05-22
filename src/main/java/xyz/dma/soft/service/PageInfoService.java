package xyz.dma.soft.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.entity.PageInfos;
import xyz.dma.soft.entity.SessionInfo;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PageInfoService {
    private final Map<String, PageInfo> pagesInfo;
    private final LocalizationService localizationService;

    public PageInfoService(LocalizationService localizationService) {
        this.localizationService = localizationService;
        this.pagesInfo = new HashMap<>();
    }

    @PostConstruct
    public void init() {
       log.info("Starting loading page infos");
        Map<String, PageInfo> abstractPagesInfo = new HashMap<>();
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(PageInfos.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            InputStream inputStream = new ClassPathResource("META-INF/pages.xml").getInputStream();
            PageInfos pageInfos = (PageInfos) jaxbUnmarshaller.unmarshal(inputStream);
            for(PageInfo pageInfo : pageInfos.getPageInfos()) {
                if (pageInfo.isAbstractPage()) {
                    abstractPagesInfo.put(pageInfo.getName(), pageInfo);
                } else {
                    this.pagesInfo.put(pageInfo.getName(), pageInfo);
                }
            }
        } catch (JAXBException | IOException e) {
            throw new RuntimeException("Pages config get error", e);
        }
        for(PageInfo pageInfo : pagesInfo.values()) {
            if(pageInfo.getExtendsPages() != null) {
                for(String parentPage : pageInfo.getExtendsPages()) {
                    PageInfo parentPageInfo = abstractPagesInfo.get(parentPage);
                    if(parentPageInfo == null) {
                        throw new RuntimeException(String.format("Abstract %s page doesn't exists", pageInfo));
                    }
                    if(pageInfo.getCodes() == null) {
                        pageInfo.setCodes(new ArrayList<>());
                    }
                    pageInfo.getCodes().addAll(parentPageInfo.getCodes());
                }
            }
        }
        log.info("Page infos loading finished");
    }

    public PageInfo getPageInfo(String page) {
        return pagesInfo.get(page);
    }

    public PageInfo getPreparedPageInfo(Model model, SessionInfo sessionInfo, String name) {
        PageInfo pageInfo = getPageInfo(name);
        if(pageInfo != null) {
            if (sessionInfo != null ) {
                if(pageInfo.getAllowedAction() != null &&
                        !sessionInfo.getAllowedActions().contains(pageInfo.getAllowedAction())) {
                    return null;
                }
                model.addAttribute("allowedActions", sessionInfo.getAllowedActions());
            }

            String languageIso3 = sessionInfo == null ? "rus" : sessionInfo.getLanguageIso3();
            String countryIso3 = sessionInfo == null ? null : sessionInfo.getCountryIso3();
            model.addAttribute("codes", pageInfo.getCodes());
            model.addAttribute("language", languageIso3);
            model.addAttribute("translations", localizationService.getTranslations(countryIso3,
                    languageIso3, pageInfo.getCodes()));
        }
        return pageInfo;
    }
}
