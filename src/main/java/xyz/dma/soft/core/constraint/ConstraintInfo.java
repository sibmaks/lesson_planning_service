package xyz.dma.soft.core.constraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.entity.ConstraintType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConstraintInfo {
    private String parameter;
    private ConstraintType constraintType;
}
