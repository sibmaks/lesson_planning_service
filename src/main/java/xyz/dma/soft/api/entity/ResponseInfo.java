package xyz.dma.soft.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@JsonIgnoreProperties(value = {"apiResultCode"})
public class ResponseInfo implements Serializable {
    @Getter
    private String resultCode;
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private String systemMessage;

    @Builder
    public ResponseInfo(ApiResultCode resultCode, String message, String systemMessage) {
        this.resultCode = resultCode.code;
        this.message = message;
        this.systemMessage = systemMessage;
    }

    public ApiResultCode getApiResultCode() {
        return ApiResultCode.get(resultCode);
    }

    public void setResultCode(ApiResultCode apiResultCode) {
        this.resultCode = apiResultCode.code;
    }
}
