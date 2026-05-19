package xyz.dma.soft.domain.i18n;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TranslationId implements Serializable {
    @ManyToOne
    private Locale locale;
    @ManyToOne
    private Localization localization;
}
