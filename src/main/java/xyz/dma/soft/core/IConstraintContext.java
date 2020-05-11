package xyz.dma.soft.core;

import xyz.dma.soft.entity.ConstraintType;

import java.util.Map;

public interface IConstraintContext {
    default boolean isValid() {
        return getConstraints() == null || getConstraints().isEmpty();
    }

    Map<String, ConstraintType> getConstraints();
}
