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
class ConstraintChainListValidator<T> implements IChainConstraintValidator<T> {
    private final IConstraintValidator<?> parent;
    private final List<IChainConstraintValidator<T>> constraintChainValidators;

    @Override
    public IChainConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field) {
        return new ConstraintChainListValidator<>(
                this,
                constraintChainValidators.stream().map(it -> it.validate(validator, field)).collect(toList())
        );
    }

    @Override
    public void onChildFailed() {
        if(parent != null) {
            parent.onChildFailed();
        }
    }

    @Override
    public <K> IChainConstraintValidator<K> map(Function<T, K> converter, String field) {
        return new ConstraintChainListValidator<>(
                this,
                constraintChainValidators.stream().map(it -> it.map(converter, field)).collect(toList())
        );
    }

    @Override
    public <K> IChainConstraintValidator<K> flatMap(Function<T, List<K>> converter, String field) {
        return new ConstraintChainListValidator<>(
                this,
                constraintChainValidators.stream().map(it -> it.flatMap(converter, field)).collect(toList())
        );
    }

    @Override
    public IChainConstraintValidator<T> filter(Function<T, ConstraintType> filter) {
        return new ConstraintChainListValidator<>(
                this,
                constraintChainValidators.stream().map(it -> it.filter(filter)).collect(toList())
        );
    }

    @Override
    public ILineConstraintValidator<T> line() {
        return new ConstraintLineListValidator<>(
                this,
                constraintChainValidators.stream().map(IChainConstraintValidator::line).collect(toList())
        );
    }
}