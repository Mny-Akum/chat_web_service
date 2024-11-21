package com.example.chat.controller;

import com.example.chat.pojo.Result;
import com.example.chat.pojo.User;
import com.example.chat.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat/user")
public class UserController {
    @Autowired
    UserService userService;
    /**
     *  注册功能
     */
    @PostMapping("/register")
    public Result register(@RequestBody User user){
        return userService.userRegister(user);
    }

    /**
     * 登录功能
     */
    @PostMapping("/login")
    public Result login(@RequestBody User user){
        return userService.login(user);
    }

    /**
     * 查询用户列表
     */
    @GetMapping("/list")
    public Result getUserList(){
        return Result.success(userService.userSelect());
    }

    /**
     * 更改用户名功能
     */
    @PutMapping("/rename")
    public Result changeName(@RequestBody User user){
        return userService.userChangeName(user);
    }

    /**
     * 更改密码功能
     */
    @PutMapping("/repsd")
    public Result changePassword(@RequestBody User user){
        return userService.userChangePassword(user);
    }

    /**
     * 查询用户信息功能
     */
    @GetMapping("/getInfo")
    public Result getUserInfo(String email){
        return userService.userInfo(email);
    };
    /**
     * 更改用户头像
     */
    @PutMapping("/reavatar")
    public Result changeAvatar(@RequestBody User user){
        return userService.changeAvatar(user.getEmail(),user.getAvatar());
    }

}
