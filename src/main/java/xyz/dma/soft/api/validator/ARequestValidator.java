package xyz.dma.soft.api.validator;

import xyz.dma.soft.api.request.StandardRequest;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.core.constraint.IRequestValidator;
import xyz.dma.soft.entity.ConstraintType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Created by maksim.drobyshev on 08-May-20.
 */
public abstract class ARequestValidator<T extends StandardRequest> implements IRequestValidator<T> {

    public ConstraintType notNull(Object val) {
        return val == null ? ConstraintType.EMPTY : null;
    }

    public ConstraintType notEmpty(String text) {
        return text == null || text.trim().isEmpty() ? ConstraintType.EMPTY : null;
    }

    public <L> ConstraintType notEmpty(List<L> roles) {
        return roles == null || roles.isEmpty() ? ConstraintType.EMPTY : null;
    }

   public ConstraintType isMore(String text, int length) {
        return text.length() < length ? ConstraintType.SHORT : null;
    }

    public ConstraintType isValidDate(String date) {
        try {
            LocalDate.parse(date, ICommonConstants.DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return ConstraintType.INVALID;
        }
        return null;
    }

    public ConstraintType dateStartBeforeEnd(String start, String end) {
        return dateStartBeforeEnd(LocalDate.parse(start, ICommonConstants.DATE_FORMATTER),
                LocalDate.parse(end, ICommonConstants.DATE_FORMATTER));
    }

    public ConstraintType dateStartBeforeEnd(LocalDate start, LocalDate end) {
        return start.isBefore(end) ? null : ConstraintType.INVALID;
    }

    public ConstraintType isValidTime(String time) {
        try {
            LocalTime.parse(time, ICommonConstants.TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return ConstraintType.INVALID;
        }
        return null;
    }

    public ConstraintType timeStartBeforeEnd(String start, String end) {
        return LocalTime.parse(start, ICommonConstants.TIME_FORMATTER)
                .isBefore(LocalTime.parse(end, ICommonConstants.TIME_FORMATTER)) ? null : ConstraintType.INVALID;
    }

    public ConstraintType inRange(int val, int from, int to) {
        return val >= from && val <= to ? null : ConstraintType.INVALID;
    }
}
