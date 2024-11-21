package com.example.chat.dao;

import com.example.chat.pojo.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    //添加用户
    int userInsert(String email,String username,String password);

    //更新密码
    int userUpdatePassword(String email,String newPassword);

    //更新用户名
    int userUpdateUsername(String email,String newUsername);

    //获取用户列表
    List<User> userSelect(String email,String password);

    //获取用户详细信息
    User userInfo(String email);

    //更改用户信息
    int userInfoUpdate(User user);
}
