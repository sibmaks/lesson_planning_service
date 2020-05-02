package xyz.dma.soft.constants;

import java.time.format.DateTimeFormatter;

public interface ICommonConstants {
    String X_USER_SESSION_ID_HEADER = "X-User-Session-Id";

    String TIME_FORMAT = "H:m:s";
    DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);
}
