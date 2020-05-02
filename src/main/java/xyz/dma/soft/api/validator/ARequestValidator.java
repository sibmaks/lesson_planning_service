package xyz.dma.soft.api.validator;

import xyz.dma.soft.api.request.StandardRequest;
import xyz.dma.soft.constants.ICommonConstants;
import xyz.dma.soft.core.IRequestValidator;
import xyz.dma.soft.core.impl.ConstraintContextImpl;

import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public abstract class ARequestValidator<T extends StandardRequest> implements IRequestValidator<T> {
    public boolean addConstraint(ConstraintContextImpl context, boolean condition, String comment, String ... path) {
        if(condition) {
            String fieldPath = String.join(".", Arrays.asList(path));
            context.addConstraint(fieldPath, comment);
        }
        return condition;
    }

    public boolean isEmpty(String text) {
        return text == null || text.trim().isEmpty();
    }

    public boolean isLess(String text, int length) {
        return text.length() < length;
    }


    public<L> boolean isEmpty(List<L> roles) {
        return roles == null || roles.isEmpty();
    }

    protected ChainRequestValidator chainConstraint(ConstraintContextImpl constraintContext) {
        return new ChainRequestValidator(constraintContext);
    }

    protected static class ChainRequestValidator {
        private final ConstraintContextImpl context;
        private boolean failed;
        private int failedIndex;

        public ChainRequestValidator(ConstraintContextImpl context) {
            this.context = context;
        }

        public ChainRequestValidator addConstraint(Supplier<Boolean> condition, String comment, String ... path) {
            return addConstraint(0, condition, comment, path);
        }

        public ChainRequestValidator addConstraint(int index, Supplier<Boolean> condition, String comment, String ... path) {
            if((!failed || index == failedIndex) && condition.get()) {
                String fieldPath = String.join(".", Arrays.asList(path));
                context.addConstraint(fieldPath, comment);
                failed = true;
                failedIndex = index;
            }
            return this;
        }
    }

    public boolean inRange(int val, int from, int to) {
        return val >= from && val <= to;
    }

    public boolean isValidTime(String time) {
        try {
            LocalTime.parse(time, ICommonConstants.TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public boolean startBeforeEnd(String start, String end) {
        return LocalTime.parse(start, ICommonConstants.TIME_FORMATTER).isBefore(LocalTime.parse(end, ICommonConstants.TIME_FORMATTER));
    }
}
