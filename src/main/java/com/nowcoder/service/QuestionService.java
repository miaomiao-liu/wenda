package com.nowcoder.service;

import com.nowcoder.dao.QuestionDao;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

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
    @Autowired
    SensitiveService sensitiveService;

    public int addQuestion(Question question){
        //过滤HTML标签，防止串改
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

        return questionDao.addQuestion(question) > 0 ? question.getUserId(): 0;
    }
    public List<Question> getLatestQuestions(int id,int offset,int limit){
        return questionDao.selectLatestQuestions(id,offset,limit);
    }
}
