package cn.yangeit.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //自定义的拦截器对象
    @Autowired
    private LoginCheckInterceptor loginCheckInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       //注册自定义拦截器对象
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/customer/**")//设置拦截器拦截的请求路径（ /** 表示拦截所有请求）
                //设置不拦截的请求路径
                .excludePathPatterns("/customer/user/login",
                        "/customer/roomTypes",
                        "/error",
                        "/swagger-ui/**", "/v3/**");
    }
}
