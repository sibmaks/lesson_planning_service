package xyz.dma.soft.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleInfo implements Serializable {
    private String name;
    private String description;
    private List<String> allowedActions;
}
