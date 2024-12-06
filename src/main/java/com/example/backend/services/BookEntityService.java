package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.*;
import com.example.backend.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class BookEntityService extends  ServiceImpl<BookEntityMapper, BookEntity>{

    @Autowired
    private BookEntityMapper bookEntityMapper;

    public List<BookEntity> getAllBookEntity() {
        return  bookEntityMapper.getAllBookEntity();
    }

    public int updateBookEntity(BookEntity record) {
        int res_code = bookEntityMapper.updateBookEntity(
                record.getId(), record.getIsbn(), record.getStatus(), record.getPlace());
        return res_code;
    }

    public int insertBookEntity(BookEntity record) {
        int res_code = bookEntityMapper.insertBookEntity(
                record.getIsbn(), record.getStatus(), record.getPlace());
        return res_code;
    }


    public int deleteBookEntity(BookEntity record) {
        int res_code = bookEntityMapper.deleteBookEntity(record.getId());
        return res_code;
    }
}
