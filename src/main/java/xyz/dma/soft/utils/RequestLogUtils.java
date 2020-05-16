package xyz.dma.soft.utils;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class RequestLogUtils {
    private static final Map<String, Pattern> patternsForPayload = new ConcurrentHashMap<>();


    public static String extractPayload(byte[] content, String characterEncoding, String... keysToHide) {
        if (content.length > 0) {
            int length = Math.min(content.length, Integer.MAX_VALUE);
            try {
                String payload = new String(content, 0, length, characterEncoding);
                payload = hideValuesInPayload(payload, keysToHide);
                return payload;
            } catch (UnsupportedEncodingException ex) {
                return "[Can't extract payload]";
            }
        }
        return null;
    }

    /**
     * Локально замерял на реальных данных из ответа:
     * - 1 000 000 вызовов отрабатывает за ~ 17 сек
     * - 1 000 000 вызовов при заранее скомпилированной регулярке отрабатывает за ~ 16 сек
     * <p>
     * Производительность
     * - При парсинге в json работает в 2 раз медленнее на больших обьемах данных и теряется исходное форматирование запроса
     * - Если компилировать регулярку заранее, то производительность увеличивается до нескольких раз
     *
     * @param payload
     * @param keysToHide
     * @return
     */
    private static String hideValuesInPayload(String payload, String... keysToHide) {
        if (keysToHide != null && keysToHide.length > 0) {
            //Надеемся, что это json, минимальная длина 8 {'k':'v'}
            if (payload != null && payload.length() > 8 && payload.trim().startsWith("{")) {
                String keys = String.join("|", keysToHide);
                Pattern pattern = patternsForPayload.get(keys);
                if (pattern == null) {
                    pattern = Pattern.compile("(['\"])(" + keys + ")(['\"]):(\\s?)([\"'])(.*?)([\"'])");
                    patternsForPayload.put(keys, pattern);
                }
                return pattern.matcher(payload).replaceAll("$1$2$3:$4$5XXX$7");
            }
        }
        return payload;
    }
}
