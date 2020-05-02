package xyz.dma.soft.core;

import java.util.Map;

public interface IConstraintContext {
    default boolean isValid() {
        return getConstraints() == null || getConstraints().isEmpty();
    }

    Map<String, String> getConstraints();
}
