package xyz.dma.soft.core.constraint.impl;

import lombok.AllArgsConstructor;
import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.IConstraintValidator;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static xyz.dma.soft.core.constraint.impl.ValidatorUtils.getPath;

@AllArgsConstructor
class ConstraintChainValidator<T> implements IChainConstraintValidator<T> {
    private final ConstraintContextBuilder builder;
    private final IConstraintValidator<?> parent;
    private final T value;
    private final String path;
    private boolean failed;

    public ConstraintChainValidator(ConstraintContextBuilder builder, T value) {
        this.builder = builder;
        this.parent = null;
        this.value = value;
        this.path = null;
    }

    @Override
    public IChainConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field) {
        if (!failed) {
            ConstraintType constraintType = validator.apply(value);
            if(constraintType == null) {
                return this;
            }
            if(parent != null) {
                parent.onChildFailed();
            }
            builder.addConstraint(getPath(path, field), constraintType);
            failed = true;
        }
        return this;
    }

    @Override
    public<K> IChainConstraintValidator<K> map(Function<T, K> transform, String field) {
        if(failed) {
            return new StubChainConstraintValidator<>();
        }
        return new ConstraintChainValidator<>(builder, this, transform.apply(value), getPath(path, field), failed);
    }

    @Override
    public <K> IChainConstraintValidator<K> flatMap(Function<T, List<K>> transform, String field) {
        if(failed) {
            return new StubChainConstraintValidator<>();
        }
        List<IChainConstraintValidator<K>> chainConstraintValidators = new ArrayList<>();
        List<K> values = transform.apply(value);
        for(int i = 0; i < values.size(); i++) {
            chainConstraintValidators.add(new ConstraintChainValidator<>(builder, parent, values.get(i), getPath(path, field) + "[" + i + "]", failed));
        }
        return new ConstraintChainListValidator<>(this, chainConstraintValidators);
    }

    @Override
    public IChainConstraintValidator<T> filter(Function<T, ConstraintType> filter) {
        if(filter.apply(value) == null) {
            return this;
        }
        return new StubChainConstraintValidator<>();
    }

    @Override
    public ILineConstraintValidator<T> line() {
        if(failed) {
            return new StubLineConstraintValidator<>();
        }
        return new ConstraintLineValidator<>(builder, this, value, path);
    }

    @Override
    public void onChildFailed() {
        if(parent != null) {
            parent.onChildFailed();
        }
        failed = true;
    }
}