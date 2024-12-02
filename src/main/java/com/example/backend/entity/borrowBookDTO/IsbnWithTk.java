package com.example.backend.entity.borrowBookDTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IsbnWithTk {
    private String isbn;
    private String accessToken;

    public IsbnWithTk(String isbn, String accessToken) {
        this.isbn = isbn;
        this.accessToken = accessToken;
    }
}
