package com.coldface.kafka.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.coldface.entity.UserBo;
import com.coldface.kafka.task.batch.UserBlockingQueueConsumerTask;
import com.coldface.kafka.task.batch.UserKafkaConsumerTask;
import com.coldface.util.LoaderProperties;
import com.google.common.base.Joiner;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * 类UserKafkaHandler.java的实现描述：TODO 类实现描述
 * @author coldface
 * @date   2016年5月30日下午6:11:25
 */
@Component
public class UserKafkaHandler {
    private static Logger logger = LogManager.getLogger(UserKafkaHandler.class);
    private ConsumerConnector consumer;
    @Value("kafka-user.properties")
    private String propertiesFileName = "kafka-user.properties";
    private static Properties kafkaProps = new Properties();
    private ExecutorService executor;
    private BlockingQueue<UserBo> userBoQueue;
    
    public UserKafkaHandler(){
        initConfig();
        boolean validate=paramValidate();
        if(!validate){
            logger.error("UserKafkaHandler Invalid config");
            return;
        }
        ConsumerConfig consumerConfig=new ConsumerConfig(kafkaProps);
        this.consumer=Consumer.createJavaConsumerConnector(consumerConfig);
        userBoQueue=new ArrayBlockingQueue<UserBo>(20);
    }
    
    /**
     * 加载kafka配置信息
     */
    public void initConfig(){
        kafkaProps=LoaderProperties.loadPropertiesStatic(this.propertiesFileName);
        logger.info("UserKafkaHandler-initConfig-kafkaProps={}", Joiner.on(",").withKeyValueSeparator("=").join(kafkaProps));
    }
    
    /**
     * 校验kafka配置信息
     * @return
     */
    public boolean paramValidate(){
        String groupId=kafkaProps.getProperty("group.id");
        String topic=kafkaProps.getProperty("topic");
        String partitionCount=kafkaProps.getProperty("partition.count");
        if(groupId==null || topic==null || partitionCount==null){
            return false;
        }
        return true;
    }
    
    public void startKafkaTask(){
        logger.info("UserKafkaHandler kafka start.....");
        //根据partition数量创建消费线程个数
        int threadNum=Integer.parseInt(kafkaProps.getProperty("partition.count"));
        String topic=kafkaProps.getProperty("topic");
        Map<String, Integer> topicCountMap=new HashMap<String,Integer>();
        topicCountMap.put(topic, new Integer(threadNum));
        Map<String, List<KafkaStream<byte[],byte[]>>> consumerMap=consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[],byte[]>> newStreams=consumerMap.get(topic);
        //创建kafka线程池
        executor=Executors.newFixedThreadPool(threadNum);
        int num=0;
        for(final KafkaStream<byte[],byte[]> stream: newStreams){
            executor.submit(new UserKafkaConsumerTask(stream,num++,userBoQueue));
        }
        
        //创建队列线程池
        executor=Executors.newFixedThreadPool(threadNum);
        executor.submit(new UserBlockingQueueConsumerTask(userBoQueue)); 
    }
    
    
}
