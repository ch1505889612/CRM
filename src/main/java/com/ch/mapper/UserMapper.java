package com.ch.mapper;

import com.ch.base.BaseMapper;
import com.ch.bean.SaleChance;
import com.ch.bean.User;
import com.ch.query.UserQuery;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User,Integer> {

    User selectQueryUsername(@Param("username") String username);

    // 查询所有的销售人员
     @MapKey("")
     List<Map<String, Object>> queryAllSales();

    List<User>  querySelectByParams(UserQuery userQuery);
}