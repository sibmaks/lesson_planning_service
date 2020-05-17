package xyz.dma.soft.api.validator;

import xyz.dma.soft.api.request.StandardRequest;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.core.constraint.IRequestValidator;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Supplier;

/**
 * Created by maksim.drobyshev on 08-May-20.
 */
public abstract class ARequestValidator<T extends StandardRequest> implements IRequestValidator<T> {

    public Supplier<Boolean> isNull(Object val) {
        return () -> val == null;
    }

    public Supplier<Boolean> isEmpty(String text) {
        return () -> text == null || text.trim().isEmpty();
    }

    public Supplier<Boolean> isLess(String text, int length) {
        return () -> text.length() < length;
    }

    public<L> Supplier<Boolean> isEmpty(List<L> roles) {
        return () -> roles == null || roles.isEmpty();
    }

    public Supplier<Boolean> inRange(int val, int from, int to) {
        return () -> val >= from && val <= to;
    }

    public Supplier<Boolean> not(Supplier<Boolean> supplier) {
        return () -> !supplier.get();
    }

    public Supplier<Boolean> isValidTime(String time, DateTimeFormatter formatter) {
        return () -> {
            try {
                LocalTime.parse(time, formatter);
            } catch (DateTimeParseException e) {
                return false;
            }
            return true;
        };
    }

    public Supplier<Boolean> isValidDate(String date) {
        return () -> {
            try {
                LocalDate.parse(date, ICommonConstants.DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                return false;
            }
            return true;
        };
    }

    public Supplier<Boolean> timeStartBeforeEnd(String start, String end, DateTimeFormatter formatter) {
        return () -> LocalTime.parse(start, formatter).isBefore(LocalTime.parse(end, formatter));
    }

    public Supplier<Boolean> dateStartBeforeEnd(String start, String end) {
        return () -> LocalDate.parse(start, ICommonConstants.DATE_FORMATTER).isBefore(LocalDate.parse(end, ICommonConstants.DATE_FORMATTER));
    }

    public Supplier<Boolean> dateStartBeforeEnd(LocalDate start, String end) {
        return () -> start.isBefore(LocalDate.parse(end, ICommonConstants.DATE_FORMATTER));
    }
}
