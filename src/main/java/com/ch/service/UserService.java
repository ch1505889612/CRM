package com.ch.service;


import com.ch.base.BaseService;
import com.ch.base.ResultInfo;
import com.ch.bean.Permission;
import com.ch.bean.User;
import com.ch.bean.UserModel;
import com.ch.bean.UserRole;
import com.ch.mapper.PermissionMapper;
import com.ch.mapper.UserMapper;
import com.ch.mapper.UserRoleMapper;
import com.ch.query.SaleChanceQuery;
import com.ch.query.UserQuery;
import com.ch.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import freemarker.template.utility.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class UserService extends BaseService<User,Integer> implements UserDetailsService{

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private PermissionMapper permissionMapper;

    /**
     * 登录验证
     * @param username
     * @param password
     * @return
     */
    public UserModel userLogin(String username,String password){
        //1.验证参数
        checkLoginParams(username,password);
        //2.根据用户名查询数据库
        User user = userMapper.selectQueryUsername(username);
        //3.判断用户是否存在
        AssertUtil.isTrue(user==null,"用户不存在");
        //4.用户存在,就校验密码
        checkLoginPwd(password,user.getUserPwd());
        //用户名和密码都正确,返回用户信息
        return buildUserInfo(user);
    }

    /**
     *修改密码
     * @param userId 用户id
     * @param oldPassword 老密码
     * @param newPassword  新密码
     * @param confirmPassword  确认密码
     * @return
     */
    public void updateUserPassword(Integer userId, String oldPassword, String newPassword, String confirmPassword){
        // 通过userId获取用户对象
        User user = userMapper.selectByPrimaryKey(userId);
        // 1. 参数校验
        checkPasswordParams(user,oldPassword,newPassword,confirmPassword);
        // 2.设置用户新密码
        user.setUserPwd(Md5Util.encode(newPassword));
        // 3. 执行更新操作
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户密码更新失败！");
    }

    /**
     * 校验传入的参数
     * @param user
     * @param oldPassword
     * @param newPassword
     * @param confirmPassword
     */
    private void checkPasswordParams(User user, String oldPassword, String newPassword, String confirmPassword) {
        //1.判断是否登录
        AssertUtil.isTrue(user==null,"用户未登录");
        //2.判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPassword),"请输入原始密码！");
        //3.判断原始密码是否与数据库中的密码相同
        AssertUtil.isTrue(!(user.getUserPwd().equals(Md5Util.encode(oldPassword))),"原始密码不正确！");
        //4.新密码 非空校验
       AssertUtil.isTrue(StringUtils.isBlank(newPassword),"新密码不能为空");
       //5.新密码与原始密码是否相同
        AssertUtil.isTrue(oldPassword.equals(newPassword),"新密码不能与原始密码相同！");
        //6. 确认密码非空判断
        AssertUtil.isTrue(StringUtils.isBlank(confirmPassword),"确认密码不能为空");
        //7. 新密码与确认密码是否相同
        AssertUtil.isTrue(!(newPassword.equals(confirmPassword)),"新密码与确认密码不一致");
    }


    /**
     * 返回用户信息
     * @param user
     * @return
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserStrId(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 校验密码
     * @param password
     * @param userPwd
     */
    private void checkLoginPwd(String password, String userPwd) {
        //加密前端传送来的密码,与数据库密码做比较
        String encode = Md5Util.encode(password);
       //比较密码
        AssertUtil.isTrue((!encode.equals(userPwd)),"密码不正确");
    }

    /**
     * 验证参数
     * @param username
     * @param password
     */
    private void checkLoginParams(String username, String password) {
        AssertUtil.isTrue(StringUtils.isBlank(username),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(password),"用户密码不能为空");
    }

    /**
     * 查询所有的销售人员
     * @return
     */
    public List<Map<String, Object>> queryAllSales() {
        return userMapper.queryAllSales();
    }


    /**
     * 查询所有的用户
     * @return
     */
    public Map<String, Object> QueryUser(UserQuery userQuery) {
          Map<String,Object> map=new HashMap<>();
          PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
          PageInfo<User> pageInfo=new PageInfo<>(userMapper.querySelectByParams(userQuery));
          map.put("code", 0);
          map.put("msg", "success");
          map.put("count", pageInfo.getTotal());
          map.put("data", pageInfo.getList());
          return map;
    }

/**
 * 添加用户
 * 1. 参数校验
 * 用户名 非空 唯一性
 * 邮箱 非空
 * 手机号 非空 格式合法
 * 2. 设置默认参数
 * isValid 1
 * creteDate 当前时间
 * updateDate 当前时间
 * userPwd 123456 -> md5加密
 * 3. 执行添加，判断结果
 */
@Transactional(propagation = Propagation.REQUIRED)
   public void addUser(User user){
       // 1. 参数校验
       checkParams(user.getUserName(),user.getEmail(),user.getPhone());
     //验证用户名是否存在
     User temp = userMapper.selectQueryUsername(user.getUserName());
     AssertUtil.isTrue(temp!=null,"用户名已存在");
      // 2. 设置默认参数
       user.setIsValid(1);
       user.setCreateDate(new Date());
       user.setUpdateDate(new Date());
       user.setUserPwd(Md5Util.encode("123456"));
      // 3. 执行添加，判断结果
        AssertUtil.isTrue(insertSelective(user)<1,"添加失败");
    /**
     * 用户角色分配
     * userId
     * roleIds
     */
      relaionUserRole(user.getId(), user.getRoleIds());
   }



    private void relaionUserRole(int useId, String roleIds) {
/**
 * 用户角色分配
 * 原始角色不存在 添加新的角色记录
 * 原始角色存在 添加新的角色记录
 * 原始角色存在 清空所有角色
 * 原始角色存在 移除部分角色
 * 如何进行角色分配???
 * 如果用户原始角色存在 首先清空原始所有角色 添加新的角色记录到用户角色表
 */
        int count = userRoleMapper.countUserRoleByUserId(useId);
        if (count > 0) {
            AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(useId) != count,
                    "用户角色分配失败!");
        }
        if (StringUtils.isNotBlank(roleIds)) {
           //重新添加新的角色
            List<UserRole> userRoles = new ArrayList<UserRole>();
            for (String s : roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(useId);
                userRole.setRoleId(Integer.parseInt(s));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                userRoles.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(userRoles) <
                    userRoles.size(), "用户角色分配失败!");
        }
    }




    /**
     * 更新用户
     * 1. 参数校验
     * id 非空 记录必须存在
     * 用户名 非空 唯一性
     * email 非空
     * 手机号 非空 格式合法
     * 2. 设置默认参数
     * updateDate
     * 3. 执行更新，判断结果
     * @param user
     */
     @Transactional(propagation = Propagation.REQUIRED)
     public void changeUser(User user){
         System.out.println(user);
         //1. 参数校验
         User user1 = userMapper.selectByPrimaryKey(user.getId());
         AssertUtil.isTrue(user1==null,"该用户记录不存在");
         checkParams(user.getUserName(),user.getEmail(),user.getPhone());
         //2.设置默认参数
         user.setUpdateDate(new Date());
         // 3. 执行更新，判断结果
         AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1, "用户更新 失败！");
         relaionUserRole(user.getId(), user.getRoleIds());
     }

    /**
     * 删除用户数据
     * @param ids 用户id
     */
    public void deletes(Integer[] ids){
         AssertUtil.isTrue(ids.length==0||ids==null,"参数为空");
          //遍历对象
        for (Integer userId:ids) {
            //统计当前用户有多少个角色
            int count = userRoleMapper.countUserRoleByUserId(userId);
            //删除当前用户的角色
            if (count>0){
                AssertUtil.isTrue(userRoleMapper.deleteUserRoleByUserId(userId)!=count,"用户角色删除失败");
            }
        }
       //判断是否删除成功
        AssertUtil.isTrue(userMapper.deleteBatch(ids)<1,"删除失败了");
     }





    /**
     * 校验数据
     * @param userName 用户名
     * @param email  邮箱
     * @param phone  手机
     */
    private void checkParams(String userName, String email, String phone) {
       AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(email==null,"邮箱不能为空");
        AssertUtil.isTrue(phone==null,"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"手机号码格式不正确！");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.selectQueryUsername(username);
        System.out.println(user+"--------------");
        if (username==null||user==null){
           throw new UsernameNotFoundException("用户不存在");
        }
            List<String> permissions = permissionMapper.queryUserHasRolesHasPermissions(user.getId());
            List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
            for (String p:permissions) {
                GrantedAuthority grantedAuthority=new SimpleGrantedAuthority(p);
                grantedAuthorities.add(grantedAuthority);
            }
            System.out.println(grantedAuthorities);
            User users = new User();
            users.setIsValid(user.getIsValid());
            users.setUserName(username);
            users.setUserPwd(user.getPassword());
            users.setAuthorities(grantedAuthorities);
            System.out.println(users);
            return users;


    }
}
