package com.driver.services.impl;

import com.driver.model.User;
import com.driver.repository.UserRepository;
import com.driver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;


    @Override
    public void deleteUser(Integer userId) {
   User user=userRepository.findById(userId).get();
   userRepository.delete(user);

    }

    @Override
    public User updatePassword(Integer userId, String password) {
        User user=userRepository.findById(userId).get();
        user.setPassword(password);
        userRepository.save(user);
        return user;
    }

    @Override
    public void register(String name, String phoneNumber, String password) {
        User user=new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
      userRepository.save(user);
    }
}
