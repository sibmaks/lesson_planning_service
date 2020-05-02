package xyz.dma.soft.api.request.role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.request.StandardRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GrantRoleRequest extends StandardRequest {
    private String role;
    private Long userId;
}
