package com.example.backend.controllers;

import com.example.backend.entity.userInfo.personalSettingRequest;
import com.example.backend.entity.userInfo.personalSettingResponse;
import com.example.backend.services.AccessService;
import com.example.backend.services.CaptchaService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/setting")
public class Personal {
    @Autowired
    private UserService userService;
    @Autowired
    private AccessService accessService;
    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/basicInfo")
    public ResponseEntity<personalSettingResponse> basicInfo(@RequestBody personalSettingRequest request) {
        try{
        String nickname = request.getNickname();
        String phone = request.getPhone();
        String address = request.getAddress();
        String accessToken = request.getAccessToken();
        int userId = accessService.getAuthenticatedId(accessToken);
        int isSuccess = userService.basicInfoSetting(nickname,phone,address,userId);
        if(isSuccess > 0){
            personalSettingResponse.Data data = new personalSettingResponse.Data(nickname,phone,address);
            personalSettingResponse response = new personalSettingResponse(
                    0,
                    "更新个人信息成功",
                    true,
                    data
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
            else{
                personalSettingResponse response = new personalSettingResponse(
                        0,
                        "更新个人信息失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch(Exception e){
            personalSettingResponse response = new personalSettingResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/resetEmail")
    ResponseEntity<personalSettingResponse> resetEmail(@RequestBody personalSettingRequest request){
        try{
            String oldEmail = request.getOldEmail();
            String newEmail = request.getNewEmail();
            int captcha = request.getCaptcha();
            String accessToken = request.getAccessToken();
            int id = accessService.getAuthenticatedId(accessToken);
            int isVerified = captchaService.verifyCaptcha(oldEmail, captcha);
            if (isVerified == 0) {
                int isSuccess = userService.resetPersonalEmail(id, oldEmail, newEmail);
                if (isSuccess > 0) {
                    personalSettingResponse.Data data = new personalSettingResponse.Data(nickname, phone, address);
                    personalSettingResponse response = new personalSettingResponse(
                            0,
                            "更新个人信息成功",
                            true,
                            data
                    );
                    return ResponseEntity.status(HttpStatus.OK).body(response);
                } else {
                    personalSettingResponse response = new personalSettingResponse(
                            0,
                            "更新个人信息失败",
                            false,
                            null
                    );
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
                }
            }
        }
        catch(Exception e){
            personalSettingResponse response = new personalSettingResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/resetPassword")
    ResponseEntity<personalSettingResponse> resetPassword(@RequestBody personalSettingRequest request){
        try{
            String nickname = request.getNickname();
            String accessToken = request.getAccessToken();
            String oldPassword = request.getOldPassword();
            String newPassword = request.getNewPassword();
            int id = accessService.getAuthenticatedId(accessToken);
            int isSuccess = userService.resetPersonalPassword(id, oldPassword, newPassword);
            if (isSuccess > 0) {
                personalSettingResponse.Data data = new personalSettingResponse.Data(nickname, phone, address);
                personalSettingResponse response = new personalSettingResponse(
                        0,
                        "用户更新密码成功",
                        true,
                        data
                );
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                personalSettingResponse response = new personalSettingResponse(
                        0,
                        "用户更新密码失败",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch(Exception e){
            personalSettingResponse response = new personalSettingResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
