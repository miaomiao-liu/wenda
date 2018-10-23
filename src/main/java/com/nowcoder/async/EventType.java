package com.nowcoder.async;

/**
 * @Author: miaomiao
 * @Date: 2018/10/23
 * @Description: 事件类型
 **/
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;
    EventType(int value) {this.value = value;}

    public int getValue(){
        return value;
    }
}
