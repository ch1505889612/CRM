package com.ch.controller;

import com.ch.base.BaseController;
import com.ch.base.ResultInfo;
import com.ch.bean.Module;
import com.ch.dto.TreeDto;
import com.ch.service.ModuleService;
import com.ch.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("module")
public class ModuleController extends BaseController {

    @Resource
    private ModuleService moduleService;


    @RequestMapping("queryAllModules")
    @ResponseBody
    public List<TreeDto> queryAllModules(Integer roleId){
        return moduleService.queryAllModules(roleId);
    }

    @RequestMapping("/index")
    public String index(){
        return "module/module";
    }


    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> moduleList(){
        return moduleService.moduleList();
    }



    // 添加资源页视图转发
    @RequestMapping("addModulePage")
    public String addModulePage(Integer grade,Integer parentId,Model model){
        model.addAttribute("grade",grade);
        model.addAttribute("parentId",parentId);
        return "module/add";
    }
    // 更新资源页视图转发
    @RequestMapping("updateModulePage")
    public String updateModulePage(Integer id,Model model){
        model.addAttribute("module",moduleService.selectByPrimaryKey(id));
        return "module/update";
    }
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveModule(Module module){
        moduleService.saveModule(module);
        return success("菜单添加成功");
    }
    @RequestMapping("queryAllModulesByGrade")
    @ResponseBody
    public List<Map<String,Object>> queryAllModulesByGrade(Integer grade){
        return moduleService.queryAllModulesByGrade(grade);
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateModule(Module module){
        moduleService.updateModule(module);
        return success("菜单更新成功");
    }


    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteModule(Integer id){
        moduleService.deleteModuleById(id);
        return success("菜单删除成功");
    }

}
