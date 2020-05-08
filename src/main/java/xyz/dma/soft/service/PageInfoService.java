package xyz.dma.soft.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.dma.soft.entity.PageInfo;
import xyz.dma.soft.entity.PageInfos;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Service
public class PageInfoService {
    private final Map<String, PageInfo> pagesInfo;
    @Value("classpath:/META-INF/pages.xml")
    private File pagesInfoFile;

    public PageInfoService() {
        this.pagesInfo = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(PageInfos.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            PageInfos pageInfos = (PageInfos) jaxbUnmarshaller.unmarshal(pagesInfoFile);
            for(PageInfo pageInfo : pageInfos.getPageInfos()) {
                this.pagesInfo.put(pageInfo.getName(), pageInfo);
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public PageInfo getPageInfo(String page) {
        return pagesInfo.get(page);
    }
}
