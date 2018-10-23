package com.nowcoder.service;

import com.nowcoder.dao.CommentDao;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: miaomiao
 * @Date: 2018/10/16
 * @Description:
 **/
@Service
public class CommentService {

    @Autowired
    CommentDao commentDao;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentsByEntity(int entityId, int entityTpye) {
        return commentDao.selectCommentByEntity(entityId, entityTpye);
    }

    public int addComment(Comment comment) {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDao.addComment(comment) > 0 ? comment.getId() : 0;
    }

    public int getCommentCount(int entityId, int entityTpye) {
        return commentDao.getCommentCount(entityId, entityTpye);
    }

    public int deleteComment(int commentId) {
        return commentDao.updateStatus(commentId, 1);
    }

    public Comment getCommentById(int id) {
        return commentDao.selectCommentById(id);
    }
}

