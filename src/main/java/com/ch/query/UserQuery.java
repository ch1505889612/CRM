package com.ch.query;

import com.ch.base.BaseQuery;
import lombok.Data;

@Data
public class UserQuery extends BaseQuery {
    // 用户名
    private String userName;
    // 邮箱
    private String email;
    // 电话
    private String phone;
}
