package xyz.dma.soft.core.constraint;

import xyz.dma.soft.entity.ConstraintType;

import java.util.function.Function;

public interface IConstraintValidator<T> {
    IConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field);

    default IConstraintValidator<T> validate(Function<T, ConstraintType> validator) {
        return validate(validator, null);
    }

    void onChildFailed();
}
