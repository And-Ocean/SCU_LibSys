package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.overdueBookDTO.OverdueRecordDTO;
import com.example.backend.services.AccessService;
import com.example.backend.services.OverdueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/overdueManagement")
public class OverdueCon {

    @Autowired
    private AccessService accessService;

    @Autowired
    private OverdueService overdueService;

    @PostMapping("/getOverdueList")
    public ResponseBase getOverdueList(@RequestBody Map<String, Integer> request) {
        ResponseBase response = new ResponseBase();
        try {
            int userId = request.get("userId");
            List<OverdueRecordDTO> result_set = overdueService.getOverdueRecords(userId);
            if (result_set != null) {
                response.pushData(result_set);
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            response.setStatus(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/returnBook")
    public ResponseBase returnBook(@RequestBody Map<String, Integer> request) {
        ResponseBase response = new ResponseBase();
        int lend_id = request.get("lendId");
        if (lend_id <= 0) {
            response.setStatus(-1);
            response.setMessage("Invalid lend_id, it does not exist");
            return response;
        }
        try {
            overdueService.returnBook(lend_id);
        } catch (Exception e) {
            response.setStatus(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }
}
