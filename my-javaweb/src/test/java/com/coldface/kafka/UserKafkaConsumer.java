package com.coldface.kafka;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.ImmutableMap;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;

/**
 * 类UserKafkaConsumer.java的实现描述：User消息消费者测试类
 * 
 * @author coldface
 * @date 2016年6月4日下午8:34:28
 */
public class UserKafkaConsumer {

    private static final String ZOOKEEPER = "127.0.0.1:2181";
    private static final String TOPIC = "user-info";
    private static final int PARTITION_NUM = 1;
    private static final String GROUP_ID = "user-info-coldface";

    public static void main(String[] args) {
        // specify some consumer properties
        Properties props = new Properties();
        props.put("zookeeper.connect", ZOOKEEPER);
        props.put("group.id", GROUP_ID);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");

        // Create the connection to the cluster
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);

        // create 4 partitions of the stream for topic “test”, to allow 4
        // threads to consume
        Map<String, List<KafkaStream<byte[], byte[]>>> topicMessageStreams =
                consumerConnector.createMessageStreams(ImmutableMap.of(TOPIC, PARTITION_NUM));
        List<KafkaStream<byte[], byte[]>> streams = topicMessageStreams.get(TOPIC);

        System.out.println(streams.size());
        System.out.println(streams.toString());
        for (final KafkaStream<byte[], byte[]> stream : streams) {
            ConsumerIterator<byte[], byte[]> it = stream.iterator();
            while (it.hasNext()) {
                MessageAndMetadata<byte[], byte[]> msg = it.next();
                String message = new String(msg.message());
                System.out.println("message==" + message);
            }
        }
    }


}
