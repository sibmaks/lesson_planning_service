package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.ApiResultCode;
import xyz.dma.soft.api.entity.RoleInfo;
import xyz.dma.soft.domain.user.User;
import xyz.dma.soft.domain.user.UserAction;
import xyz.dma.soft.domain.user.UserRole;
import xyz.dma.soft.exception.ServiceException;
import xyz.dma.soft.repository.UserActionRepository;
import xyz.dma.soft.repository.UserRepository;
import xyz.dma.soft.repository.UserRoleRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class RoleService {
    private final UserActionRepository userActionRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;

    @Transactional
    public void modify(String roleName, List<String> allowedActions) {
        UserRole userRole = userRoleRepository.findFirstByName(roleName);
        if(userRole == null) {
            throw ServiceException.builder().code(ApiResultCode.NOT_FOUND).build();
        }
        List<UserAction> actions = new ArrayList<>();
        if(allowedActions != null) {
            for (String action : allowedActions) {
                actions.add(userActionRepository.findFirstByName(action));
            }
        }
        userRole.setAllowedActions(actions);
        userRoleRepository.save(userRole);
    }

    @Transactional
    public void grant(String roleName, Long userId) {
        User user = userRepository.findFirstById(userId);
        if(user == null) {
            throw ServiceException.builder().code(ApiResultCode.USER_NOT_FOUND).build();
        }
        UserRole userRole = userRoleRepository.findFirstByName(roleName);
        if(userRole == null) {
            throw ServiceException.builder().code(ApiResultCode.ROLE_NOT_FOUND).build();
        }
        if(user.getUserRoles() == null) {
            user.setUserRoles(new ArrayList<>());
        }
        for(UserRole role : user.getUserRoles()) {
            if(role.getId() == userRole.getId()) {
                throw ServiceException.builder().code(ApiResultCode.ROLE_ALREADY_GRANTED).build();
            }
        }
        user.getUserRoles().add(userRole);
        userRepository.save(user);
    }

    public List<RoleInfo> getAll() {
        return buildRoleInfos(userRoleRepository.findAll());
    }

    public List<RoleInfo> getRoles(Long userId) {
        User user = userRepository.findFirstById(userId);
        return buildRoleInfos(user.getUserRoles());
    }

    public static List<RoleInfo> buildRoleInfos(Iterable<UserRole> roles) {
        List<RoleInfo> roleInfos = new ArrayList<>();
        if(roles != null) {
            for (UserRole userRole : roles) {
                RoleInfo roleInfo = RoleInfo.builder()
                        .name(userRole.getName())
                        .description(userRole.getDescription())
                        .allowedActions(userRole.getAllowedActions() == null ? emptyList() :
                                userRole.getAllowedActions().stream().map(UserAction::getName).collect(toList()))
                        .build();
                roleInfos.add(roleInfo);
            }
        }
        return roleInfos;
    }

    @Transactional
    public void retract(String roleName, Long userId) {
        User user = userRepository.findFirstById(userId);
        if(user == null) {
            throw ServiceException.builder().code(ApiResultCode.USER_NOT_FOUND).build();
        }
        UserRole userRole = userRoleRepository.findFirstByName(roleName);
        if(userRole == null) {
            throw ServiceException.builder().code(ApiResultCode.ROLE_NOT_FOUND).build();
        }
        if(user.getUserRoles() != null) {
            for (UserRole role : user.getUserRoles()) {
                if (role.getId() == userRole.getId()) {
                    user.getUserRoles().remove(role);
                    break;
                }
            }
        }
        userRepository.save(user);
    }
}
