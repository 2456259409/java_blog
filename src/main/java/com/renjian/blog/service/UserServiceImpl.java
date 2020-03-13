package com.renjian.blog.service;

import com.renjian.blog.dao.UserRepository;
import com.renjian.blog.module.User;
import com.renjian.blog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Override
    public User checkUser(String username, String password) {
        User user=null;
        user=userRepository.findByUsernameAndPassword(username, MD5Utils.code(username,password));
        return user;
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void save(User user) {
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setPower(0);
        user.setPassword(MD5Utils.code(user.getUsername(),user.getPassword()));
        userRepository.save(user);
    }
}
