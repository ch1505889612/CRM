package com.ch.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneUtil {

    public static  boolean isMobile(String phone){
        Pattern p = null;
        Matcher m = null;
        p = Pattern.compile("^1(3|4|5|6|7|8|9)\\d{9}$"); // 验证手机号
        m = p.matcher(phone);
        return m.matches();
    }

    public static void main(String[] args) {
        System.out.println(isMobile("156713219732"));
    }
}
