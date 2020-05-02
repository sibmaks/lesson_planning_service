package xyz.dma.soft.core.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import xyz.dma.soft.core.IConstraintContext;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstraintContextImpl implements IConstraintContext {
    private Map<String, String> constraints;

    public void addConstraint(String field, String message) {
        if(constraints == null) {
            constraints = new HashMap<>();
        }
        constraints.put(field, message);
    }
}
