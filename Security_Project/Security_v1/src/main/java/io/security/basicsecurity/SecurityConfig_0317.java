package io.security.basicsecurity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// @Configuration // 설정 Class이기 때문에
// @EnableWebSecurity
public class SecurityConfig_0317 extends WebSecurityConfigurerAdapter {

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
                .antMatchers("/login").permitAll()
                // 인가 정책
                .antMatchers("/user").hasRole("USER") // /user 자원에 접근하면 USER 권한을 가진 사용자만 인가 처리
                .antMatchers("/admin/pay").hasRole("ADMIN")
                .antMatchers("/admin/**").access("hasRole('ADMIN') or hasRole('SYS')") // 구체적인 범위가 광범위한 범위보다 더 위에 위치해야 한다.
                .anyRequest().authenticated(); // 어떠한 요청에도 인증을 받는다 : 인가 정책

        http
                .formLogin() // form Login 방식으로 인증 : 인증 정책
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        RequestCache requestCache = new HttpSessionRequestCache();
                        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest, httpServletResponse);
                        String redirectUrl = savedRequest.getRedirectUrl();
                        httpServletResponse.sendRedirect(redirectUrl);
                    }
                });

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

        // 인증&인가 예외 처리
        http
                .exceptionHandling()
                .authenticationEntryPoint(new AuthenticationEntryPoint() {  // 인증 예외
                    @Override
                    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
                        // 여기서 /login은 우리가 인터페이스를 직접 구현했기 때문에 스프링 시큐리티가 아닌 우리가 만든 로그인 페이지로 이동하도록 처리된다.
                        // 위에서 "/login"을 permitAll 처리해주지 않으면 접근이 불가해짐으로 추가해준다.
                        httpServletResponse.sendRedirect("/login");
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() { // 인가 예외
                    @Override
                    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
                        // 인증을 받은 사용자이나, 권한이 맞지 않는 것임으로 자원에 접근하도록 처리하지 않아도 됀다.
                        httpServletResponse.sendRedirect("/denied");
                    }
                });

        http
                .csrf(); // 기본적으로 활성화 되어있음으로, 명시하지 않아도 된다.
//                .csrf().disable();

    }
}
