package com.example.backend.controllers;
import com.example.backend.entity.BookCategory;
import com.example.backend.entity.BookISBN;
import com.example.backend.entity.ResponseBase;
import com.example.backend.services.BookCategoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/BookCategory")
public class BookCategoryCon {
    @Autowired
    private BookCategoryService bookCategoryService;
    @PostMapping("/getBookCategory")
    public ResponseEntity<ResponseBase> getBookCategory() {
        ResponseBase response = new ResponseBase();
        List<BookCategory> records = bookCategoryService.getBookCategory();

        for (BookCategory record : records) {
            response.pushData(record);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/getCategoryNum")
    public ResponseEntity<ResponseBase> getCategoryNum(@RequestBody String category) {
        ResponseBase response = new ResponseBase();
        List<String> records = bookCategoryService.getCategoryNum(category);

        for (String record : records) {
            response.pushData(record);
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PostMapping("/getLendNum")
    public ResponseEntity<ResponseBase> getLendNum() {
        ResponseBase response = new ResponseBase();

        // 获取上周的日期列表
        List<String> lastWeekDates = DateUtils.getLastWeekDates();

        // 存储每天的借书数量
        List<Integer> dailyLendCounts = new ArrayList<>();

        // 根据每一天的日期查询借书数量
        for (String date : lastWeekDates) {
            int lendCount = bookCategoryService.getLendCountByDate(date);
            dailyLendCounts.add(lendCount);
        }

        // 将借书数量加入响应数据
        response.pushData(dailyLendCounts);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



    public static class DateUtils {

        // 获取上周的每一天（周一到周日）
        public static List<String> getLastWeekDates() {
            List<String> lastWeekDates = new ArrayList<>();

            Calendar calendar = Calendar.getInstance();
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK); // 获取今天是星期几
            // 计算上周的周一
            calendar.add(Calendar.DAY_OF_YEAR, -(dayOfWeek + 5));  // 上周的周一
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            // 获取上周的每一天（周一到周日）
            for (int i = 0; i < 7; i++) {
                lastWeekDates.add(sdf.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            System.out.println(lastWeekDates);
            return lastWeekDates;
        }
    }

}