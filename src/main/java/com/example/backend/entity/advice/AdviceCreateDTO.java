package com.example.backend.entity.advice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AdviceCreateDTO {
    private String accessToken;
    private String ad_title;
    private String ad_ctnt;

    public AdviceCreateDTO(String accessToken, String ad_title, String ad_ctnt) {
        this.accessToken = accessToken;
        this.ad_title = ad_title;
        this.ad_ctnt = ad_ctnt;
    }
}
