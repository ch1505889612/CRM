package com.ch.handler;

import com.alibaba.fastjson.JSONObject;
import com.ch.bean.User;
import com.ch.bean.UserModel;
import com.ch.utils.UserIDBase64;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
         response.setContentType("application/json;charset=utf-8");
         response.setCharacterEncoding("utf-8");

        User principal = (User) authentication.getPrincipal();
        UserModel userModel = new UserModel();
        userModel.setUserStrId(UserIDBase64.encoderUserID(principal.getId()));
        userModel.setUserName(principal.getUserName());
        userModel.setTrueName(principal.getTrueName());
         PrintWriter pw=null;
         Map<String,Object> map= new HashMap<>();
         map.put("code",200);
         map.put("msg","登录成功");
         map.put("result",userModel);
         pw=response.getWriter();
         System.out.println(new ObjectMapper().writeValueAsString(map)+"--------------");
         pw.write(new ObjectMapper().writeValueAsString(map));
         pw.flush();
         pw.close();
    }
}
