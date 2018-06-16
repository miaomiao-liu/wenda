package com.nowcoder.service;

import com.nowcoder.dao.UserDao;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * User: miaomiao
 * Date: 18-6-16
 * Description:
 */
@Service
public class UserService {


    @Autowired
    UserDao userDao;


    public User getUser(int id){

        return userDao.selectById(id);
    }
}
