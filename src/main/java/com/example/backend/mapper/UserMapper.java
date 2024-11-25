package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>{

    @Select("SELECT * FROM user WHERE id = #{id}")
    User findByUserId(int id);
    @Select("SELECT * FROM user WHERE email = #{email}")
    User findByEmail(String email);
    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUserName(String username);

    @Select("SELECT * FROM user")
    List<User> findAllUser();
    @Insert("INSERT INTO user (email, username, password, role) VALUES(#{email}, #{username}, #{password}, #{role})")
    //@Options(useGeneratedKeys = true, keyProperty = "user_id")
    int insertUser(String email, String username,String password, int role);
    @Update("UPDATE user SET password = #{password} WHERE email = #{email} and username=#{username}")
    int updatePassword(String email, String username, String password);
    @Update("UPDATE user SET nickname = #{nickname},phone = #{phone},address= #{address} WHERE id = #{id}")
    int updateUserInfo(String nickname, String phone,String address, int id);
}
