package xyz.dma.soft.api.response.localization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.TranslationEntity;
import xyz.dma.soft.api.response.StandardResponse;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TranslationsGetResponse extends StandardResponse {
    private Map<String, TranslationEntity> translations;
}
