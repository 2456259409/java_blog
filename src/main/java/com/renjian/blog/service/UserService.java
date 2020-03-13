package com.renjian.blog.service;

import com.renjian.blog.module.User;

public interface UserService {

    User checkUser(String username,String password);

    User getByUsername(String username);

    void save(User user);

}
