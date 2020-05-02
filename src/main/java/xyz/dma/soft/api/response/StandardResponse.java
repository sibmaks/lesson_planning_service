package xyz.dma.soft.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.entity.ResponseInfo;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class StandardResponse implements Serializable {
    private ResponseInfo responseInfo;

    public StandardResponse() {
        responseInfo = ResponseInfo.builder().resultCode(ApiResultCode.OK).build();
    }
}
