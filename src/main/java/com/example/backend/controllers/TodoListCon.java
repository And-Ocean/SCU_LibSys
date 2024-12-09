package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.TodoRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.services.TodoService;
import java.util.List;

@RestController
@RequestMapping("/api/todolist")
public class TodoListCon {

    @Autowired
    private TodoService my_service;

    @PostMapping("/modifyRec")
    public ResponseBase modifyRec(@RequestBody TodoRecord record) {
//        int res_code = my_service.updateTodoRecord(record);
//        System.out.println("modifyRec res_code: " + res_code);
        return new ResponseBase();
    }


    @PostMapping("/deleteTodo")
    public ResponseBase deleteRec(@RequestBody TodoRecord record) {
        System.out.println("deleteRecord " + record.getTodo_id());
        int res_code = my_service.deleteRecord(record);
        System.out.println("deleteRecord res_code: " + res_code);
        return new ResponseBase();
    }
}
