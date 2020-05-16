package xyz.dma.soft.core.constraint;

import java.util.Set;

public interface IConstraintContext {
    default boolean isValid() {
        return getConstraints() == null || getConstraints().isEmpty();
    }

    Set<ConstraintInfo> getConstraints();
}
