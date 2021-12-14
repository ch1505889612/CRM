package com.ch.service;

import com.ch.base.BaseService;
import com.ch.bean.Permission;
import com.ch.bean.Role;
import com.ch.bean.User;
import com.ch.bean.UserRole;
import com.ch.mapper.*;
import com.ch.query.RoleQuery;
import com.ch.utils.AssertUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class RoleService extends BaseService<Role,Integer> {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Resource
    private ModuleMapper moduleMapper;

    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleMapper.queryAllRoles(userId);
    }

    /**
     * 查询role数据
     * @param roleQuery
     * @return
     */
    public Map<String,Object> queryAllRoleInfo(RoleQuery roleQuery){
        Map<String,Object> map=new HashMap<>();
        PageHelper.startPage(roleQuery.getPage(),roleQuery.getLimit());
        PageInfo<Role> pageInfo=new PageInfo<>( roleMapper.querySelectParams(roleQuery));
        map.put("code",0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRole(Role role){
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null !=temp,"该角色已存在!");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(role)<1,"角色记录添加失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role){
        AssertUtil.isTrue(null==role.getId()||null==selectByPrimaryKey(role.getId()),"待 修改的记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()),"请输入角色名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null !=temp && !(temp.getId().equals(role.getId())),"该角色已存在!");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(role)<1,"角色记录更新失败!");
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteRole(Integer roleId){
        Role temp =selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null==roleId||null==temp,"待删除的记录不存在!");
        temp.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp)<1,"角色记录删除失败!");
    }


    public void addGrant(Integer[] mids, Integer roleId) {
/**
 * 核心表-t_permission t_role(校验角色存在)
 * 如果角色存在原始权限 删除角色原始权限
 * 然后添加角色新的权限 批量添加权限记录到t_permission
 */
        Role temp =selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null==roleId||null==temp,"待授权的角色不存在!");
        int count = permissionMapper.countPermissionByRoleId(roleId);
        if(count>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionsByRoleId(roleId)<count,"权限分配失败!");
        }
        if(null !=mids && mids.length>0){
            List<Permission> permissions=new ArrayList<Permission>();
            for (Integer mid : mids) {
                Permission permission=new Permission();
                permission.setCreateDate(new Date());
                permission.setUpdateDate(new Date());
                permission.setModuleId(mid);
                permission.setRoleId(roleId);
                permission.setAclValue(moduleMapper.selectByPrimaryKey(mid).getOptValue());
                permissions.add(permission);
            }
           AssertUtil.isTrue( permissionMapper.insertBatch(permissions)!=permissions.size(),"授权失败");
        }
    }


}
