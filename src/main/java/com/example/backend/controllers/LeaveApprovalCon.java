package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.LeaveApprovalRecord;
import com.example.backend.services.LeaveApprovalService;
import com.example.backend.mapper.LeaveApprovalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/leaveApproval")
public class LeaveApprovalCon {

    @Autowired
    private LeaveApprovalService leave_service;

    @PostMapping("/getLeaveRecord")
    public ResponseBase getLeaveRecord() {
        ResponseBase res = new ResponseBase();
        List<LeaveApprovalRecord> records = leave_service.getAllRecords();

        for (LeaveApprovalRecord record : records) {
            res.pushData(record);
        }
        return res;
    }

    @PostMapping("/modifyLeaveRecord")
    public ResponseBase modifyLeaveRecord(@RequestBody LeaveApprovalRecord record) {
        int res_code = leave_service.updateLeaveApprovalRecord(record);
        return new ResponseBase();
    }

    @PostMapping("/addLeaveRecord")
    public ResponseBase addLeaveRecord(@RequestBody LeaveApprovalRecord record) {
        int res_code = leave_service.insertLeaveApprovalRecord(record);
        return new ResponseBase();
    }


    @PostMapping("/deleteLeaveRecord")
    public ResponseBase deleteLeaveRecord(@RequestBody LeaveApprovalRecord record) {
        int res_code = leave_service.deleteRecord(record);
        return new ResponseBase();
    }
}
