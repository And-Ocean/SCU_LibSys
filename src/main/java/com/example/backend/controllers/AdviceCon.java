package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.TodoRecord;
import com.example.backend.entity.advice.AdminReplyDTO;
import com.example.backend.entity.advice.AdviceCreateDTO;
import com.example.backend.entity.advice.StuGetDTO;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.example.backend.services.TodoService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/advice")
public class AdviceCon {

    @Autowired
    private TodoService my_service;

    @Autowired
    private AccessService access_service;

    @PostMapping("/getRec")
    public ResponseBase getRec() {
        return  my_service.getAllRecords();
    }

    @PostMapping("/stuGetRec")
    public ResponseBase stuGetRec(@RequestBody adminUserInfoRequest request) {
        int user_id = access_service.getAuthenticatedId(request.getAccessToken());
        System.out.println(user_id);
        return my_service.getMyRecords(user_id);
    }

    @PostMapping("/modifyRec")
    public ResponseBase modifyRec(@RequestBody AdminReplyDTO record) {
        int user_id = access_service.getAuthenticatedId(record.getAccessToken());
        record.setReplyer_id(user_id);
        int res_code = my_service.updateTodoRecord(record);
        return new ResponseBase();
    }

    @PostMapping("/deleteTodo")
    public ResponseBase deleteRec(@RequestBody TodoRecord record) {
        System.out.println("deleteRecord " + record.getTodo_id());
        int res_code = my_service.deleteRecord(record);
//        System.out.println("deleteRecord res_code: " + res_code);
        return new ResponseBase();
    }

    @PostMapping("/createRec")
    public ResponseBase addRec(@RequestBody AdviceCreateDTO record) {
        int user_id = access_service.getAuthenticatedId(record.getAccessToken());
        System.out.println("createRec " + user_id);
        my_service.createRecord(record, user_id);
        return new ResponseBase();
    }
}
