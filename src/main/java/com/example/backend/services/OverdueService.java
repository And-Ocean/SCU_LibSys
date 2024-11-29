package com.example.backend.services;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.backend.mapper.OverdueMapper;
import com.example.backend.entity.overdueBookDTO.OverdueRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OverdueService extends ServiceImpl<OverdueMapper, OverdueRecordDTO> {

    @Autowired
    public OverdueMapper overdueMapper;

    public List<OverdueRecordDTO> getOverdueBookList(int user_id) {
        return overdueMapper.selectOverdueRecordDTO(user_id);
    }

    public int returnByLendId(int lend_id) {
        overdueMapper.returnSetYesByLendId(lend_id);
        overdueMapper.statusSetYesByLendId(lend_id);
        return 0;
    }
}
