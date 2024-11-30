package com.example.backend.entity.userInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class adminUserInfoRequest {
    private String accessToken;
    private String keyword;
    private String userName;
    private String userPhone;
    private String userSex;
    private String userAddress;
    private String nickName;
}
