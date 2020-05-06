package xyz.dma.soft.api.response.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.RoleInfo;
import xyz.dma.soft.api.entity.UserInfoEntity;
import xyz.dma.soft.api.response.StandardResponse;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoginResponse extends StandardResponse {
    private UserInfoEntity userInfo;
    private List<RoleInfo> roleInfos;
}
