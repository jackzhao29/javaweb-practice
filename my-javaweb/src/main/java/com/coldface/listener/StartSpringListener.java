package com.coldface.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.coldface.kafka.handler.LogInfoKafkaHandler;
import com.coldface.kafka.handler.UserKafkaHandler;

/**
 * 类StartSpringListener.java的实现描述：spring监听开始类
 * 
 * @author coldface
 * @date 2016年6月4日下午7:51:28
 */
public class StartSpringListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserKafkaHandler userKafkaHandler;
    @Autowired
    private LogInfoKafkaHandler logInfoKafkaHandler;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // root application context 没有parent，他就是老大.
        if (event.getApplicationContext().getParent() == null) {
            //用户信息消费者
            userKafkaHandler.startKafkaTask();
            //日志信息消费者
            logInfoKafkaHandler.startKafkaTask();
        }
    }
}
