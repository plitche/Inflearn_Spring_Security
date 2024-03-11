package io.security.basicsecurity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${spring.security.user.password}") // Properties에서 비밀번호 값을 가져옴
    private String password;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("user".equals(username)) {
            // 사용자가 존재하는 경우, UserDetails 객체를 생성하여 반환합니다.
            return User.builder()
                    .username("user")
                    .password(password) // 가져온 비밀번호 값을 사용합니다.
                    .roles("USER")
                    .build();
        } else {
            // 사용자가 존재하지 않는 경우, 예외를 던져서 Spring Security에게 알려줍니다.
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
