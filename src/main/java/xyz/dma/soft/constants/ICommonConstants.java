package xyz.dma.soft.constants;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public interface ICommonConstants {
    String X_USER_SESSION_ID_HEADER = "X-User-Session-Id";

    String TIME_FORMAT = "HH:mm";
    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    String DATE_FORMAT = "dd-MM-yyyy";
    DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";
    DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

    List<String> LANGUAGES = Arrays.asList("rus", "eng");
}
