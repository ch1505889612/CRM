package com.ch.mapper;

import com.ch.base.BaseMapper;
import com.ch.base.BaseQuery;
import com.ch.bean.SaleChance;
import com.ch.query.SaleChanceQuery;

import java.util.List;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    List<SaleChance> querySelectByParams(SaleChanceQuery saleChanceQuery);
}