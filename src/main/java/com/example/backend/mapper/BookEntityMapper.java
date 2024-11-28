package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.BookEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookEntityMapper extends BaseMapper<BookEntity> {

    @Select("SELECT * FROM bookentity")
    List<BookEntity> getAllBookEntity();

    @Update("UPDATE bookentity " +
            "SET isbn = #{isbn}, status = #{status}, place = #{place} WHERE id = #{id}")
    int updateBookEntity(int id, String isbn, String status, String place);

    @Insert("INSERT INTO bookentity (id, isbn, status, place)" +
            " VALUES(#{id}, #{isbn}, #{status}, #{place})")
    int insertBookEntity(int id, String isbn, String status, String place);

    @Delete("DELETE FROM bookentity WHERE id = #{id}")
    int deleteBookEntity(int id);

}
