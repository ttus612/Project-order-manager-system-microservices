package com.example.userserver.controller;

import com.example.userserver.configResponseMessage.UserNotFoundException;
import com.example.userserver.models.User;
import com.example.userserver.configResponseMessage.ResponseMessage;
import com.example.userserver.service.UserService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@EnableCaching
@RateLimiter(name = "userService")
public class UserController {
    private UserService userService;

        @PostMapping
        public ResponseEntity<User> saveUser(@RequestBody User user, @RequestHeader("loggedInUser") String userName) {
            System.out.println("Logged in user details: " + userName);
            User savedUser = userService.saveUser(user);
            if (savedUser == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
            }
        }

    @DeleteMapping("/{id}")
    @CacheEvict(key = "#userId", value = "USER")
    public ResponseEntity deleteUser(@PathVariable("id") Long userId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ResponseMessage("Xóa người dùng thành công"));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseMessage(e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Cacheable(key = "#userId", value = "USER")
    public User getUserById(@PathVariable("id") Long userId, @RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        User user = userService.getUserById(userId);
        return  user;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(@RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in user details: " + userName);
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
