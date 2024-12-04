package com.example.backend.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@TableName("bookentity")
public class BookEntity {
    private int id;
    private String isbn;
    private String status;
    private String place;

    public BookEntity(int id, String isbn, String status, String place) {
        this.id = id;
        this.isbn = isbn;
        this.status = status;
        this.place = place;
    }


}
