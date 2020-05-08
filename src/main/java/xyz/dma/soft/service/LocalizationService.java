package xyz.dma.soft.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.dma.soft.domain.i18n.Locale;
import xyz.dma.soft.domain.i18n.Localization;
import xyz.dma.soft.domain.i18n.Translation;
import xyz.dma.soft.api.entity.TranslationEntity;
import xyz.dma.soft.repository.TranslationRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class LocalizationService {
    private final Map<String, Map<String, TranslationEntity>> localizations;
    private final TranslationRepository translationRepository;

    public LocalizationService(TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
        this.localizations = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void init() {
        log.info("Start loading translations");
        List<Translation> translations = translationRepository.findAllBy();
        for(Translation translation : translations) {
            Locale locale = translation.getTranslationId().getLocale();
            Localization localization = translation.getTranslationId().getLocalization();
            if(!localizations.containsKey(localization.getCode())) {
                localizations.put(localization.getCode(), new ConcurrentHashMap<>());
            }
            Map<String, TranslationEntity> translationMap = localizations.get(localization.getCode());
            TranslationEntity translationEntity = TranslationEntity.builder()
                    .code(localization.getCode())
                    .countryISO3(locale.getCountryISO3())
                    .languageISO3(locale.getLanguageISO3())
                    .translation(translation.getTranslation())
                    .build();
            translationMap.put(getLocaleCode(locale), translationEntity);
        }
        log.info("Translations loading finished");
    }

    private static String getLocaleCode(Locale locale) {
        return getLocaleCode(locale.getCountryISO3(), locale.getLanguageISO3());
    }

    private static String getLocaleCode(String countryISO3, String languageISO3) {
        boolean country = countryISO3 != null && !countryISO3.isEmpty();
        boolean language = languageISO3 != null && !languageISO3.isEmpty();
        if(country && language) {
            return String.format("%s_%s", languageISO3, countryISO3);
        } else if(country || language) {
            return language ? languageISO3 : countryISO3;
        } else {
            return "default";
        }
    }

    private TranslationEntity buildNotTranslatedYet(String code) {
        return TranslationEntity.builder()
                .code(code)
                .translation(String.format("** %s - not translated yet **", code))
                .build();
    }

    public Map<String, TranslationEntity> getTranslations(String countryISO3, String languageISO3, List<String> codes) {
        Map<String, TranslationEntity> translationEntityMap = new HashMap<>();
        String localeCode = getLocaleCode(countryISO3, languageISO3);

        for(String code : codes) {
            Map<String, TranslationEntity> translationMap = localizations.get(code);
            if(translationMap == null) {
                translationEntityMap.put(code, buildNotTranslatedYet(code));
                continue;
            }
            TranslationEntity translationEntity = translationMap.get(localeCode);
            if(translationEntity != null) {
                translationEntityMap.put(code, translationEntity);
                continue;
            }
            if(languageISO3 != null) {
                translationEntity = translationMap.get(languageISO3);
                if(translationEntity != null) {
                    translationEntityMap.put(code, translationEntity);
                    continue;
                }
            }
            translationEntity = translationMap.get("default");
            if(translationEntity == null) {
                translationEntityMap.put(code, buildNotTranslatedYet(code));
            } else {
                translationEntityMap.put(code, translationEntity);
            }
        }

        return translationEntityMap;
    }
}
