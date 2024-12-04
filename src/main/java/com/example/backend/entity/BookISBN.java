package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

@TableName("bookisbn")
public class BookISBN {
    private String isbn;
    private String title;
    private float price;
    private String author;
    private String publisher;
    private String borrownum;
    private String category;
    private String cover_url;
    private String summary;

    public BookISBN(String isbn, String title, float price, String author, String publisher, String borrownum,
                    String category, String cover_url, String summary) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.author = author;
        this.publisher = publisher;
        this.borrownum = borrownum;
        this.category = category;
        this.cover_url = cover_url;
        this.summary = summary;
    }
}
