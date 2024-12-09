package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.TodoRecord;
import com.example.backend.entity.advice.AdminGetDTO;
import com.example.backend.entity.advice.StuGetDTO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;


@Mapper
public interface TodoListMapper extends BaseMapper<TodoRecord> {


    @Select("SELECT username, ad_id, ad_ctnt, ad_title, replied, finished, reply_ctnt, crt_time " +
            "FROM advice, user where advice.user_id = user.id")
    ArrayList<AdminGetDTO> getAll();


    @Update("UPDATE advice " +
            "SET replied = #{replied} , reply_ctnt = #{reply_ctnt}," +
            "replyer_id = #{replyer_id} " +
            "WHERE ad_id = #{ad_id}")
    int updateTodoRecord(int ad_id, String replied, String reply_ctnt, int replyer_id);


    @Insert("INSERT INTO usertodo (todo_title, todo_ctnt, todo_fin, todo_crt, todo_ddl, user_id, adder_id)" +
            " VALUES(#{todo_title}, #{todo_ctnt}, #{todo_fin}, #{todo_crt}, #{todo_ddl}, #{user_id}, #{adder_id})")
    int insertTodoRecord(int user_id, int todo_id, int adder_id, String todo_title,
                         String todo_ctnt, String todo_fin, String todo_crt, String todo_ddl);

    @Delete("DELETE from advice WHERE ad_id = #{todo_id}")
    int deleteRecord(int todo_id);


    @Insert("INSERT INTO advice (user_id, ad_ctnt, ad_title, replied, finished, reply_ctnt, replyer_id, crt_time) " +
            "VALUES (#{user_id}, #{ad_ctnt}, #{ad_title}, '未回复' , '未完成', '无', -1, #{crt_time})")
    void createRecord(int user_id, String ad_ctnt, String ad_title, String crt_time);

    @Select("SELECT ad_id, ad_ctnt, ad_title, replied, finished, reply_ctnt, crt_time " +
            "from advice where user_id = #{userId}")
    List<StuGetDTO> getMyRecords(int userId);
}
