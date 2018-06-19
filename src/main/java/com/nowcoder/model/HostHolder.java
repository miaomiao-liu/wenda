package com.nowcoder.model;

import org.springframework.stereotype.Component;

/**
 * User: miaomiao
 * Date: 18-6-16
 * Description:每个线程都有一个公共的入口，只有自己的本地变量
 */
@Component
public class HostHolder {

    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }

}
