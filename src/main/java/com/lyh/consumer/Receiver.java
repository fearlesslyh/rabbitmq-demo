package com.lyh.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
public class Receiver {
    private final static String Exchange_Name = "logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置主机地址
        connectionFactory.setHost("localhost");

        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建通道
        Channel channel = connection.createChannel();
        // 声明交换机
        channel.exchangeDeclare(Exchange_Name, BuiltinExchangeType.FANOUT);
        // 声明队列，这里是建一个临时队列
        String queueName = channel.queueDeclare().getQueue();
        // 绑定队列到交换机
        channel.queueBind(queueName, Exchange_Name, "severity");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 定义回调函数
        DeliverCallback deliverCallback = (tag, delivery) -> {
            // 获取消息内容
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            // 打印消息内容
            System.out.println(" [x] Received '" + message + "'");
        };

        // 消费消息
        channel.basicConsume(queueName, true, deliverCallback, tag -> {
        });
    }
}
