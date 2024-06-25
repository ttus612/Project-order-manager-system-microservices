package com.example.userserver.configResponseMessage;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long userId) {
        super("Người dùng với ID " + userId + " không tồn tại");
    }
}
