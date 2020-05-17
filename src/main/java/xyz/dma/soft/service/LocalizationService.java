package xyz.dma.soft.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.TranslationEntity;
import xyz.dma.soft.domain.i18n.Locale;
import xyz.dma.soft.domain.i18n.Localization;
import xyz.dma.soft.domain.i18n.Translation;
import xyz.dma.soft.entity.SessionInfo;
import xyz.dma.soft.repository.LocaleRepository;
import xyz.dma.soft.repository.TranslationRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class LocalizationService {
    private final Map<String, Map<String, TranslationEntity>> translations;
    private final Map<String, Locale> localizations;
    private final TranslationRepository translationRepository;

    public LocalizationService(TranslationRepository translationRepository, LocaleRepository localeRepository) {
        this.translationRepository = translationRepository;
        this.localizations = new HashMap<>();
        localeRepository.findAllBy()
                .forEach(it -> this.localizations.put(getLocaleCode(it.getCountryIso3(), it.getLanguageIso3()), it));
        this.translations = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void init() {
        log.info("Start loading translations");
        List<Translation> translations = translationRepository.findAllBy();
        for(Translation translation : translations) {
            Locale locale = translation.getTranslationId().getLocale();
            Localization localization = translation.getTranslationId().getLocalization();
            if(!this.translations.containsKey(localization.getCode())) {
                this.translations.put(localization.getCode(), new ConcurrentHashMap<>());
            }
            Map<String, TranslationEntity> translationMap = this.translations.get(localization.getCode());
            TranslationEntity translationEntity = TranslationEntity.builder()
                    .code(localization.getCode())
                    .countryIso3(locale.getCountryIso3())
                    .languageIso3(locale.getLanguageIso3())
                    .translation(translation.getTranslation())
                    .build();
            translationMap.put(getLocaleCode(locale), translationEntity);
        }
        log.info("Translations loading finished");
    }

    private static String getLocaleCode(Locale locale) {
        return getLocaleCode(locale.getCountryIso3(), locale.getLanguageIso3());
    }

    private static String getLocaleCode(String countryIso3, String languageIso3) {
        boolean country = countryIso3 != null && !countryIso3.isEmpty();
        boolean language = languageIso3 != null && !languageIso3.isEmpty();
        if(country && language) {
            return String.format("%s_%s", languageIso3, countryIso3);
        } else if(country || language) {
            return language ? languageIso3 : countryIso3;
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

    public TranslationEntity getTranslation(String countryIso3, String languageIso3, String code) {
        Map<String, TranslationEntity> translationEntityMap = getTranslations(countryIso3, languageIso3, Arrays.asList(code));
        return translationEntityMap.get(code);
    }

    public String getTranslated(String countryIso3, String languageIso3, String code) {
        TranslationEntity translationEntity = getTranslation(countryIso3, languageIso3, code);
        return translationEntity == null ? null : translationEntity.getTranslation();
    }

    public String getTranslated(SessionInfo sessionInfo, String code) {
        TranslationEntity translationEntity = getTranslation(sessionInfo.getCountryIso3(), sessionInfo.getLanguageIso3(), code);
        return translationEntity == null ? null : translationEntity.getTranslation();
    }

    public String getTranslated(String languageIso3, String code) {
        TranslationEntity translationEntity = getTranslation(null, languageIso3, code);
        return translationEntity == null ? null : translationEntity.getTranslation();
    }

    public Locale getLocale(String countryIso3, String languageIso3) {
        String code = getLocaleCode(countryIso3, languageIso3);
        if(!localizations.containsKey(code)) {
            return localizations.get(languageIso3);
        }
        return localizations.get(code);
    }

    public Map<String, TranslationEntity> getTranslations(String countryIso3, String languageIso3, List<String> codes) {
        Map<String, TranslationEntity> translationEntityMap = new HashMap<>();
        String localeCode = getLocaleCode(countryIso3, languageIso3);

        for(String code : codes) {
            Map<String, TranslationEntity> translationMap = translations.get(code);
            if(translationMap == null) {
                translationEntityMap.put(code, buildNotTranslatedYet(code));
                continue;
            }
            TranslationEntity translationEntity = translationMap.get(localeCode);
            if(translationEntity != null) {
                translationEntityMap.put(code, translationEntity);
                continue;
            }
            if(languageIso3 != null) {
                translationEntity = translationMap.get(languageIso3);
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
