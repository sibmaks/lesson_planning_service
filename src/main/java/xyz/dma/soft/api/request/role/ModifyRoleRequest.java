package xyz.dma.soft.api.request.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.request.StandardRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModifyRoleRequest extends StandardRequest {
    private String role;
    private List<String> allowedActions;
}
