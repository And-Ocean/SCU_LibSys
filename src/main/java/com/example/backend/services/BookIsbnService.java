package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.*;
import com.example.backend.mapper.BookIsbnMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BookIsbnService extends ServiceImpl<BookIsbnMapper, BookISBN>{

    @Autowired
    private BookIsbnMapper bookIsbnMapper;

    public List<BookISBN> getAllBookISBN() {
        return  bookIsbnMapper.getAllBookISBN();
    }

    public int updateBookISBN(BookISBN record) {
        int res_code = bookIsbnMapper.updateBookISBN(
                record.getIsbn(), record.getTitle(), record.getPrice(), record.getAuthor(),
                record.getPublisher(), record.getBorrownum(), record.getCategory(), record.getCover_url(), record.getSummary());
        return res_code;
    }

    public int insertBookISBN(BookISBN record) {
        int res_code = bookIsbnMapper.insertBookISBN(
                record.getIsbn(), record.getTitle(), record.getPrice(), record.getAuthor(),
                record.getPublisher(), record.getBorrownum(),record.getCategory() ,record.getCover_url(), record.getSummary());
        return res_code;
    }

    public int deleteBookISBN(BookISBN record) {
        int res_code = bookIsbnMapper.deleteBookISBN(record.getIsbn());
        return res_code;
    }

}
