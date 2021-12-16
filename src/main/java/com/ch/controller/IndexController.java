package com.ch.controller;


import com.ch.base.BaseController;
import com.ch.base.ResultInfo;
import com.ch.bean.User;
import com.ch.service.PermissionService;
import com.ch.service.UserService;
import com.ch.utils.CookieUtil;
import com.ch.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;


    /**
     * 登录页面
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }

    // 系统界面欢迎页
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){
        int userId = LoginUserUtil.releaseUserIdFromCookie(request);
        User user = userService.selectByPrimaryKey(userId);
        request.setAttribute("user", user);
        return "main";
    }

    @RequestMapping("/user/toPasswordPage")
    public String toPasswordPage(){
        return "/user/password";
    }


}
