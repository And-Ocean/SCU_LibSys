package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.borrowBookDTO.BookBorrowedDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface BookBorrowMapper extends BaseMapper<BookBorrowedDTO> {


    @Select("select id as book_id, lend_time, return_time, returned, isbn, status, place, title, price, author, publisher " +
            "from " +
            "(select l.lend_time, l.return_time, l.returned, b.* from lends as l, bookentity as b " +
            "where l.book_id = b.id and l.user_id = #{user_id}) as t natural join bookisbn")
    List<BookBorrowedDTO> selectBookBorrowedDTO(int user_id);
}
