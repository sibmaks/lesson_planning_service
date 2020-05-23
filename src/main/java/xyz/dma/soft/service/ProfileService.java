package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.UserInfoEntity;
import xyz.dma.soft.domain.user.UserInfo;
import xyz.dma.soft.repository.UserInfoRepository;
import xyz.dma.soft.repository.UserRepository;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public UserInfoEntity getUserInfo(Long userId) {
        UserInfo userInfo = userInfoRepository.findFirstById(userId);
        return new UserInfoEntity(userId, userInfo);
    }

    @Transactional
    public void changeProfile(Long userId, UserInfoEntity newUserInfoEntity) {
        UserInfo userInfo = userInfoRepository.findFirstById(userId);
        if(userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUser(userRepository.findFirstById(userId));
        }
        if(newUserInfoEntity != null) {
            String firstName = newUserInfoEntity.getFirstName();
            String lastName = newUserInfoEntity.getLastName();
            if (firstName != null) {
                userInfo.setFirstName(firstName.trim().isEmpty() ? null : firstName);
            }
            if (lastName != null) {
                userInfo.setLastName(lastName.trim().isEmpty() ? null : lastName);
            }
        }
        userInfoRepository.save(userInfo);
    }
}
