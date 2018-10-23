package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: miaomiao
 * @Date: 2018/10/23
 * @Description: 往redis中添加任务
 **/
@Component
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try {
            String key = RedisKeyUtil.getEventqueueKey();
            jedisAdapter.lpush(key,JSONObject.toJSONString(eventModel));
            return true;
        }catch (Exception e){
            return false;
        }

    }

}
