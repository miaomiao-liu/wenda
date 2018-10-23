package com.nowcoder.service;

import com.nowcoder.dao.MessageDao;
import com.nowcoder.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: miaomiao
 * @Date: 2018/10/17
 * @Description:
 **/
@Service
public class MessageService {

    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message) {
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message) > 0 ? message.getId() : 0;
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDao.selectConversationDetail(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDao.selectConversationList(userId, offset, limit);
    }

    public int getConversationUnreadCount(int userId,String conversationId){
        return messageDao.getConversationUnreadCount(conversationId,userId);
    }

    public int updateConversationRead(String conversationId,int userId){
        return messageDao.updateConversationRead(conversationId,userId);
    }
}
