package xyz.dma.soft.core.constraint.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.dma.soft.core.constraint.ConstraintInfo;
import xyz.dma.soft.core.constraint.IChainConstraintValidator;
import xyz.dma.soft.core.constraint.IConstraintContext;
import xyz.dma.soft.core.constraint.ILineConstraintValidator;
import xyz.dma.soft.entity.ConstraintType;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by maksim.drobyshev on 08-May-20.
 */
public class ConstraintContextBuilder {
    @Getter
    private final Set<ConstraintInfo> constraints;

    public ConstraintContextBuilder() {
        constraints = new HashSet<>();
    }

    void addConstraint(String field, ConstraintType constraintType) {
        ConstraintInfo constraintInfo = new ConstraintInfo();
        constraintInfo.setParameter(field);
        constraintInfo.setConstraintType(constraintType);
        constraints.add(constraintInfo);
    }

    public<T> IChainConstraintValidator<T> chain(T value) {
        return new ConstraintChainValidator<>(this, value);
    }

    public<T> ILineConstraintValidator<T> line(T value) {
        return new ConstraintLineValidator<>(this, value);
    }

    public IConstraintContext build() {
        return new ConstraintContextImpl(constraints);
    }

    @AllArgsConstructor
    private static class ConstraintContextImpl implements IConstraintContext {
        @Getter
        private final Set<ConstraintInfo> constraints;
    }
}