package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.mapper.BookBorrowMapper;
import com.example.backend.entity.borrowBookDTO.BookBorrowedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookBorrowService extends ServiceImpl<BookBorrowMapper, BookBorrowedDTO> {

    @Autowired
    public BookBorrowMapper bookBorrowMapper;

    public List<BookBorrowedDTO> getBookBorrowedList(int user_id) {
        return bookBorrowMapper.selectBookBorrowedDTO(user_id);
    }

    public int returnByLendId(int lend_id) {
        bookBorrowMapper.returnSetYesByLendId(lend_id);
        bookBorrowMapper.statusSetYesByLendId(lend_id);
        return 0;
    }



}