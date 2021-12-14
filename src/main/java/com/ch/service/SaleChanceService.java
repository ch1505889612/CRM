package com.ch.service;

import com.ch.base.BaseQuery;
import com.ch.base.BaseService;
import com.ch.base.ResultInfo;
import com.ch.bean.SaleChance;
import com.ch.mapper.SaleChanceMapper;
import com.ch.query.SaleChanceQuery;
import com.ch.utils.AssertUtil;
import com.ch.utils.PhoneUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SaleChanceService extends BaseService<SaleChance,Integer> {

    @Resource
    private SaleChanceMapper saleChanceMapper;
    /**
     * 多条件分页查询营销机会 (BaseService 中有对应的方法)
     * @param saleChanceQuery
     * @return
     */

    public Map<String,Object> querySelectByParams(SaleChanceQuery saleChanceQuery) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(saleChanceQuery.getPage(),saleChanceQuery.getLimit());
        PageInfo<SaleChance> pageInfo = new PageInfo<>(saleChanceMapper.querySelectByParams(saleChanceQuery));
        map.put("code",0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    /**
     * 营销数据添加
     * @param saleChance
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveSaleChance(SaleChance saleChance){
        // 1.参数校验
        checkParams(saleChance.getCustomerName(), saleChance.getLinkMan(),saleChance.getLinkPhone());
        // 2.设置相关参数默认值
        // 未选择分配人
        saleChance.setState(0);
        saleChance.setDevResult(0);
        // 选择分配人
        if (StringUtils.isNotBlank(saleChance.getAssignMan())) {
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }
        //默认值  state,devResult(0--未开发,1--开发中,2--开发成功了,3--开发失败)
        saleChance.setIsValid(1);
        saleChance.setUpdateDate(new Date());
        saleChance.setCreateDate(new Date());
        // 3.执行添加 判断结果
        AssertUtil.isTrue(insertSelective(saleChance)<1,"添加失败");
    }

    /**
     * 校验数据
     * @param customerName   客户名
     * @param linkMan   联系人
     * @param linkPhone  手机号
     */
    private void checkParams(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName),"请输入客户名");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan),"请输入联系人");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone),"请输入手机号");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone),"手机号格式不正确");
    }

    /**
     * 营销机会数据更新
     *
     * 一:验证
     *    1.当前对象的id
     *    2.客户非空
     *    3.联系人非空
     *    4.电话非空,11位合法
     * 二:
     *   设定默认值
     * 三
     *  修改是否成功
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateSaleChance (SaleChance saleChance) {
        // 1.参数校验
        // 通过id查询记录
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        System.out.println(temp);
        // 判断是否为空
        AssertUtil.isTrue(temp==null,"待更新记录不存在！");
        // 校验基础参数
        checkParams(temp.getCustomerName(), temp.getLinkMan(), temp.getLinkPhone());
        // 2. 设置相关参数值
        saleChance.setUpdateDate(new Date());
        if (StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())) {
        // 如果原始记录未分配，修改后改为已分配
            saleChance.setState(1);
            saleChance.setAssignTime(new Date());
            saleChance.setDevResult(1);
        } else if (StringUtils.isNotBlank(temp.getAssignMan())
                && StringUtils.isBlank(saleChance.getAssignMan())) {
        // 如果原始记录已分配，修改后改为未分配
            saleChance.setAssignMan("");
            saleChance.setState(0);
            saleChance.setAssignTime(null);
            saleChance.setDevResult(0);
        }
        // 3.执行更新 判断结果
      AssertUtil.isTrue(updateByPrimaryKeySelective(saleChance)<1,"修改失败");
    }


    public void removeSaleChanges(Integer[] ids){
        System.out.println(Arrays.toString(ids));
        //判断数据是否为空
        AssertUtil.isTrue((ids==null||ids.length==0),"请选择要删除的数据");
        AssertUtil.isTrue(deleteBatch(ids)<1,"删除数据失败");
    }
}
