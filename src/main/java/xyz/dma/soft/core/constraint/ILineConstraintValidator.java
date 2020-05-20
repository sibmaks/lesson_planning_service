package xyz.dma.soft.core.constraint;

import xyz.dma.soft.entity.ConstraintType;

import java.util.function.Function;

public interface ILineConstraintValidator<T> extends IConstraintValidator<T> {

    ILineConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field);

    default ILineConstraintValidator<T> validate(Function<T, ConstraintType> validator) {
        return validate(validator, null);
    }

    IChainConstraintValidator<T> chain();

}
