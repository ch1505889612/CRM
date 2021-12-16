//package com.ch.config;
//
//import com.ch.interceptor.NoLoginInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//
//@Configuration
//public class MvcConfig implements WebMvcConfigurer {
//
//    @Autowired
//    private NoLoginInterceptor noLoginInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        // 需要一个实现HandlerInterceptor接口的拦截器实例，这里使用的是NoLoginInterceptor
//
//        registry.addInterceptor(noLoginInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/index","/user/login","/css/**","/images/**","/js/**","/lib/**");
//    }
//}
