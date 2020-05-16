package xyz.dma.soft.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.core.constraint.IConstraintContext;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConstraintException extends RuntimeException {
    private IConstraintContext constraintContext;
}
