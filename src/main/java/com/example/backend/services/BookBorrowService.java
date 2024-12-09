package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.entity.BookEntity;
import com.example.backend.entity.borrowBookDTO.BookBorrowedWithUserInfoDTO;
import com.example.backend.entity.borrowBookDTO.BorrowRecordsAdmin;
import com.example.backend.mapper.BookBorrowMapper;
import com.example.backend.entity.borrowBookDTO.BookBorrowedDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookBorrowService extends ServiceImpl<BookBorrowMapper, BookBorrowedDTO> {

    @Autowired
    public BookBorrowMapper bookBorrowMapper;

    public List<BookBorrowedDTO> getBookBorrowedList(int user_id) {
        return bookBorrowMapper.selectBookBorrowedDTO(user_id);
    }


    public List<BorrowRecordsAdmin> getBorrowAllRecords() {
        List<BookBorrowedWithUserInfoDTO> records =  bookBorrowMapper.getBookBorrowedAll();
        List<BorrowRecordsAdmin> res = new ArrayList<>();

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (BookBorrowedWithUserInfoDTO record : records) {
            LocalDate due_dt = LocalDateTime.parse(record.getReturn_time().trim(), formatter).toLocalDate();
            if (due_dt.isBefore(today)) {
                res.add(new BorrowRecordsAdmin(record, 1));
            } else {
                res.add(new BorrowRecordsAdmin(record, -1));    //-1 not overdue
            }
        }
        return res;
    }

    public int returnByLendId(int lend_id, String isbn) {
        bookBorrowMapper.returnSetYesByLendId(lend_id);
        bookBorrowMapper.statusSetYesByLendId(lend_id);
        System.out.println(isbn);
        bookBorrowMapper.borrowNumSet(isbn);
        return 0;
    }

    public ArrayList<BookEntity> getBooksAvailable(String isbn) {
        return bookBorrowMapper.getBooksAvailable(isbn);
    }

    public void borrowExecute(int user_id, int book_id, String isbn) {
        bookBorrowMapper.updateBookEntityStatus(book_id);
        bookBorrowMapper.updateBookIsbnBorrowNum(isbn);


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(formatter);
        LocalDateTime plus15Days = now.plusDays(15);
        String formattedPlus15Days = plus15Days.format(formatter);
//        System.out.println(formattedNow);
//        System.out.println(formattedPlus15Days);
        bookBorrowMapper.createBoRelation(user_id, book_id, formattedNow, formattedPlus15Days);
    }
}