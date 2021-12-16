package com.ch.config;


import com.ch.handler.MyAccessDeniedHandler;
import com.ch.handler.MyAuthenticationFailureHandler;
import com.ch.handler.MyAuthenticationSuccessHandler;
import com.ch.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled=true,jsr250Enabled=true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    @Resource
    private UserService userService;
    /**
     * 登录成功处理器
     */
    @Resource
    private MyAuthenticationSuccessHandler authenticationSuccessHandler;

    /**
     * 登录失败处理器
     */
    @Resource
    private MyAuthenticationFailureHandler authenticationFailureHandler;

    /**
     * 无权限访问处理器
     */
    @Resource
    private MyAccessDeniedHandler accessDeniedHandler;


    /**
     *
     * @return 配置密码处理器
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }




    @Override
    protected void configure(HttpSecurity http) throws Exception {

            //登录处理
        http.formLogin()
                .loginPage("/index")
                .loginProcessingUrl("/login")
                .successHandler(authenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                //自定义表单参数
                .usernameParameter("username")
                .passwordParameter("password");



        //认证处理
        http.authorizeRequests()
                .antMatchers("/index","/css/**","/images/**","/js/**","/lib/**")
                .permitAll()
                .anyRequest().authenticated();

        //权限访问处理器
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
        //关闭csrf
        http.csrf().disable();
    }
}
