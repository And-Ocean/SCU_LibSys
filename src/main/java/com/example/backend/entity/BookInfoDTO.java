package com.example.backend.entity;

//The result of linking bookEntity with bookIsbn
//DTO stands for Data Transfer Object

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BookInfoDTO {
    private int id;
    private String isbn;
    private String status;
    private String place;

    private String title;
    private float price;
    private String author;
    private String publisher;
    private String borrownum;

    public BookInfoDTO(int id, String isbn, String status, String place, String title,
                       float price, String author, String publisher, String borrownum) {
        this.id = id;
        this.isbn = isbn;
        this.status = status;
        this.place = place;
        this.title = title;
        this.price = price;
        this.author = author;
        this.publisher = publisher;
        this.borrownum = borrownum;
    }
}
