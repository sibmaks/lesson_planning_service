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
    private final String languageIso3;
    @Getter
    private final Set<ConstraintInfo> constraints;

    public ConstraintContextBuilder(String languageIso3) {
        this.languageIso3 = languageIso3;
        this.constraints = new HashSet<>();
    }

    public ConstraintContextBuilder() {
        this(null);
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
        return new ConstraintContextImpl(languageIso3, constraints);
    }

    @AllArgsConstructor
    private static class ConstraintContextImpl implements IConstraintContext {
        @Getter
        private final String languageIso3;
        @Getter
        private final Set<ConstraintInfo> constraints;
    }
}