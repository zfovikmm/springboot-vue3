package com.zm.config;

import com.zm.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Resource
    LoginInterceptor loginInterceptor;

//    @Resource
//    ActionInterceptor actionInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test/**",
                        "/redis/**",
                        "/user/login",
                        "/category/list",
                        "/query",
                        "/doc/list/**"
//                        "/doc/vote/**",
//                        "/doc/find-content/**",
//                        "/ebook-snapshot/**"
//                        "/ebook/upload/avatar",
//                        "/file/**"
                );

//        registry.addInterceptor(actionInterceptor)
//                .addPathPatterns(
//                        "/*/save",
//                        "/*/delete/**",
//                        "/*/reset-password");
    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/file/**").addResourceLocations("file:D:/file/wiki/");
//    }
}
