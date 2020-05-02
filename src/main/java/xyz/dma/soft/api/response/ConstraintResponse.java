package xyz.dma.soft.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.dma.soft.api.entity.ResponseInfo;

import java.util.Map;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConstraintResponse extends StandardResponse {
    private Map<String, String> constrains;

    public ConstraintResponse(ResponseInfo responseInfo, Map<String, String> constrains) {
        super(responseInfo);
        this.constrains = constrains;
    }
}
