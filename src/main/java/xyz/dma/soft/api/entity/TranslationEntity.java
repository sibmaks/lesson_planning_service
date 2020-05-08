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
    private String countryIso3;
    private String languageIso3;
    public String translation;
}
