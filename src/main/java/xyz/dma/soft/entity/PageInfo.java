package xyz.dma.soft.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement
public class PageInfo implements Serializable {
    private String name;
    private boolean abstractPage;
    private String allowedAction;
    private List<String> extendsPages;
    private List<String> codes;
    private String templatePath;
}
