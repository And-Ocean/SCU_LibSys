package com.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.backend.entity.overdueBookDTO.OverdueRecordDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OverdueMapper extends BaseMapper<OverdueRecordDTO> {

    @Select("SELECT book_id, lend_time, return_time, returned, fine_amount, lend_id " +
            "FROM overdue_records WHERE user_id = #{userId} AND returned = 0")
    List<OverdueRecordDTO> selectOverdueRecords(int userId);

    @Update("UPDATE overdue_records SET returned = 1 WHERE lend_id = #{lendId}")
    void returnSetYesByLendId(int lendId);

    @Update("UPDATE books SET status = 1 WHERE id = ( " +
            "SELECT book_id FROM overdue_records WHERE lend_id = #{lendId})")
    void statusSetYesByLendId(int lendId);
}
