package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.service.QuestionService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: miaomiao
 * @Date: 2018-10-11
 * @Description:
 **/
@Controller
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @RequestMapping(value = "/question/add")
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                                @RequestParam("content") String content){
        try{
            Question question = new Question();
            question.setCommentCount(0);
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            if (hostHolder.getUser() == null){
                //没登录返回999，前端会跳转到登录界面
                return WendaUtil.getJsonString(999);
            }else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0){
                return WendaUtil.getJsonString(0);
            }

        }catch (Exception e){
            logger.error("添加题目失败" + e.getMessage());
        }
        return WendaUtil.getJsonString(1,"失败");
    }
}
