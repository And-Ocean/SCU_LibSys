package com.example.backend.controllers;

import com.example.backend.entity.User;
import com.example.backend.entity.userInfo.adminUserInfoRequest;
import com.example.backend.entity.userInfo.adminUserInfoResponse;
import com.example.backend.services.AccessService;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class Admin {
    @Autowired
    private UserService userService;
    @Autowired
    private AccessService accessService;
    @PostMapping("/user/list")
    public ResponseEntity<adminUserInfoResponse> userList(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            String keyword = request.getKeyword();
            int userId = accessService.getAuthenticatedId(accessToken);
            List<User> userInfo = userService.adminUserInfo(userId,keyword);
            List<adminUserInfoResponse.Data> data = new ArrayList<>();
            for(User user:userInfo){
                adminUserInfoResponse.Data temp = new adminUserInfoResponse.Data();
                temp.setUserName(user.getUsername());
                temp.setNickName(user.getNickname());
                temp.setUserSex(user.getSex());
                temp.setUserRole(user.getRole());
                temp.setUserAddress(user.getAddress());
                temp.setUserPhone(user.getPhone());
                data.add(temp);
            }
            adminUserInfoResponse response = new adminUserInfoResponse(
                    0,
                    "获取用户列表成功",
                    true,
                    data
            );
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    1,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/user/add")
    public ResponseEntity<adminUserInfoResponse> userAdd(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            String userName = request.getUserName();
            String nickName = request.getNickName();
            String userSex = request.getUserSex();
            String userPhone = request.getUserPhone();
            String userAddress = request.getUserAddress();
            int adminId = accessService.getAuthenticatedId(accessToken);
            int role = userService.userInfoByUserid(adminId).getRole();
            if(role==1){
                boolean isSuccess = userService.adminUserAdd(userName, nickName, userSex, userPhone, userAddress);
                adminUserInfoResponse response;
                if(isSuccess){
                    response = new adminUserInfoResponse(
                            0,
                            "添加用户成功",
                            true,
                            null
                    );
                }
                else{
                    response = new adminUserInfoResponse(
                            1,
                            "添加用户失败(该学号已经被注册)",
                            false,
                            null
                    );
                }
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            else{
                adminUserInfoResponse response = new adminUserInfoResponse(
                        1,
                        "无管理员权限",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/user/update")
    public ResponseEntity<adminUserInfoResponse> userUpdate(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            String userName = request.getUserName();
            String nickName = request.getNickName();
            String userSex = request.getUserSex();
            String userPhone = request.getUserPhone();
            String userAddress = request.getUserAddress();
            int userRole = request.getUserRole();
            int adminId = accessService.getAuthenticatedId(accessToken);
            int role = userService.userInfoByUserid(adminId).getRole();
            if(role==1) {
                boolean isSuccess = userService.adminUserUpdate(userName, nickName, userSex, userPhone, userAddress,userRole);
                adminUserInfoResponse response;
                if(isSuccess){
                    response = new adminUserInfoResponse(
                            0,
                            "更新用户信息成功",
                            true,
                            null
                    );
                }
                else{
                    response = new adminUserInfoResponse(
                            1,
                            "更新用户信息失败",
                            false,
                            null
                    );
                }
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                adminUserInfoResponse response = new adminUserInfoResponse(
                        1,
                        "无管理员权限",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @PostMapping("/user/delete")
    public ResponseEntity<adminUserInfoResponse> userDelete(@RequestBody adminUserInfoRequest request) {
        try {
            String accessToken = request.getAccessToken();
            String userName = request.getUserName();
            int adminId = accessService.getAuthenticatedId(accessToken);
            int role = userService.userInfoByUserid(adminId).getRole();
            if(role==1) {
                boolean isSuccess = userService.adminUserDelete(userName);
                adminUserInfoResponse response;
                if(isSuccess){
                    response = new adminUserInfoResponse(
                            0,
                            "删除用户成功",
                            true,
                            null
                    );
                }
                else{
                    response = new adminUserInfoResponse(
                            1,
                            "删除用户失败",
                            false,
                            null
                    );
                }
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            else{
                adminUserInfoResponse response = new adminUserInfoResponse(
                        1,
                        "无管理员权限",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        }
        catch (Exception e) {
            adminUserInfoResponse response = new adminUserInfoResponse(
                    2,
                    "服务器内部错误",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
