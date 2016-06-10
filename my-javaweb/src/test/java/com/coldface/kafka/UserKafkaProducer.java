package com.coldface.kafka;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.Charsets;

import com.google.common.collect.Lists;
import com.google.common.io.Files;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

/**
 * 类UserKafkaProducer.java的实现描述：user消息生产者测试类
 * @author coldface
 * @date   2016年6月4日下午8:21:16
 */
public class UserKafkaProducer {
    private static final String TOPIC = "user-info";
    private static final String BROKER_LIST = "127.0.0.1:9092";
    private static final String SERIALIZER_CLASS = "kafka.serializer.StringEncoder";
    
    public static void main(String[] args) {
        System.out.println("start....");
        Properties props = new Properties();
        props.put("serializer.class", SERIALIZER_CLASS);
        props.put("metadata.broker.list", BROKER_LIST);
        
        ProducerConfig config = new ProducerConfig(props);
        Producer<String, String> producer = new Producer<String, String>(config);
        sendLocalFile(producer);
        
        System.out.println("end...");
    }

    public static void sendLocalFile(Producer producer){
        String testFilePath = "my-javaweb/src/test/java/com/coldface/kafka/userinfo.txt";
        File testFile = new File(testFilePath);
        List<KeyedMessage<String,String>> messages = 
                new ArrayList<KeyedMessage<String, String>>();
        List<String> lines = Lists.newArrayList();
        try {
            lines = Files.readLines(testFile, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int i =1;
        for (String line : lines) {
            System.out.println(i+"==="+line);
            messages.add(new KeyedMessage<String, String>
            (TOPIC, line));
            i++;
            if(i%30==0){
                producer.send(messages);
            }
        }
    }
}
