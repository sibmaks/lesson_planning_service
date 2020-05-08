package xyz.dma.soft.api.request.localization;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.request.StandardRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TranslationsGetRequest extends StandardRequest {
    private String countryIso3;
    private String languageIso3;
    private List<String> codes;
}
