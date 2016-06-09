package com.coldface.kafka.task.batch;

import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.coldface.entity.UserBo;
import com.coldface.util.Constants;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

/**
 * 类UserKafkaConsumerTask.java的实现描述：消费kafka消息的线程
 * 
 * @author coldface
 * @date 2016年5月30日下午5:20:52
 */
public class UserKafkaConsumerTask implements Runnable {
    private static Logger logger = LogManager.getLogger(UserKafkaConsumerTask.class);

    private BlockingQueue<UserBo> userBoQueue;
    private static int BATCH_QUEUE_SIZE = 20;
    private KafkaStream<byte[], byte[]> kafkaStream;

    public UserKafkaConsumerTask(KafkaStream<byte[], byte[]> kafkaStream, int threadNum,
            BlockingQueue<UserBo> userBoQueue) {
        this.kafkaStream = kafkaStream;
        this.userBoQueue = userBoQueue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        logger.info("UserKafkaConsumerTask-run......");
        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
        while (it.hasNext()) {
            try {
                // 判断kafka开关
                if (Constants.KAFKA_CONSUMER_FLAG_TRUE
                        .equals(Constants.KAFKA_CONSUMER_SWITCH_FLAG)) {
                    MessageAndMetadata<byte[], byte[]> msg = it.next();
                    String message = new String(msg.message());
                    addMessageQueue(message);
                } else {
                    // 获取目前队列里的元素个数，如果不够20个元素，继续添加，直到队列里的元素达到20个，批量插入一次（保证停止服务时队列里的数据都存入DB）
                    int size = userBoQueue.size();
                    // 如果队列里没有元素，让线程睡眠1分钟
                    if (size == 0) {
                        System.out.println("Thread.sleep");
                        Thread.sleep(60 * 1000);
                        continue;
                    }
                    int sum = BATCH_QUEUE_SIZE - size;
                    for (int i = 0; i < sum; i++) {
                        MessageAndMetadata<byte[], byte[]> msg = it.next();
                        String message = new String(msg.message());
                        addMessageQueue(message);
                    }
                }
            } catch (Exception ex) {

            }
        }
    }

    /**
     * 将kafka消息放入队列中
     * 
     * @param message
     */
    public void addMessageQueue(String message) {
        UserBo userBo = JSON.parseObject(message, UserBo.class);
        try {
            userBoQueue.put(userBo);
        } catch (InterruptedException e) {
            logger.error("userBo存入队列异常", e);
        }

    }

}
