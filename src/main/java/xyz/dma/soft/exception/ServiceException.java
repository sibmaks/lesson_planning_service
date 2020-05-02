package xyz.dma.soft.exception;

import lombok.*;
import xyz.dma.soft.api.entity.ApiResultCode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceException extends RuntimeException {
    private ApiResultCode code;
    private String message;
    private String systemMessage;
}
