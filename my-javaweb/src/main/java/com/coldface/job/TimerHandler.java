package com.coldface.job;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 类TimerHandler.java的实现描述：定时器类
 * @author coldface
 * @date   2016年6月4日下午10:05:21
 */
@Component
public class TimerHandler {
    
    private static final Logger logger=LogManager.getLogger(TimerHandler.class);

    public void sendMail(){
       logger.info("发送邮件定时器....");
    }
}
