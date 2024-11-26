package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.borrowBookDTO.BookBorrowedDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface BookBorrowMapper extends BaseMapper<BookBorrowedDTO> {


    @Select("select id as book_id, lend_time, return_time, returned, isbn, status, place, title, price, author, publisher, lend_id " +
            "from " +
            "(select l.id as lend_id, l.lend_time, l.return_time, l.returned, b.* from lends as l, bookentity as b " +
            "where l.book_id = b.id and l.user_id = #{user_id} and l.returned = 0) as t natural join bookisbn")
    List<BookBorrowedDTO> selectBookBorrowedDTO(int user_id);

    @Update("update lends set returned = 1 where id = #{lend_id}")
    void returnSetYesByLendId(int lend_id);

    @Update("update bookentity set status = 1 where id = ( " +
            "select book_id from lends where id = #{lend_id})")
    void statusSetYesByLendId(int lend_id);
}
