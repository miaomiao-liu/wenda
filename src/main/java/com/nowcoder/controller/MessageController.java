package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: miaomiao
 * @Date: 2018/10/17
 * @Description:
 **/
@Controller
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    HostHolder hostHolder;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "/msg/list",method = RequestMethod.GET)
    public String getConversationList(Model model){
        try{
            if (hostHolder.getUser() == null){
                return "redirect:/reglogin";
            }
            int localUserId = hostHolder.getUser().getId();
            List<Message> conversationList = messageService.getConversationList(localUserId,0,10);
            List<ViewObject> conversations = new ArrayList<>();
            for (Message message : conversationList){
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                int targetId = message.getToId() == localUserId ? message.getFromId() : message.getToId();
                vo.set("user",userService.getUser(targetId));
                vo.set("unread",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            logger.error("获取列表失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(value = "/msg/detail",method = RequestMethod.GET)
    public String getConversationDetail(@RequestParam("conversationId") String conversationId,
                                      Model model){
        try{
            List<Message> messageList = messageService.getConversationDetail(conversationId,0,10);
            //只更新自己收到的消息是否已读
            messageService.updateConversationRead(conversationId,hostHolder.getUser().getId());
            List<ViewObject> messages = new ArrayList<>();
            for (Message message : messageList){
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                vo.set("user",userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("获取详情失败" + e.getMessage());
        }
        return "letterDetail";

    }

    @RequestMapping(value = "/msg/addMessage",method = RequestMethod.POST)
    @ResponseBody
    public String addMessage(@RequestParam("content") String content,
                             @RequestParam("toName") String toName){
        try{
            if (hostHolder.getUser() == null){
                return WendaUtil.getJsonString(999,"未登录");
            }
            User user = userService.selectByName(toName);
            if (user == null){
                return WendaUtil.getJsonString(1,"用户不存在");
            }
            Message message = new Message();
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setCreatedDate(new Date());
            messageService.addMessage(message);
            return WendaUtil.getJsonString(0);//0表示成功
        }catch (Exception e){
            logger.error("发送消息失败" + e.getMessage());
            return WendaUtil.getJsonString(1,"发送消息失败");
        }
    }

}
