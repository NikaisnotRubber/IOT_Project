package com.param.bs_backend.interceptor;

import com.param.bs_backend.annotation.TokenRequired;
import com.param.bs_backend.pojo.User;
import com.param.bs_backend.service.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.param.bs_backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.lang.reflect.Method;


public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;

    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        // 從 http 請求頭中取出 token
        String token = httpServletRequest.getHeader("Authorization");
        // 判斷當前攔截到的是Controller的方法還是其他資源
        if (!(object instanceof HandlerMethod handlerMethod)) {
            //當前攔截到的不是動態方法，直接放行
            return true;
        }
        Method method = handlerMethod.getMethod();
        //檢查有沒有需要用戶權限的註解
        if (method.isAnnotationPresent(TokenRequired.class)) {
            TokenRequired userLoginToken = method.getAnnotation(TokenRequired.class);
            if (userLoginToken.required()) {
                // 執行認證
                if (token == null) {
                    throw new RuntimeException("無token，請重新登錄");
                }
                // 獲取 token 中的 user id
                String userId;
                try {
                    userId = JWT.decode(token).getClaim("userId").asString();
                } catch (JWTDecodeException j) {
                    throw new RuntimeException("401");
                }
                // Convert userId to Integer
                int userIdInteger;
                try {
                    userIdInteger = Integer.parseInt(userId);
                } catch (NumberFormatException e) {
                    throw new RuntimeException("Invalid userId format");
                }

                User user = userService.findUserById(userIdInteger);
                if (user == null) {
                    throw new RuntimeException("用戶不存在，請重新登錄");
                }
                // 驗證 token
                try {
                    if (!JwtUtil.verity(token)) {
                        throw new RuntimeException("無效的令牌");
                    }
                } catch (JWTVerificationException e) {
                    throw new RuntimeException("401");
                }
                return true;
            }
        }


        return true;
    }


    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
