package xyz.dma.soft.api.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xyz.dma.soft.api.entity.UserInfo;
import xyz.dma.soft.api.request.StandardRequest;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RegisterRequest extends StandardRequest {
    private String login;
    private String password;
    private UserInfo userInfo;
    private List<String> roles;
}
