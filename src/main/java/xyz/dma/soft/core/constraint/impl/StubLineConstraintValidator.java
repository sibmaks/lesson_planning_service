package xyz.dma.soft.core.constraint.impl;

import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;

import java.util.function.Function;

class StubLineConstraintValidator<T> implements ILineConstraintValidator<T> {
    @Override
    public ILineConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field) {
        return this;
    }

    @Override
    public void onChildFailed() {

    }

    @Override
    public IChainConstraintValidator<T> chain() {
        return new StubChainConstraintValidator<>();
    }
}