package com.lyh.consumer;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "direct_logs";

    public static void main(String[] argv) throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置主机地址
        factory.setHost("localhost");
        // 创建连接
        Connection connection = factory.newConnection();
        // 创建通道
        Channel channel = connection.createChannel();

        // 声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        // 声明队列
        String queueName = channel.queueDeclare().getQueue();

        // 判断参数是否为空
        if (argv.length < 1) {
            System.err.println("Usage: ReceiveLogsDirect [info] [warning] [error]");
            System.exit(1);
        }

        // 遍历参数，绑定队列到交换机
        for (String severity : argv) {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        // 定义回调函数
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            // 获取消息内容
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            // 打印消息
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        // 消费消息
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }
}