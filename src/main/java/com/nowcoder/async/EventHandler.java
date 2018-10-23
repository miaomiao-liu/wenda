package com.nowcoder.async;

import java.util.List;

/**
 * @Author: miaomiao
 * @Date: 2018/10/23
 * @Description: 处理事件的接口
 **/
public interface EventHandler {
    void doHandler(EventModel eventModel);

    List<EventType> getSupperEventTypes();
}
