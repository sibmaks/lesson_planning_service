package xyz.dma.soft.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionInfo implements Serializable {
    public static String ATTR_LANGUAGE_ISO3 = "ATTR_LANGUAGE_ISO3";
    public static String ATTR_COUNTRY_ISO3 = "ATTR_COUNTRY_ISO3";

    private String id;
    private Long userId;
    private List<String> allowedActions;
    private Map<String, String> attributes;
    private boolean authorized;

    public String getLanguageIso3() {
        return getAttribute(ATTR_LANGUAGE_ISO3);
    }

    public String getCountryIso3() {
        return getAttribute(ATTR_COUNTRY_ISO3);
    }

    public String getAttribute(String key) {
        Map<String, String> attributes = this.attributes;
        return attributes == null ? null : attributes.get(key);
    }
}
