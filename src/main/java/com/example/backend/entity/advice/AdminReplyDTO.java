package com.example.backend.entity.advice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class AdminReplyDTO {

    private String accessToken;
    private int ad_id;
    private String replied;
    private String reply_ctnt;
    private int replyer_id;

    public AdminReplyDTO(String accessToken, int ad_id, String replied, String reply_ctnt, int replyer_id) {
        this.accessToken = accessToken;
        this.ad_id = ad_id;
        this.replied = replied;
        this.reply_ctnt = reply_ctnt;
        this.replyer_id = replyer_id;
    }
}
