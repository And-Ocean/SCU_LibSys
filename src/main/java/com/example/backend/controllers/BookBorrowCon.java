package com.example.backend.controllers;

import com.example.backend.entity.BookEntity;
import com.example.backend.entity.ResponseBase;
import com.example.backend.entity.borrowBookDTO.*;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.services.AccessService;
import com.example.backend.services.BookBorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @PostMapping("/return_by_lend_id")
    public ResponseBase returnBookByLendId(@RequestBody ResponseBase request) {
        System.out.println("return_by_lend_id exec");
        ArrayList<Object> data = request.getData();
        ResponseBase response = new ResponseBase();
        int lend_id = (Integer) data.get(0);
        if (lend_id <= 0) {
            response.setStatus(-1);
            response.setMessage("Something went wrong with lend_id, it does not exist");
            return response;
        }
        String isbn = data.get(1).toString();
        bookBorrowService.returnByLendId(lend_id, isbn);
        return response;
    }

    @PostMapping("/borrowBook")
    public ResponseBase borrowBook(@RequestBody IsbnWithTk isbnWithTk) {
        ResponseBase response = new ResponseBase();
        ArrayList<BookEntity> booksAvailable = bookBorrowService.getBooksAvailable(isbnWithTk.getIsbn());
        response.pushData(booksAvailable);
        return response;
    }

    @PostMapping("/borrowABookById")
    public ResponseBase borrowABookById(@RequestBody IdWithTk idWithTk) {
        ResponseBase response = new ResponseBase();
        try {
            String accessToken = idWithTk.getAccessToken();
            int userId = accessService.getAuthenticatedId(accessToken);
            bookBorrowService.borrowExecute(userId, idWithTk.getId(), idWithTk.getIsbn());
        }
        catch (Exception e) {
            response.setStatus(-1);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PostMapping("/get_all_records")
    public ResponseBase getBorrowedBookAll() {
        ResponseBase response = new ResponseBase();
        try {
            List<BorrowRecordsAdmin> result_set = bookBorrowService.getBorrowAllRecords();
            if (result_set != null) {
                response.pushData(result_set);
            } else {
                throw new Exception();
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            response.setStatus(-1);
//            response.setMessage(e.getMessage());
        }
        return response;
    }
}
