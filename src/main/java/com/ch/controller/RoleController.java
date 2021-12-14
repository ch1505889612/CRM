package com.ch.controller;


import com.ch.annotations.RequirePermission;
import com.ch.base.BaseController;
import com.ch.base.ResultInfo;
import com.ch.bean.Role;
import com.ch.query.RoleQuery;
import com.ch.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {

    @Resource
    private RoleService roleService;

    /**
     * 查询角色列表
     * @return
     */
    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String, Object>> queryAllRoles(Integer userId){

        return roleService.queryAllRoles(userId);
    }


    /**
     * 进入角色模块页面
     */
    @RequestMapping("/index")
     public String index(){
         return "role/role";
     }

     @RequestMapping("list")
     @ResponseBody
     @RequirePermission(code = "602002")
     public Map<String,Object> queryAllRoleInfo(RoleQuery roleQuery){
         return roleService.queryAllRoleInfo(roleQuery);
     }

    @RequestMapping("addOrUpdateRolePage")
    public String addUserPage(Integer id, Model model){
        if(null !=id){
            model.addAttribute("role",roleService.selectByPrimaryKey(id));
        }
        return "role/add_update";
    }
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.saveRole(role);
        return success("角色记录添加成功");
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("角色记录更新成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("角色记录删除成功");
    }



    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId, Model model){
        model.addAttribute("roleId",roleId);
        return "role/grant";
    }

    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mids,Integer roleId){
        roleService.addGrant(mids,roleId);
        return success("权限添加成功");
    }

}