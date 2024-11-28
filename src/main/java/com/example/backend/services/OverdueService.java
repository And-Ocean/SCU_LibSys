package com.example.backend.services;

import com.example.backend.entity.overdueBookDTO.OverdueRecordDTO;
import com.example.backend.mapper.OverdueMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import java.util.List;

public interface OverdueService extends IService<OverdueRecordDTO> {
    List<OverdueRecordDTO> getOverdueRecords(int userId);
    void returnBook(int lendId);

    @Service
    class OverdueServiceImpl extends ServiceImpl<OverdueMapper, OverdueRecordDTO> implements OverdueService {

        private final OverdueMapper overdueMapper;

        public OverdueServiceImpl(OverdueMapper overdueMapper) {
            this.overdueMapper = overdueMapper;
        }

        @Override
        public List<OverdueRecordDTO> getOverdueRecords(int userId) {
            return overdueMapper.selectOverdueRecords(userId);
        }

        @Override
        public void returnBook(int lendId) {
            overdueMapper.returnSetYesByLendId(lendId);
            overdueMapper.statusSetYesByLendId(lendId);
        }
    }
}
