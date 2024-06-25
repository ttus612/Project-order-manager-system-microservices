package com.example.userserver.service.impl;

import com.example.userserver.configResponseMessage.UserNotFoundException;
import com.example.userserver.models.User;
import com.example.userserver.repository.UserRepository;
import com.example.userserver.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String REDIS_KEY = "USER";
    @Override
    public User saveUser(User user) {
        System.out.println("Called saveUser() into DB & Redis");
        try {
            userRepository.save(user);
            redisTemplate.opsForHash().put(REDIS_KEY, user.getId().toString(), user);
            return user;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteUser(Long userId) {
        System.out.println("Called deleteUser() from DB & redis: " + userId);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userRepository.delete(user);
        redisTemplate.opsForHash().delete(REDIS_KEY, userId.toString());
    }


    @Override
    public User updateUser(User user) {
        User existingUser = userRepository.findById(user.getId()).get();
        if (existingUser != null) {
            System.out.println("Called updateUser() from DB : " + existingUser);
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPhoneNumber(user.getPhoneNumber());
            existingUser.setEmail(user.getEmail());
            existingUser.setAddress(user.getAddress());

            existingUser = userRepository.save(existingUser);

            System.out.println("Called updateUser() into Redis : " + existingUser);
            redisTemplate.delete(REDIS_KEY + "::" + existingUser.getId().toString());
            redisTemplate.opsForHash().put(REDIS_KEY, existingUser.getId().toString(), existingUser);
        }
        return existingUser;
    }

    @Override
    public User getUserById(Long userId) {
        System.out.println("Called getUserById() from redis: " + userId);
        return (User) redisTemplate.opsForHash().get(REDIS_KEY, userId.toString());
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList;
        userList = redisTemplate.opsForHash().values(REDIS_KEY);
        return userList;
    }
}
