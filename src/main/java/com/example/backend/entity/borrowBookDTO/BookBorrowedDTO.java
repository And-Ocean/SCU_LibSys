package com.example.backend.entity.borrowBookDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BookBorrowedDTO {
    private int book_id;
    private String lend_time;
    private String return_time;
    private char returned;
    private String isbn;
    private String status;
    private String place;
    private String title;
    private float price;
    private String author;
    private String publisher;
    private int lend_id;

    public BookBorrowedDTO(int book_id, String lend_time, String return_time, char returned, String isbn,
                           String status, String place, String title, float price, String author, String publisher, int lend_id) {
        this.book_id = book_id;
        this.lend_time = lend_time;
        this.return_time = return_time;
        this.returned = returned;
        this.isbn = isbn;
        this.status = status;
        this.place = place;
        this.title = title;
        this.price = price;
        this.author = author;
        this.publisher = publisher;
        this.lend_id = lend_id;
    }
}
