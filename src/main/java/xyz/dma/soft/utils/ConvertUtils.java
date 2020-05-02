package xyz.dma.soft.utils;

import xyz.dma.soft.constants.ICommonConstants;

import java.time.LocalTime;

public class ConvertUtils {
    public static LocalTime parseTime(String time) {
        return LocalTime.parse(time, ICommonConstants.TIME_FORMATTER);
    }
}
