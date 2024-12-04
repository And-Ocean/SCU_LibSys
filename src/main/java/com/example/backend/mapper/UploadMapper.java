package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
@Mapper
public interface UploadMapper extends BaseMapper<User> {
    @Update("UPDATE user SET avatar = #{avatar} WHERE id = #{id}")
    int updatePersonalAvatar(int id,String avatar);
}
