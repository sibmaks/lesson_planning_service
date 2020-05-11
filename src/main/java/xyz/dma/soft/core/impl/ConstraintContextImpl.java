package xyz.dma.soft.core.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.core.IConstraintContext;
import xyz.dma.soft.entity.ConstraintType;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstraintContextImpl implements IConstraintContext {
    private Map<String, ConstraintType> constraints;

    public void addConstraint(String field, ConstraintType type) {
        if(constraints == null) {
            constraints = new HashMap<>();
        }
        constraints.put(field, type);
    }
}
