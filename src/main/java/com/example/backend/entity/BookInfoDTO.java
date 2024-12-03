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
    private String cover_url;
    private String summary;
    private String category;

    public BookInfoDTO(int id, String isbn, String status, float price, String title, String place,
                       String author, String publisher, String borrownum, String cover_url, String summary, String category) {
        this.id = id;
        this.isbn = isbn;
        this.status = status;
        this.price = price;
        this.title = title;
        this.place = place;
        this.author = author;
        this.publisher = publisher;
        this.borrownum = borrownum;
        this.cover_url = cover_url;
        this.summary = summary;
        this.category = category;
    }
}
