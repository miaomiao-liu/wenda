package com.nowcoder;

import com.nowcoder.dao.QuestionDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

/**
 * User: miaomiao
 * Date: 18-6-12
 * Description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WendaApplication.class)
public class InitDataBaseTest {

    @Autowired
    UserDao userDao;

    @Autowired
    QuestionDao questionDao;

    @Test
    public void initDatabase(){

        User user = new User();

//        for (int i = 0;i < 10; i++) {
//            user.setName("miaomiao"+i);
//            user.setPassword("1234");
//            user.setSalt("mkd");
//            user.setHeadUrl("http/:123.12");
//
//            userDao.addUser(user);
//        }
        user = userDao.selectById(3);
        System.out.println(user.getName());

        userDao.deleteByID(5);
        Assert.assertNull(userDao.selectById(5));


        Question question = new Question();

//        for (int i = 0; i < 10; i++){
//            question.setUserId(i);
//            question.setCreatedDate(new Date());
//            question.setContent("hhhhh"+i);
//            question.setTitle("title"+i);
//            questionDao.addQuestion(question);
//        }
        for(Question question1 :questionDao.selectLatestQuestions(0,0,10)){
            System.out.println(question1.getTitle());
        }

    }

}
