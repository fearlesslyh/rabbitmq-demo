package com.lyh.provider;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Date;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
public class FanoutProducer {
    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection()) {
            try (Channel channel = connection.createChannel()) {
                // 声明 Fanout 交换机
                channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
                // 发送消息到交换机（无需指定路由键）
                String message = "广播消息: " + new Date();
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
                System.out.println("发送广播: " + message);
            }
        }
    }
}