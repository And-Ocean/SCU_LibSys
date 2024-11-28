package com.example.backend.entity.overdueBookDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OverdueRecordDTO {
    private int book_id;
    private String lend_time;
    private String return_time;
    private char returned;
    private int fine_amount;
    private int lend_id;

    public OverdueRecordDTO(int book_id, String lend_time, String return_time, char returned, int fine_amount, int lend_id) {
        this.book_id = book_id;
        this.lend_time = lend_time;
        this.return_time = return_time;
        this.returned = returned;
        this.fine_amount = fine_amount;
        this.lend_id = lend_id;
    }
}
