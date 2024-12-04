package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.User;
import com.example.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    public int login(String username, String password) {
        try {
            User user = userMapper.findByUserName(username);
            if (user != null) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                if (encoder.matches(password, user.getPassword())) {
                    return user.getId();
                }
            }
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示登录失败
        }
        return 0; // 用户名或密码错误
    }
    public User userInfoByUserid(int userId) {
        try {
            User user = userMapper.findByUserId(userId);
            if (user != null) {
                return user;
            }
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return null; // 表示查询失败
        }
        return null;
    }
    //返回登录的用户相关信息
    public User userInfoByUsername(String username) {
        try {
            User user = userMapper.findByUserName(username);
            if (user != null) {
                return user;
            }
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return null; // 表示查询失败
        }
        return null; // 用户不存在
    }
    //个人设置信息更改
    public int basicInfoSetting(String username,String phone,String intro,int id) {
    try {
        User user = userMapper.findByUserId(id);
        if (user != null) {
            return userMapper.updateUserInfo(username, phone, intro, id);
        }
        return -1;
    } catch (Exception e) {
        // 记录异常信息
        e.printStackTrace();
        // 可以选择返回一个特定的错误码或抛出自定义异常
        return -1; // 表示更改失败
        }
    }

    public int register(String email,String username, String password) {
        try {
            String nickname = "";
            String usersex = "";
            String userphone = "";
            String useraddress = "";
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passwordBCrypt = encoder.encode(password);
            int role = 0; // 默认为一般用户
            if (userMapper.findByUserName(username) != null) {
                return 0;
            }
            return userMapper.adminInsertUser(username,nickname, usersex, userphone, useraddress,passwordBCrypt, role);
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示注册失败
        }
    }

    public int resetPassword(String email,String username, String password) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passwordBCrypt = encoder.encode(password);
            if (userMapper.findByUserName(username) == null) {
                return 0;
            }
            return userMapper.updatePassword(email,username, passwordBCrypt);
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示重置密码失败
        }
    }
    public int resetPersonalPassword(int id,String old_password, String new_password) {
        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String newPasswordBCrypt = encoder.encode(new_password);
            User user = userMapper.findByUserId(id);
            if(user!=null){
                if (encoder.matches(old_password, user.getPassword())) {
                    return userMapper.updatePersonalPassword(id, newPasswordBCrypt);
                }
            }
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示重置密码失败
        }
        return 0; // 输入原来密码错误
    }
    public int resetPersonalEmail(int id,String oldEmail, String newEmail) {
        try {
            if (userMapper.findByUserId(id) == null) {
                return 0;
            }
            return userMapper.updatePersonalEmail(id,oldEmail, newEmail);
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示重置密码失败
        }
    }
    public List<User> adminUserInfo(int user_id,String keyword){
        try {
            User user = userMapper.findByUserId(user_id);
            int role = user.getRole();
            if (role==1){
                if(keyword.equals("")) {
                    return userMapper.findAllUser();
                }
                else{
                    return userMapper.findUserWithKeyword(keyword);
                }
            }
            return null;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public boolean adminUserAdd(String username,String nickname,String sex,String phone,String address){
        if(userMapper.findByUserName(username)!=null){
            return false;
        }
        String password = "123456";//初始密码
        int role = 0;//默认为一般用户
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String passwordBCrypt = encoder.encode(password);
        return userMapper.adminInsertUser(username,nickname,sex,phone,address,passwordBCrypt,0)>0;
    }
    public boolean adminUserUpdate(String userName,String nickName,String userSex,String userPhone,String userAddress){
        int id = userMapper.findByUserName(userName).getId();
        return userMapper.adminUpdateUserInfo(userName,nickName,userSex,userPhone,userAddress,id)>0;
    }
    public boolean adminUserDelete(String userName){
        int id = userMapper.findByUserName(userName).getId();
        return userMapper.adminDeleteUser(id)>0;
    }
}