package com.example.backend.entity.borrowBookDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BookBorrowedWithUserInfoDTO {
    private int book_id;
    private String lend_time;
    private String return_time;
    private char returned;
    private String isbn;
    private String title;
    private float price;
    private String author;
    private String publisher;
    private int lend_id;
    private String cover_url;
    private String summary;
    private String category;

    private int user_id;
    private String email;
    private String sex;
    private String username;
    private String phone;
    private String address;

    public BookBorrowedWithUserInfoDTO(int book_id, String lend_time, String return_time, char returned, String isbn, String title, float price, String author, String publisher, int lend_id, String cover_url, String summary, String category, int user_id, String email, String sex, String username, String phone, String address) {
        this.book_id = book_id;
        this.lend_time = lend_time;
        this.return_time = return_time;
        this.returned = returned;
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.author = author;
        this.publisher = publisher;
        this.lend_id = lend_id;
        this.cover_url = cover_url;
        this.summary = summary;
        this.category = category;
        this.user_id = user_id;
        this.email = email;
        this.sex = sex;
        this.username = username;
        this.phone = phone;
        this.address = address;
    }
}
