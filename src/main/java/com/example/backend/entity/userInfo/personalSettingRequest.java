package com.example.backend.entity.userInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class personalSettingRequest {
    private String nickname;
    private String phone;
    private String address;
    private String accessToken;
    private int captcha;
    private String newemail;
    private String oldemail;
    private String oldpassword;
    private String newpassword;
}
