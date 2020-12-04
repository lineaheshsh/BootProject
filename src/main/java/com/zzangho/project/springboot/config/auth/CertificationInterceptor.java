package com.zzangho.project.springboot.config.auth;

import com.zzangho.project.springboot.config.auth.dto.SessionUser;
import com.zzangho.project.springboot.domain.user.User;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class CertificationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        System.out.println("============================= 인터셉터 시작 ==============================");
        HttpSession session = request.getSession();
        SessionUser loginVO = (SessionUser) session.getAttribute("user");

        if(ObjectUtils.isEmpty(loginVO)){
            System.out.println("============================= 로그인 정보 X ==============================");
            response.sendRedirect("/user/login");
            return false;
        }else{
            System.out.println("============================= 로그인 정보 O ==============================");
            session.setMaxInactiveInterval(30*60);
            return true;
        }

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        // TODO Auto-generated method stub

    }
}
