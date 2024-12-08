package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.TodoRecord;
import com.example.backend.entity.advice.AdminGetDTO;
import com.example.backend.entity.advice.AdminReplyDTO;
import com.example.backend.entity.advice.AdviceCreateDTO;
import com.example.backend.entity.advice.StuGetDTO;
import com.example.backend.mapper.TodoListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.entity.ResponseBase;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class TodoService extends ServiceImpl<TodoListMapper, TodoRecord> {

    @Autowired
    private TodoListMapper todoListMapper;

    public ResponseBase getAllRecords() {
        ArrayList<AdminGetDTO> set = todoListMapper.getAll();
        ResponseBase response = new ResponseBase();
        response.pushData(set);
        return response;
    }

    public ResponseBase getMyRecords(int userId) {
        ResponseBase res = new ResponseBase();
        res.pushData(todoListMapper.getMyRecords(userId));
        return res;
    }

    public int updateTodoRecord(AdminReplyDTO record) {
//        System.out.println(record.getTodo_fin());
        int res_code = todoListMapper.updateTodoRecord(record.getAd_id(), record.getReplied(),
                record.getReply_ctnt(), record.getReplyer_id());
        return res_code;
    }

    public void createRecord(AdviceCreateDTO record, int user_id) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(formatter);

        todoListMapper.createRecord(user_id, record.getAd_ctnt(), record.getAd_title(), formattedNow);
    }


    public int deleteRecord(TodoRecord record) {
//        System.out.println(record.getTodo_fin());
        int res_code = todoListMapper.deleteRecord(record.getTodo_id());
        return res_code;
    }


}
