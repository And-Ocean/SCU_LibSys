package com.example.backend.controllers;

import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.example.backend.services.BookBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.backend.entity.borrowBookDTO.BookBorrowedDTO;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/bookBorrow")
public class BookBorrowCon {

    @Autowired
    private AccessService accessService;

    @Autowired
    private BookBorrowService bookBorrowService;

    @PostMapping("/get_books_by_user_id")
    public ResponseBase userList(@RequestBody adminUserInfoRequest request) {
        ResponseBase response = new ResponseBase();
        try {
            String accessToken = request.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);

            List<BookBorrowedDTO> result_set = bookBorrowService.getBookBorrowedList(userId);
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


}
