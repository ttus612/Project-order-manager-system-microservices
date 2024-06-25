package com.example.userserver.service;

import com.example.userserver.models.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    void deleteUser(Long userId);

    User updateUser(User user);

    User getUserById(Long userId);

    List<User> getAllUsers();


}
