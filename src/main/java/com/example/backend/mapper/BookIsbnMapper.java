package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.BookISBN;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookIsbnMapper extends BaseMapper<BookISBN> {
    @Select("SELECT * FROM bookisbn")
    List<BookISBN> getAllBookISBN();

    @Update("UPDATE bookisbn " +
            "SET title = #{title}, price = #{price}, author = #{author}, publisher = #{publisher}, " +
            "borrownum = #{borrownum}, category = #{category}, cover_url = #{cover_url}, summary = #{summary}" +
            " WHERE isbn = #{isbn}")
    int updateBookISBN(String isbn, String title, float price, String author,
                                  String publisher, String borrownum, String category,  String cover_url, String summary);

    @Insert("INSERT INTO bookisbn (isbn, title, price, author, publisher, borrownum, category, cover_url, summary)" +
            " VALUES(#{isbn}, #{title}, #{price}, #{author}, #{publisher}, #{borrownum}, #{category}, #{cover_url}, #{summary})")
    int insertBookISBN(String isbn, String title, float price, String author,
                             String publisher, String borrownum, String category,  String cover_url, String summary);

    @Delete("DELETE FROM bookisbn WHERE isbn = #{isbn}")
    int deleteBookISBN(String isbn);
}
