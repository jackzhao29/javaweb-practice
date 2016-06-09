package com.coldface.kafka.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.coldface.kafka.task.single.LogInfoKafkaConsumerTask;
import com.coldface.util.LoaderProperties;
import com.google.common.base.Joiner;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * 类LogInfoKafkaHandler.java的实现描述：TODO 类实现描述
 * @author coldface
 * @date   2016年6月4日下午9:41:47
 */
@Component
public class LogInfoKafkaHandler {
    private static Logger logger =LogManager.getLogger(LogInfoKafkaHandler.class);
    private ConsumerConnector consumer;
    @Value("kafka-loginfo.properties")
    private String propertiesFileName = "kafka-loginfo.properties";
    private static Properties kafkaProps = new Properties();
    private ExecutorService executor;
    
    public LogInfoKafkaHandler(){
        initConfig();
        boolean validate=paramValidate();
        if(!validate){
            logger.error("LogInfoKafkaHandler Invalid config");
            return;
        }
        ConsumerConfig consumerConfig=new ConsumerConfig(kafkaProps);
        this.consumer=Consumer.createJavaConsumerConnector(consumerConfig);
    }
    
    /**
     * 加载kafka配置信息
     */
    public void initConfig(){
        kafkaProps=LoaderProperties.loadPropertiesStatic(this.propertiesFileName);
        logger.info("LogInfoKafkaHandler-initConfig-kafkaProps={}", Joiner.on(",").withKeyValueSeparator("=").join(kafkaProps));
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
        logger.info("LogInfoKafkaHandler kafka start.....");
        //根据partition数量创建消费线程个数
        int threadNum=Integer.parseInt(kafkaProps.getProperty("partition.count"));
        String topic=kafkaProps.getProperty("topic");
        Map<String, Integer> topicCountMap=new HashMap<String,Integer>();
        topicCountMap.put(topic, new Integer(threadNum));
        Map<String, List<KafkaStream<byte[],byte[]>>> consumerMap=consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[],byte[]>> newStreams=consumerMap.get(topic);
        //创建kafka线程池
        executor=Executors.newFixedThreadPool(threadNum);
        for(final KafkaStream<byte[],byte[]> stream: newStreams){
            executor.submit(new LogInfoKafkaConsumerTask(stream));
        }
       
    }
    
}
