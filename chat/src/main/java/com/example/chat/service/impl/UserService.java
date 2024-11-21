package com.example.chat.service.impl;

import com.example.chat.pojo.Result;
import com.example.chat.pojo.User;
import com.example.chat.dao.UserDao;
import com.example.chat.service.UserServiceInterface;
import com.example.chat.pojo.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService implements UserServiceInterface {
    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserDao userDao;
    @Override
    public Result userRegister(User user) {
        if (userDao.userSelect(user.getEmail(),null).isEmpty()){
            int changeCount = userDao.userInsert(user.getEmail(), user.getUsername(), user.getPassword());
            log.info("{}：进行注册功能,email={},username={},更改了{}行",
                    LocalDateTime.now(),user.getEmail(),user.getUsername(),changeCount);
            if (changeCount > 0 ) return Result.success("注册成功");
        }

        return Result.error("注册失败，用户已存在");
    }


    @Override
    public Result userChangePassword(User user) {
        int changCount = userDao.userUpdatePassword(user.getEmail(),user.getPassword());
        log.info("{}：进行更改密码功能,email={},更改了{}行",
                LocalDateTime.now(),user.getEmail(),changCount);
        if (changCount > 0){
            return Result.success("更改成功");
        }else {
            return Result.error("更改失败，用户不存在");
        }

    }

    @Override
    public Result userChangeName(User user) {
        int changCount = userDao.userUpdateUsername(user.getEmail(),user.getUsername());
        log.info("{}：进行更改用户名功能,email={},更改了{}行",
                LocalDateTime.now(),user.getEmail(),changCount);
        if (changCount > 0){
            return Result.success("更改成功");
        }else {
            return Result.error("更改失败，用户不存在");
        }
    }


    //登录功能
    @Override
    public Result login(User user) {
        if (user.getEmail() == null || user.getPassword() == null || user.getEmail().isEmpty() || user.getPassword().isEmpty()){
            return Result.error("账号或密码为空");
        }

        boolean empty = userDao.userSelect(user.getEmail(), user.getPassword()).isEmpty();
        log.info("{}进行了登录操作",user.getEmail());
        if (!empty){
            Map<String,Object> clamis = new HashMap<>();
            clamis.put("email",user.getEmail());
            clamis.put("password",user.getPassword());
            String jwt = JwtUtil.generateJwt(clamis);
            return Result.success(jwt);
        }else {
            return Result.error("登录失败,用户不存在或密码错误");
        }
    }

    @Override
    public List<User> userSelect(User user) {
        log.info("{},查询了用户email={}",LocalDateTime.now(),user.getEmail());
        return userDao.userSelect(user.getEmail(),user.getPassword());
    }

    @Override
    public List<User> userSelect() {
        log.info("{},查询了用户列表",LocalDateTime.now());
        return userDao.userSelect(null,null);
    }

    @Override
    public Result userInfo(String email) {
        if(email.isEmpty()){
            return Result.success();
        }
        User user = userDao.userInfo(email);
        return Result.success(user);
    }

    @Override
    public Result changeAvatar(String email,String uuid) {
        User user = new User();
        user.setAvatar(uuid);
        user.setEmail(email);
        int i = userDao.userInfoUpdate(user);
        if (i>0){
            return Result.success("更改成功");
        }else{
            return Result.error("更改失败，用户不存在");
        }
    }


}
