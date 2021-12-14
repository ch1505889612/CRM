package com.ch.mapper;

import com.ch.base.BaseMapper;
import com.ch.bean.Role;
import com.ch.bean.UserRole;
import com.ch.query.RoleQuery;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role,Integer> {
    // 查询角色列表
    @MapKey("")
    public List<Map<String,Object>> queryAllRoles(@Param("userId")Integer userId);

    List<Role> querySelectParams(RoleQuery roleQuery);

    Role queryRoleByRoleName(@Param("roleName") String roleName);
}