package com.example.backend.entity.borrowBookDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IdWithTk {
    int id;
    private String accessToken;
    private String isbn;

    public IdWithTk(int id, String accessToken, String isbn) {
        this.id = id;
        this.accessToken = accessToken;
        this.isbn = isbn;
    }
}
