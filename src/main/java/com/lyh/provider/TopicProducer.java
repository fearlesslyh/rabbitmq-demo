package com.lyh.provider;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
public class TopicProducer {
    private static final String EXCHANGE_NAME = "topic_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        try (Connection connection = connectionFactory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            channel.basicPublish(EXCHANGE_NAME, "order.create", null, "订单创建成功".getBytes());
            channel.basicPublish(EXCHANGE_NAME,"order.pay.success",null,"订单支付成功".getBytes());
            System.out.println("发送订单消息完成!");
        }
    }
}
