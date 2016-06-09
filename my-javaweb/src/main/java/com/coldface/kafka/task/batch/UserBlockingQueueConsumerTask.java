package com.coldface.kafka.task.batch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.coldface.entity.UserBo;
import com.coldface.service.UserService;
import com.coldface.util.SpringUtils;

/**
 * 类UserBlockingQueueConsumerTask.java的实现描述：消费队列的线程
 * 
 * @author coldface
 * @date 2016年5月30日下午7:32:42
 */
public class UserBlockingQueueConsumerTask implements Runnable {

    private static final Logger logger =
            LoggerFactory.getLogger(UserBlockingQueueConsumerTask.class);
    private BlockingQueue<UserBo> userBoQueue;
    private UserService userService;

    public UserBlockingQueueConsumerTask(BlockingQueue<UserBo> userBoQueue) {
        this.userBoQueue = userBoQueue;
        this.userService = SpringUtils.getBean(UserService.class);
    }


    public void run() {
        List<UserBo> list = new ArrayList<UserBo>();
        while (true) {
            try {
                list = new ArrayList<UserBo>();
                for (int i = 0; i < 20; i++) {
                    list.add(userBoQueue.take());
                }
                // 批量执行插入操作
                boolean result = userService.batchSaveUserBo(list);

            } catch (Exception ex) {
                logger.error("UserBlockingQueueConsumerTask-run", ex);
                continue;
            }
        }
    }
}
