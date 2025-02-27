package com.lyh.provider;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
public class DirectProducer {
    private static final String EXCHANGE_NAME = "direct_exchange";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            // 声明 Direct 交换机
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            // 发送不同路由键的消息
            channel.basicPublish(EXCHANGE_NAME, "error", null, "错误日志".getBytes());
            channel.basicPublish(EXCHANGE_NAME, "info", null, "普通日志".getBytes());
            System.out.println("发送日志消息完成");
        }
    }
}
