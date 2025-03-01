package com.param.bs_backend.config;


import com.param.bs_backend.interceptor.AuthenticationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        return new AuthenticationInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // InterceptorConfig內的addInterceptor需要一個實現HandlerInterceptor接口的攔截器實例，
        // addPathPatterns方法用於設置攔截器的過濾路徑規則。
        // 攔截所有請求，通過判斷是否有 @TokenRequired 註解 決定是否需要登錄
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/**");
    }
}
