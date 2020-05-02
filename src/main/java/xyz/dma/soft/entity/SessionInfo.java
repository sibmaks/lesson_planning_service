package xyz.dma.soft.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionInfo implements Serializable {
    private String id;
    private Long userId;
    private List<String> allowedActions;
    private boolean authorized;
}
