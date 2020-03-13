package com.renjian.blog.util;

import org.springframework.util.DigestUtils;

public class MD5Utils {

    private final static String secret="||";

    public static String code(String username,String password){

        StringBuilder str=new StringBuilder();
        str.append(username);
        str.append(secret);
        str.append(password);
        return DigestUtils.md5DigestAsHex(str.toString().getBytes());
    }

}
