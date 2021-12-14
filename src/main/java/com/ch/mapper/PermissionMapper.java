package com.ch.mapper;

import com.ch.base.BaseMapper;
import com.ch.bean.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission,Integer> {

    int countPermissionByRoleId(@Param("roleId") Integer roleId);

    int deletePermissionsByRoleId(@Param("roleId")Integer roleId);

    List<Integer> queryRoleHasAllModuleIdsByRoleId(@Param("roleId") Integer roleId);

    List<String> queryUserHasRolesHasPermissions(Integer userId);

    int countPermissionsByModuleId(Integer mid);

    int deletePermissionsByModuleId(Integer mid);
}