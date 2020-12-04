package com.zzangho.project.springboot.config.auth;

import com.zzangho.project.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security 설정들을 활성화시켜 줍니다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // h2-console 화면을 사용하기 위해 해당 옵션들을 disable 합니다.
                .and()
                    .authorizeRequests()    // URL별 권한 관리를 설정하는 옵션의 시작점. authorizeRequest가 선언되야만 antMatchers 옵션을 사용할 수 있다.
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**", "/vendor/**").permitAll()    // 전체 열람 권한
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())    // USER 권한을 가진 사람만 열람 권한
                    .anyRequest() // 설정된 값들 이외 나머지 URL들을 나타낸다.
                    .authenticated()    // 여기서는 authenticated()를 추가하여 나머지 URL들을 모두 인증된 사용자에게만 허용하게 한다.
                .and()
                    .oauth2Login()
                    .loginPage("/user/login")
                    .permitAll()
                .and()
                    .logout()
                        .logoutSuccessUrl("/user/login")    // 로그아웃 시
                .and()
                    .oauth2Login()  // OAuth2로그인 기능에 대한 여러 설정의 진입점
                        .userInfoEndpoint() // OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당한다.
                            .userService(customOAuth2UserService);
                            // 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록한다.
        //                  // 리소스 서버에서 사용자 정보를 가져온 상태에서 추가로 진행하고자 하는 기능을 명시 할 수 있다.
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }
}
