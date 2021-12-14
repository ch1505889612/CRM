package com.ch.service;

import com.ch.base.BaseService;
import com.ch.bean.Permission;
import com.ch.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {

    @Resource
    private PermissionMapper permissionMapper;


    public List<String> queryUserHasRolesHasPermissions(Integer userId) {
        return permissionMapper.queryUserHasRolesHasPermissions(userId);
    }
}
