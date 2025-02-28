package com.lyh.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
public class DirectConsumer {
    private static final String EXCHANGE_NAME = "direct_exchange";
    private static final String QUEUE_NAME = "error_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Channel channel;
        Connection connection = connectionFactory.newConnection();
            channel = connection.createChannel();

        // 声明 Direct 交换机和队列
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 绑定队列到交换机（只接收 error 路由键）
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "error");

        DeliverCallback callback = (tag, delivery) -> {
            System.out.println("错误日志: " + new String(delivery.getBody()));
        };
        channel.basicConsume(QUEUE_NAME, true, callback, consumerTag -> {
        });
        System.out.println("等待接收错误日志...");
    }
}
