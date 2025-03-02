package com.lyh.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
public class TopicConsumer {
    private static final String EXCHANGE_NAME = "topic_exchange";
    private static final String QUEUE_NAME = "order_events";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //设置主机地址
        connectionFactory.setHost("localhost");

        Channel channel;
        //创建连接
        Connection connection = connectionFactory.newConnection();
        //创建通道
        channel = connection.createChannel();

        //声明Topic类型的交换机和队列
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        //绑定通配符（匹配所有以order.开头的事件）
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "order.*");

        //设置回调函数
        DeliverCallback callback = (tag, delivery) -> {
            System.out.println("收到订单事件： " + new String(delivery.getBody()));
        };
        //开始消费消息
        channel.basicConsume(QUEUE_NAME, true, callback, consumerTag -> {
        });
        System.out.println("等待接受订单事件......");

    }
}
