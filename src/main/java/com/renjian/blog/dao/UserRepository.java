package com.renjian.blog.dao;

import com.renjian.blog.module.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndPassword(String username,String password);

    User findByUsername(String username);

}
