package com.ch.controller;



import com.ch.base.BaseController;
import com.ch.base.ResultInfo;
import com.ch.bean.SaleChance;
import com.ch.query.SaleChanceQuery;
import com.ch.service.SaleChanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("sale_chance")
public class SaleChangeController extends BaseController {

    /**
     * 进入营销机会页面
     * @return
     */
    @RequestMapping("index")
    public String index () {
        return "saleChance/sale_chance";
    }

    @Resource
    private SaleChanceService saleChanceService;


    /**
     * 多条件分页查询营销机会
     * @param query
     * @return
     */
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> querySaleChanceByParams (SaleChanceQuery query) {
        return saleChanceService.querySelectByParams(query);
    }

   /**
    * 机会数据添加与更新页面视图转发
    * @param id
    * @param model
    * @return
        */
    @RequestMapping("addOrUpdateSaleChancePage")
    public String addOrUpdateSaleChancePage(Integer id, Model model){
        if (id!=null){
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            System.out.println(saleChance);
            model.addAttribute("saleChance",saleChance);
        }
        return "saleChance/add_update";
    }

    /**
     * 添加
     * @param saleChance
     * @return
     */

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveSaleChange(SaleChance saleChance){
        saleChanceService.saveSaleChance(saleChance);
        return success();
    }

    /**
     * 修改
     * @param saleChance
     * @return
     */
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo changeSaleChange(SaleChance saleChance){
          saleChanceService.updateSaleChance(saleChance);
          return success();
    }

    /**
     * 删除计划项
     * @param ids
     * @return
     */
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteCusDevPlan(Integer[] ids){
        saleChanceService.removeSaleChanges(ids);
        return success("计划项删除成功!");
    }
}
