package com.lyh.provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
public class HelloProducer {
    private static final String QUEUE_NAME = "hello_queue";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection()) {
            try (Channel channel = connection.createChannel()) {
                // 声明队列（如果不存在则创建）
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                // 发送消息
                String message = "Hello RabbitMQ!";
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("发送消息: " + message);
            }
        }
    }
}