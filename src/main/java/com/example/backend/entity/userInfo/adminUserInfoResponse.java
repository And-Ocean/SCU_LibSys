package com.example.backend.entity.userInfo;

import java.util.List;

public class adminUserInfoResponse {
    private int status;
    private String message;
    private boolean success;

    private List<Data> data;

    public adminUserInfoResponse(int status, String message, boolean success, List<Data> data) {
        this.status = status;
        this.message = message;
        this.success = success;
        this.data = data;
    }
    // 内部类 Data
    public static class Data {
        private String nickName;



        private String userAddress;
        private String userSex;
        private int userRole;
        private String userPhone;


        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserAddress() {
            return userAddress;
        }

        public void setUserAddress(String userAddress) {
            this.userAddress = userAddress;
        }

        public String getUserSex() {
            return userSex;
        }

        public void setUserSex(String userSex) {
            this.userSex = userSex;
        }
        public int getUserRole() {
            return userRole;
        }

        public void setUserRole(int userRoleName) {
            this.userRole = userRoleName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }
    }
        public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }
}
