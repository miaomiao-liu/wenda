package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @RequestMapping(value = "/question/add")
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title,
                              @RequestParam("content") String content) {
        try {
            Question question = new Question();
            question.setCommentCount(0);
            question.setContent(content);
            question.setTitle(title);
            question.setCreatedDate(new Date());
            if (hostHolder.getUser() == null) {
                //没登录返回999，前端会跳转到登录界面
                return WendaUtil.getJsonString(999);
            } else {
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question) > 0) {
                return WendaUtil.getJsonString(0);
            }

        } catch (Exception e) {
            logger.error("添加题目失败" + e.getMessage());
        }
        return WendaUtil.getJsonString(1, "失败");
    }

    @RequestMapping("/question/{qid}")
    public String questionDetail(@PathVariable("qid") int qid,
                                 Model model) {
        Question question = questionService.selectById(qid);
        model.addAttribute("question", question);
        model.addAttribute("user", userService.getUser(question.getUserId()));

        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentList){
            ViewObject vo = new ViewObject();
            vo.set("comment",comment);
            vo.set("user",userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);

        return "detail";
    }
}
