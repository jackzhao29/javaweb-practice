package com.coldface.kafka.task.single;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.coldface.entity.LogInfoBo;
import com.coldface.service.LogInfoService;
import com.coldface.util.SpringUtils;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

/**
 * 类LogInfoKafkaConsumerTask.java的实现描述：TODO 类实现描述
 * 
 * @author coldface
 * @date 2016年6月4日下午9:32:06
 */
public class LogInfoKafkaConsumerTask implements Runnable {

    private static Logger logger = LogManager.getLogger(LogInfoKafkaConsumerTask.class);

    private KafkaStream<byte[], byte[]> kafkaStream;
    private LogInfoService logInfoService;

    public LogInfoKafkaConsumerTask(KafkaStream<byte[], byte[]> kafkaStream) {
        this.kafkaStream = kafkaStream;
        this.logInfoService = SpringUtils.getBean(LogInfoService.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        logger.info("LogInfoKafkaConsumerTask-run......");
        ConsumerIterator<byte[], byte[]> it = kafkaStream.iterator();
        while (it.hasNext()) {
            try {
                MessageAndMetadata<byte[], byte[]> msg = it.next();
                String message = new String(msg.message());
                logInfoService.addLogInfo(JSON.parseObject(message, LogInfoBo.class));
            } catch (Exception ex) {
                logger.error("LogInfoKafkaConsumerTask-run异常",ex);
            }
        }

    }

}
