package xyz.dma.soft.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import xyz.dma.soft.api.entity.UserInfoEntity;

import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetUsersResponse extends StandardResponse {
    private final List<UserInfoEntity> infoEntities;
}
