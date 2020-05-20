package xyz.dma.soft.core.constraint.impl;

import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;

import java.util.List;
import java.util.function.Function;

class StubChainConstraintValidator<T> implements IChainConstraintValidator<T> {
    @Override
    public IChainConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field) {
        return this;
    }

    @Override
    public void onChildFailed() {

    }

    @Override
    public <K> IChainConstraintValidator<K> map(Function<T, K> converter, String field) {
        return new StubChainConstraintValidator<>();
    }

    @Override
    public <K> IChainConstraintValidator<K> flatMap(Function<T, List<K>> converter, String field) {
        return new StubChainConstraintValidator<>();
    }

    @Override
    public IChainConstraintValidator<T> filter(Function<T, ConstraintType> filter) {
        return this;
    }

    @Override
    public ILineConstraintValidator<T> line() {
        return new StubLineConstraintValidator<>();
    }
}