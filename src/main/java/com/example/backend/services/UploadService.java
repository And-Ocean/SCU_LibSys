package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.User;
import com.example.backend.mapper.UploadMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UploadService extends ServiceImpl<UploadMapper, User> {

    @Autowired
    private UploadMapper uploadMapper;
    public int uploadPersonalAvatar(int id,String url) {
        try {
            return uploadMapper.updatePersonalAvatar(id, url);
        }
        catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            // 可以选择返回一个特定的错误码或抛出自定义异常
            return -1; // 表示重置密码失败
        }
    }
}
