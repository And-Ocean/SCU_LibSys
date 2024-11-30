package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.User;
import org.apache.ibatis.annotations.*;

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
    @Select("SELECT * FROM user WHERE username LIKE CONCAT('%', #{keyword}, '%') OR nickname LIKE CONCAT('%', #{keyword}, '%')OR sex LIKE CONCAT('%', #{keyword}, '%')OR phone LIKE CONCAT('%', #{keyword}, '%')OR address LIKE CONCAT('%', #{keyword}, '%')")
    List<User> findUserWithKeyword(String keyword);
    @Insert("INSERT INTO user (username, nickname, sex, phone, address, password,role) VALUES(#{username}, #{nickname}, #{sex}, #{phone}, #{address}, #{password}, #{role})")
    //@Options(useGeneratedKeys = true, keyProperty = "user_id")
    int adminInsertUser(String username, String nickname,String sex,String phone, String address,String password, int role);
    @Update("UPDATE user SET password = #{password} WHERE email = #{email} and username=#{username}")
    int updatePassword(String email, String username, String password);
    @Update("UPDATE user SET nickname = #{nickname},phone = #{phone},address= #{address} WHERE id = #{id}")
    int updateUserInfo(String nickname, String phone,String address, int id);
    @Update("UPDATE user SET username=#{username},nickname=#{nickname},sex=#{sex},phone=#{phone},address=#{address} WHERE id=#{id}")
    int adminUpdateUserInfo(String username, String nickname, String sex, String phone, String address, int id);
    @Update("UPDATE user SET email = #{newEmail} WHERE id =#{id} and email=#{oldEmail}")
    int updatePersonalEmail(int id,String oldEmail,String newEmail);
    @Update("UPDATE user SET password = #{newPassword} WHERE id=#{id}")
    int updatePersonalPassword(int id,String newPassword);
    @Delete("DELETE FROM user WHERE id=#{id}")
    int adminDeleteUser(int id);
}
