package com.nowcoder.dao;

import com.nowcoder.model.Message;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: miaomiao
 * @Date: 2018/10/17
 * @Description:
 **/
@Mapper
@Repository
public interface MessageDao {

    String TABLE_NAME = " message ";
    String INSERT_FIELDS = " from_id,to_id,content,has_read,conversation_id,created_date ";
    String SELECT_FIELDS = "id" + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME, " ( ",INSERT_FIELDS, " ) values ( #{fromId},#{toId},#{content},#{hasRead},#{conversationId},#{createdDate} )"})
    int addMessage(Message message);


    @Select({"select ",SELECT_FIELDS, "from ",TABLE_NAME,
            "where conversation_id = #{conversationId} order by created_date desc limit #{offset},#{limit}"})
    List<Message> selectConversationDetail(@Param("conversationId") String conversationId,
                                           @Param("offset") int offset,
                                           @Param("limit") int limit);

    @Select({"select",INSERT_FIELDS,",count(*) as id from ( select * from", TABLE_NAME ,
            "where from_id = #{userId} or to_id = #{userId} order by created_date desc) t " +
                    "group by conversation_id order by created_date desc limit #{offset},#{limit}"})
    List<Message> selectConversationList(@Param("userId") int userId,
                                               @Param("offset") int offset,
                                               @Param("limit") int limit);

    @Select({"select count(id) from",TABLE_NAME,"where has_read = 0 and to_id = #{userId} and conversation_id = #{conversationId}"})
    int getConversationUnreadCount(@Param("conversationId") String conversationId,
                                   @Param("userId") int userId);

    @Update({"update",TABLE_NAME,"set has_read = 1 where conversation_id = #{conversationId} and to_id = #{userId}"})
    int updateConversationRead(@Param("conversationId") String conversationId,
                               @Param("userId") int userId);
}
