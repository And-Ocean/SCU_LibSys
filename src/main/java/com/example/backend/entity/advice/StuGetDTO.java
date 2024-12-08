package com.example.backend.entity.advice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class StuGetDTO {
    private int ad_id;
    private String ad_ctnt;
    private String ad_title;
    private String replied;
    private String finished;
    private String reply_ctnt;
    private String crt_time;

    public StuGetDTO(int ad_id, String ad_ctnt, String ad_title, String replied, String finished,
                     String reply_ctnt, String crt_time) {
        this.ad_id = ad_id;
        this.ad_ctnt = ad_ctnt;
        this.ad_title = ad_title;
        this.replied = replied;
        this.finished = finished;
        this.reply_ctnt = reply_ctnt;
        this.crt_time = crt_time;
    }
}
