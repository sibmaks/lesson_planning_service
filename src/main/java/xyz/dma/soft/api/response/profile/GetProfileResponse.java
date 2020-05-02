package xyz.dma.soft.api.response.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.UserInfo;
import xyz.dma.soft.api.response.StandardResponse;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetProfileResponse extends StandardResponse {
    private UserInfo userInfo;
}
