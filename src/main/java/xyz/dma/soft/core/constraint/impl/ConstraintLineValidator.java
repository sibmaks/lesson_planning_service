package xyz.dma.soft.core.constraint.impl;

import lombok.AllArgsConstructor;
import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.IConstraintValidator;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;

import java.util.function.Function;

import static xyz.dma.soft.core.constraint.impl.ValidatorUtils.getPath;

@AllArgsConstructor
class ConstraintLineValidator<T> implements ILineConstraintValidator<T> {
    private final ConstraintContextBuilder builder;
    private final IConstraintValidator<T> parent;
    private final T value;
    private final String path;

    public ConstraintLineValidator(ConstraintContextBuilder builder, T value) {
        this.builder = builder;
        this.parent = null;
        this.value = value;
        this.path = null;
    }

    @Override
    public ILineConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field) {
        ConstraintType constraintType = validator.apply(value);
        if (constraintType == null) {
            return this;
        }
        if(parent != null) {
            parent.onChildFailed();
        }
        builder.addConstraint(getPath(path, field), constraintType);
        return this;
    }

    @Override
    public void onChildFailed() {
        if(parent != null) {
            parent.onChildFailed();
        }
    }

    @Override
    public IChainConstraintValidator<T> chain() {
        return new ConstraintChainValidator<>(builder, parent, value, path, false);
    }
}