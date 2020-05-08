package xyz.dma.soft.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TranslationEntity implements Serializable {
    public String code;
    private String countryISO3;
    private String languageISO3;
    public String translation;
}
