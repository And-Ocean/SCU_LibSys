package com.example.backend.entity.borrowBookDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BorrowRecordsAdmin extends BookBorrowedWithUserInfoDTO {
    private int overdue;

    public BorrowRecordsAdmin(BookBorrowedWithUserInfoDTO base, int overdue) {
        super(
                base.getBook_id(),
                base.getLend_time(),
                base.getReturn_time(),
                base.getReturned(),
                base.getIsbn(),
                base.getTitle(),
                base.getPrice(),
                base.getAuthor(),
                base.getPublisher(),
                base.getLend_id(),
                base.getCover_url(),
                base.getSummary(),
                base.getCategory(),
                base.getUser_id(),
                base.getEmail(),
                base.getSex(),
                base.getUsername(),
                base.getPhone(),
                base.getAddress()
        );
        this.overdue = overdue;
    }

}
