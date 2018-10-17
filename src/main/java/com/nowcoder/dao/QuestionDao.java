package com.nowcoder.dao;

import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: miaomiao
 * Date: 18-6-12
 * Description:
 */
@Mapper
@Repository
public interface QuestionDao {

    String TABLE_NAME = " question ";
    String INSERT_FIELDS = " title,content,created_date,user_id,comment_count ";
    String SELECT_FIELDS = "id," + INSERT_FIELDS;

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELDS,
            ") values(#{title},#{content},#{createdDate},#{userId},#{commentCount})"})
    int addQuestion(Question question);

    @Select({"select" , SELECT_FIELDS ,"from" ,TABLE_NAME ,"where id=#{id}"})
    Question selectById(int id);


    List<Question> selectLatestQuestions(@Param("userId") int id,
                                         @Param("offset") int offset,
                                         @Param("limit") int limit);

    @Update({"update ", TABLE_NAME ," set comment_count = #{CommentCount} where id = #{id}"})
    int updateCommentCount(@Param("id")int id,
                           @Param("CommentCount") int CommentCount);

}
