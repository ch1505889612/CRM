package com.ch.controller;


import com.ch.base.BaseController;
import com.ch.base.ResultInfo;
import com.ch.bean.User;
import com.ch.bean.UserModel;
import com.ch.exceptions.ParamsException;
import com.ch.query.UserQuery;
import com.ch.service.UserService;
import com.ch.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("login")
    @ResponseBody
    public ResultInfo userLogin (String username,String password){
        ResultInfo resultInfo = new ResultInfo();
        // 通过 try catch 捕获 Service 层抛出的异常
//        try {
            // 调用Service层的登录方法，得到返回的用户对象
            UserModel userModel = userService.userLogin(username, password);
            /**
             * 登录成功后，有两种处理：
             * 1. 将用户的登录信息存入 Session （ 问题：重启服务器，Session 失效，客户端
             需要重复登录 ）
             * 2. 将用户信息返回给客户端，由客户端（Cookie）保存
             */
            // 将返回的UserModel对象设置到 ResultInfo 对象中
            resultInfo.setResult(userModel);
//        }catch (ParamsException e){
//            e.printStackTrace();
//            resultInfo.setMsg(e.getMsg());
//            resultInfo.setCode(e.getCode());
//        }catch (Exception e){
//            e.printStackTrace();
//            resultInfo.setMsg("操作失败");
//            resultInfo.setCode(500);
//        }
        return resultInfo;
    }

    @RequestMapping("updatePassword")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,String oldPassword, String newPassword, String confirmPassword){
        ResultInfo resultInfo = new ResultInfo();
//        try {
            // 获取userId
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
            //调用service方法
            userService.updateUserPassword(userId,oldPassword,newPassword,confirmPassword);
//        }catch (ParamsException pe){
//            pe.printStackTrace();
//            resultInfo.setCode(pe.getCode());
//            resultInfo.setMsg(pe.getMsg());
//        }catch (Exception e){
//            e.printStackTrace();
//            resultInfo.setMsg("操作失败");
//            resultInfo.setCode(500);
//        }
        return resultInfo;
    }

    /**
     * 跳转到基本信息页面
     * @param req
     * @return
     */
    @RequestMapping("toSettingPage")
    public String setting(HttpServletRequest req){
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(userId);
        req.setAttribute("user",user);
        return "/user/setting";
    }


    /**
     * 修改基本信息
     */
    @RequestMapping("sayUpdate")
    @ResponseBody
    public ResultInfo sayUpdate(User user){
        ResultInfo resultInfo = new ResultInfo();
        System.out.println(user);
        Integer count = userService.updateByPrimaryKeySelective(user);
        if (count>0){
           resultInfo.setCode(200);
           resultInfo.setMsg("修改成功");
        }
        return resultInfo;
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    @RequestMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales() {
        return userService.queryAllSales();
    }


    /**
     * 进入用户模块页面
     */
    @RequestMapping("index")
    public String index() {
        return "user/user";
    }

    /**
     * 添加修改页面
     * @return
     */
    @RequestMapping("addUpdateUser")
    public String addUpdateUser(Integer id, Model model) {
        System.out.println(id);
        if (id!=null){
            User user = userService.selectByPrimaryKey(id);
            System.out.println(user+"---------------");
            model.addAttribute("user",user);
        }
        return "user/add_update";
    }



    /**
     * 加载用户模块用户信息
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> list(UserQuery userQuery){
        return userService.QueryUser(userQuery);
    }

    /**
     * 添加用户
     */
    @RequestMapping("add")
    @ResponseBody
    public ResultInfo add(User user){
        userService.addUser(user);
        return success();
    }

    /**
     * 添加用户
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(User user){
        userService.changeUser(user);
        return success();
    }

    /**
     * 删除用户
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer[] ids){
        userService.deletes(ids);
        return success();
    }

}
