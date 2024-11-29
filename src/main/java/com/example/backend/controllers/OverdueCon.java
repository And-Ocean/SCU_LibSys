package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.example.backend.services.OverdueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.entity.overdueBookDTO.OverdueRecordDTO;

import java.util.List;

@RestController
@RequestMapping("/api/overdue")
public class OverdueCon {

    @Autowired
    private AccessService accessService;

    @Autowired
    private OverdueService overdueService;

    @PostMapping("/get_overdue_books_by_user_id")
    public ResponseBase getOverdueBooksByUserId(@RequestBody adminUserInfoRequest request) {
        ResponseBase response = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);

            List<OverdueRecordDTO> result_set = overdueService.getOverdueBookList(userId);
            if (result_set != null) {
                response.pushData(result_set);
            } else {
                throw new Exception();
            }
        }
        catch (Exception e) {
            response.setStatus(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/return_overdue_book_by_lend_id")
    public ResponseBase returnOverdueBookByLendId(@RequestBody OverdueRecordDTO overdueRecordDTO) {
        int lend_id = overdueRecordDTO.getLend_id();
        ResponseBase response = new ResponseBase();
        if (lend_id <= 0) {
            response.setStatus(-1);
            response.setMessage("Something went wrong with lend_id, it does not exist");
            return response;
        }
        overdueService.returnByLendId(lend_id);
        return response;
    }
}
