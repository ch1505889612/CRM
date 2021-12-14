package com.ch.mapper;

import com.ch.base.BaseMapper;
import com.ch.bean.UserRole;
import org.apache.ibatis.annotations.Param;


public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {


    int countUserRoleByUserId(@Param("userId") int useId);

    int deleteUserRoleByUserId(@Param("userId")int useId);
}