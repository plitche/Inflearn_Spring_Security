package io.security.basicsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 설정 Class이기 때문에
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // in-memory 방식 사용자 생성
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // (noop) : prefix(password encode 관련 내용)

        auth.inMemoryAuthentication().withUser("user").password("(noop)1111").roles("USER");
        auth.inMemoryAuthentication().withUser("sys").password("(noop)1111").roles("SYS", "USER");
        auth.inMemoryAuthentication().withUser("admin").password("(noop)1111").roles("ADMIN", "SYS", "USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 인가 정책
                .antMatchers("/user").hasRole("USER") // /user 자원에 접근하면 USER 권한을 가진 사용자만 인가 처리
                .antMatchers("/admin/pay").hasRole("ADMIN")
                .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')") // 구체적인 범위가 광범위한 범위보다 더 위에 위치해야 한다.
                .anyRequest().authenticated(); // 어떠한 요청에도 인증을 받는다 : 인가 정책

        http
                .formLogin(); // form Login 방식으로 인증 : 인증 정책

        http
                .rememberMe()
                .userDetailsService(userDetailsService);

        http
                .sessionManagement()
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true); // 새로운 로그인을 시도하는 사용자의 세션을 차단시킴
                // .maxSessionsPreventsLogin(true); // 기존에 로그인 되어있는 사용자의 세션을 삭제함

        // 세션 고정 보호
        http
                .sessionManagement()
                // .sessionFixation().none(); // 무방비 상태
                .sessionFixation().changeSessionId(); // 기본 값


    }
}
