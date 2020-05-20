package xyz.dma.soft.core.constraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import xyz.dma.soft.entity.ConstraintType;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * Created by maksim.drobyshev on 08-May-20.
 */
public class ConstraintContextBuilder {
    @Getter
    private final Set<ConstraintInfo> constraints;

    public ConstraintContextBuilder() {
        constraints = new HashSet<>();
    }

    private void addConstraint(String field, ConstraintType constraintType) {
        ConstraintInfo constraintInfo = new ConstraintInfo();
        constraintInfo.setParameter(field);
        constraintInfo.setConstraintType(constraintType);
        constraints.add(constraintInfo);
    }

    public<T> IChainConstraintValidator<T> chain(T value) {
        return new ConstraintChainValidator<>(value);
    }

    public<T> ILineConstraintValidator<T> line(T value) {
        return new ConstraintLineValidator<>(value);
    }

    public IConstraintContext build() {
        return new ConstraintContextImpl(constraints);
    }

    @AllArgsConstructor
    private class ConstraintLineListValidator<T> implements ILineConstraintValidator<T> {
        private final IConstraintValidator<T> parent;
        private final List<ILineConstraintValidator<T>> constraintValidators;

        @Override
        public ILineConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field) {
            return new ConstraintLineListValidator<>(
                    this,
                    constraintValidators.stream().map(it -> it.validate(validator, field)).collect(toList())
            );
        }

        @Override
        public void onChildFailed() {
            if (parent != null) {
                parent.onChildFailed();
            }
        }

        @Override
        public IChainConstraintValidator<T> chain() {
            return new ConstraintChainListValidator<>(
                    this,
                    constraintValidators.stream().map(ILineConstraintValidator::chain).collect(toList())
            );
        }
    }

    @AllArgsConstructor
    private class ConstraintChainListValidator<T> implements IChainConstraintValidator<T> {
        private final IConstraintValidator<?> parent;
        private final List<IChainConstraintValidator<T>> constraintChainValidators;

        @Override
        public IChainConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field) {
            return new ConstraintChainListValidator<>(
                    this,
                    constraintChainValidators.stream().map(it -> it.validate(validator, field)).collect(toList())
            );
        }

        @Override
        public void onChildFailed() {
            if(parent != null) {
                parent.onChildFailed();
            }
        }

        @Override
        public <K> IChainConstraintValidator<K> map(Function<T, K> converter, String field) {
            return new ConstraintChainListValidator<>(
                    this,
                    constraintChainValidators.stream().map(it -> it.map(converter, field)).collect(toList())
            );
        }

        @Override
        public <K> IChainConstraintValidator<K> flatMap(Function<T, List<K>> converter, String field) {
            return new ConstraintChainListValidator<>(
                    this,
                    constraintChainValidators.stream().map(it -> it.flatMap(converter, field)).collect(toList())
            );
        }

        @Override
        public IChainConstraintValidator<T> filter(Function<T, ConstraintType> filter) {
            return new ConstraintChainListValidator<>(
                    this,
                    constraintChainValidators.stream().map(it -> it.filter(filter)).collect(toList())
            );
        }

        @Override
        public ILineConstraintValidator<T> line() {
            return new ConstraintLineListValidator<>(
                    this,
                    constraintChainValidators.stream().map(IChainConstraintValidator::line).collect(toList())
            );
        }
    }

    @AllArgsConstructor
    private class ConstraintChainValidator<T> implements IChainConstraintValidator<T> {
        private final IConstraintValidator<?> parent;
        private final T value;
        private final String path;
        private boolean failed;

        public ConstraintChainValidator(T value) {
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
                addConstraint(getPath(path, field), constraintType);
                failed = true;
            }
            return this;
        }

        @Override
        public<K> IChainConstraintValidator<K> map(Function<T, K> transform, String field) {
            if(failed) {
                return new StubChainConstraintValidator<>();
            }
            return new ConstraintChainValidator<>(this, transform.apply(value), getPath(path, field), failed);
        }

        @Override
        public <K> IChainConstraintValidator<K> flatMap(Function<T, List<K>> transform, String field) {
            if(failed) {
                return new StubChainConstraintValidator<>();
            }
            List<IChainConstraintValidator<K>> chainConstraintValidators = new ArrayList<>();
            List<K> values = transform.apply(value);
            for(int i = 0; i < values.size(); i++) {
                chainConstraintValidators.add(new ConstraintChainValidator<>(parent, values.get(i), getPath(path, field) + "[" + i + "]", failed));
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
            return new ConstraintLineValidator<>(this, value, path);
        }

        @Override
        public void onChildFailed() {
            if(parent != null) {
                parent.onChildFailed();
            }
            failed = true;
        }
    }

    @AllArgsConstructor
    private class ConstraintLineValidator<T> implements ILineConstraintValidator<T> {
        private final IConstraintValidator<T> parent;
        private final T value;
        private final String path;

        public ConstraintLineValidator(T value) {
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
            addConstraint(getPath(path, field), constraintType);
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
            return new ConstraintChainValidator<>(parent, value, path, false);
        }
    }

    private static class StubChainConstraintValidator<T> implements IChainConstraintValidator<T> {
        @Override
        public IChainConstraintValidator<T> validate(Function<T, ConstraintType> validator, String field) {
            return this;
        }

        @Override
        public void onChildFailed() {

        }

        @Override
        public <K> IChainConstraintValidator<K> map(Function<T, K> converter, String field) {
            return new StubChainConstraintValidator<>();
        }

        @Override
        public <K> IChainConstraintValidator<K> flatMap(Function<T, List<K>> converter, String field) {
            return new StubChainConstraintValidator<>();
        }

        @Override
        public IChainConstraintValidator<T> filter(Function<T, ConstraintType> filter) {
            return this;
        }

        @Override
        public ILineConstraintValidator<T> line() {
            return new StubLineConstraintValidator<>();
        }
    }

    private static class StubLineConstraintValidator<T> implements ILineConstraintValidator<T> {
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

    private static String getPath(String path, String field) {
        if(path == null && field == null) {
            return "";
        } else if(path == null) {
            return field;
        } else if(field == null) {
            return path;
        } else {
            return path + "." + field;
        }
    }

    @AllArgsConstructor
    private static class ConstraintContextImpl implements IConstraintContext {
        @Getter
        private final Set<ConstraintInfo> constraints;
    }
}