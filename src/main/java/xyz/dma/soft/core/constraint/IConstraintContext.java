package xyz.dma.soft.core.constraint;

import java.util.Set;

public interface IConstraintContext {
    String getLanguageIso3();

    default boolean isValid() {
        return getConstraints() == null || getConstraints().isEmpty();
    }

    Set<ConstraintInfo> getConstraints();
}
