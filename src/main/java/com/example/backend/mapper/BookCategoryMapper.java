package com.example.backend.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.BookCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface BookCategoryMapper extends BaseMapper<BookCategory> {
    @Select("SELECT * FROM category")
    List<BookCategory> getBookCategory();

    @Select("SELECT * FROM lends")
    List<BookCategory> getLendNum();

    @Select("SELECT * FROM bookisbn WHERE category = #{category}")
    List<String> getCategoryNum(String category);

    @Select("SELECT COUNT(*) FROM lends WHERE DATE(lend_time) = #{date}")
    int countLendByDate(@Param("date") String date);

}
