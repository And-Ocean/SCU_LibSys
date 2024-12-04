package com.example.backend.services;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.BookCategory;

import com.example.backend.mapper.BookCategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.awt.print.Book;
import java.util.List;
@Service
public class BookCategoryService extends ServiceImpl<BookCategoryMapper, BookCategory> {
    @Autowired
    private BookCategoryMapper bookCategoryMapper;
    public List<BookCategory> getBookCategory() {
            return  bookCategoryMapper.getBookCategory();
        }

    public List<BookCategory> getLendNum() {
        return  bookCategoryMapper.getLendNum();
    }

    public List<String> getCategoryNum(String category) {
        return  bookCategoryMapper.getCategoryNum(category);
    }

    public int getLendCountByDate(String date) {
        return bookCategoryMapper.countLendByDate(date);
    }
}
