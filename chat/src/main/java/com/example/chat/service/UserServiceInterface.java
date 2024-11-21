package com.example.chat.service;

import com.example.chat.pojo.Result;
import com.example.chat.pojo.User;

import java.util.List;

public interface UserServiceInterface {

    //用户注册使用
    Result userRegister(User user);


    //用户更改密码使用
    Result userChangePassword(User user);

    //用户更改用户名
    Result userChangeName(User user);

    //用户登录
    Result login(User user);

    //查询用户
    List<User> userSelect(User user);
    List<User> userSelect();

    //查询用户信息
    Result userInfo(String email);

    //更改用户头像
    Result changeAvatar(String email,String uuid);

}
