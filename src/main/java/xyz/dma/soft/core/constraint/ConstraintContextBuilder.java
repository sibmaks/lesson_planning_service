package xyz.dma.soft.core.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.dma.soft.entity.ConstraintType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Created by maksim.drobyshev on 08-May-20.
 */
public class ConstraintContextBuilder {
    @Getter
    private final Set<ConstraintInfo> constraints;
    private boolean failed;
    private int failedIndex;

    public ConstraintContextBuilder() {
        constraints = new HashSet<>();
    }

    private void addConstraint(String field, ConstraintType constraintType) {
        ConstraintInfo constraintInfo = new ConstraintInfo();
        constraintInfo.setParameter(field);
        constraintInfo.setConstraintType(constraintType);
        constraints.add(constraintInfo);
    }

    public ConstraintContextBuilder assertConstraintViolation(Supplier<Boolean> condition,
                                                              ConstraintType constraintType, String... path) {
        return assertConstraintViolation(0, condition, constraintType, path);
    }

    public ConstraintContextBuilder assertConstraintViolation(int index, Supplier<Boolean> condition,
                                                              ConstraintType constraintType, String... path) {
        if ((!failed || index == failedIndex) && condition.get()) {
            String fieldPath = String.join(".", Arrays.asList(path));
            addConstraint(fieldPath, constraintType);
            failed = true;
            failedIndex = index;
        }
        return this;
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