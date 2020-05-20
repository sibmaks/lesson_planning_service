package xyz.dma.soft.core.constraint;

import xyz.dma.soft.entity.ConstraintType;

import java.util.List;
import java.util.function.Function;

public interface IChainConstraintValidator<T> extends IConstraintValidator<T> {

    default IChainConstraintValidator<T> validate(Function<T, ConstraintType> validator) {
        return validate(validator, null);
    }

    IChainConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field);

    <K> IChainConstraintValidator<K> map(Function<T, K> converter, String field);

    <K> IChainConstraintValidator<K> flatMap(Function<T, List<K>> converter, String field);

    IChainConstraintValidator<T> filter(Function<T, ConstraintType> filter);

    ILineConstraintValidator<T> line();
}
