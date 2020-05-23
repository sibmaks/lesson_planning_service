package xyz.dma.soft.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.response.StandardResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SetBlockResponse extends StandardResponse {
    private Long userId;
    private Boolean blocked;
}
