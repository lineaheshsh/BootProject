package com.zzangho.project.springboot.config;

import com.zzangho.project.springboot.config.auth.CertificationInterceptor;
import com.zzangho.project.springboot.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    // HandlerMethodArgumentResolver는 항상 WebMvcConfigurer의 addArgumentResolvers를 통해 추가해야 한다.
    // 다른 Handler-MethodArgumentResolver가 필요하다면 같은 방식으로 추가해 주면 된다.
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver);
    }

    /*
     * 로그인 인증 Interceptor 설정
     * */
    @Autowired
    CertificationInterceptor certificationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(certificationInterceptor)
                .addPathPatterns("/*")
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/js/**")
                .excludePathPatterns("/css/**")
                .excludePathPatterns("/vendor/**");
    }
}
