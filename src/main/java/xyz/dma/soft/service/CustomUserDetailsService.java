package xyz.dma.soft.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.dma.soft.domain.user.User;
import xyz.dma.soft.domain.user.UserRole;
import xyz.dma.soft.repository.UserRepository;

import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findFirstByLogin(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        org.springframework.security.core.userdetails.User.UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(username);
        builder.password(user.getPassword());
        builder.roles(user.getUserRoles().stream().map(UserRole::getName).collect(Collectors.toList()).toArray(new String[]{}));

        return builder.build();
    }
}
