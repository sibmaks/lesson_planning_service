package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.dma.soft.api.entity.UserInfo;
import xyz.dma.soft.repository.UserInfoRepository;
import xyz.dma.soft.repository.UserRepository;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final UserInfoRepository userInfoRepository;

    public UserInfo getUserInfo(Long userId) {
        xyz.dma.soft.domain.user.UserInfo userInfo = userInfoRepository.findFirstById(userId);
        return new UserInfo(userId, userInfo);
    }

    @Transactional
    public void changeProfile(Long userId, UserInfo newUserInfo) {
        xyz.dma.soft.domain.user.UserInfo userInfo = userInfoRepository.findFirstById(userId);
        if(userInfo == null) {
            userInfo = new xyz.dma.soft.domain.user.UserInfo();
            userInfo.setUser(userRepository.findFirstById(userId));
        }
        if(newUserInfo != null) {
            String firstName = newUserInfo.getFirstName();
            String lastName = newUserInfo.getLastName();
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
