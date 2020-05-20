package xyz.dma.soft.core.constraint.impl;

import lombok.AllArgsConstructor;
import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.IConstraintValidator;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;

import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@AllArgsConstructor
class ConstraintLineListValidator<T> implements ILineConstraintValidator<T> {
    private final IConstraintValidator<T> parent;
    private final List<ILineConstraintValidator<T>> constraintValidators;

    @Override
    public ILineConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field) {
        return new ConstraintLineListValidator<>(
                this,
                constraintValidators.stream().map(it -> it.validate(validator, field)).collect(toList())
        );
    }

    @Override
    public void onChildFailed() {
        if (parent != null) {
            parent.onChildFailed();
        }
    }

    @Override
    public IChainConstraintValidator<T> chain() {
        return new ConstraintChainListValidator<>(
                this,
                constraintValidators.stream().map(ILineConstraintValidator::chain).collect(toList())
        );
    }
}