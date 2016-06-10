package com.coldface.util;

/**
 * 类Constants.java的实现描述：常量工具类
 * @author coldface
 * @date   2016年5月30日下午5:44:45
 */
public class Constants {
    //kafka需要消费消息
    public static final String KAFKA_CONSUMER_FLAG_TRUE = "true";
    //kafka不需要消费消息
    public static final String KAFKA_CONSUMER_FLAG_FALSE = "false";
    //动态控制kakfa消费消息的开关
    public static volatile String KAFKA_CONSUMER_SWITCH_FLAG="true";
    

}
