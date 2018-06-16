package com.nowcoder.service;

import com.nowcoder.dao.QuestionDao;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: miaomiao
 * Date: 18-6-16
 * Description:
 */
@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public List<Question> getLatestQuestions(int id,int offset,int limit){
        return questionDao.selectLatestQuestions(id,offset,limit);
    }
}
