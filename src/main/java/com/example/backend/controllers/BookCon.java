package com.example.backend.controllers;

import com.example.backend.entity.BookEntity;
import com.example.backend.entity.BookISBN;
import com.example.backend.entity.ResponseBase;
import com.example.backend.services.BookEntityService;
import com.example.backend.services.BookIsbnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/book")
public class BookCon {
    @Autowired
    private BookIsbnService book_service;

    @PostMapping("/getBookIsbn")
    public ResponseEntity<ResponseBase> getBookISBN() {
        ResponseBase response = new ResponseBase();
        List<BookISBN> records = book_service.getAllBookISBN();

        for (BookISBN record : records) {
            response.pushData(record);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/modifyBookISBN")
    public ResponseBase modifyBookISBN(@RequestBody BookISBN record) {
        ResponseBase response = new ResponseBase();
        int res_code = book_service.updateBookISBN(record);
        return new ResponseBase();
    }

    @PostMapping("/addBookISBN")
    public ResponseBase addBookISBN(@RequestBody BookISBN record){
        int res_code = book_service.insertBookISBN(record);
        return new ResponseBase();
    }
    @PostMapping("/deleteBookISBN")
    public ResponseEntity<ResponseBase> deleteBookISBN(@RequestBody BookISBN record) {
        ResponseBase response = new ResponseBase();
        int res_code = book_service.deleteBookISBN(record);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Autowired
    private BookEntityService book_entity_service;
    @PostMapping("/addBookEntity")
    public ResponseBase addBookEntity(@RequestBody BookEntity record){
        int res_code = book_entity_service.insertBookEntity(record);
        return new ResponseBase();
    }
}
