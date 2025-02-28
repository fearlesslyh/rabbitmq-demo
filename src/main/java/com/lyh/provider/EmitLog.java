package com.lyh.provider;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
public class EmitLog {

    private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        // 设置主机地址
        factory.setHost("localhost");
        // 创建连接
        try (Connection connection = factory.newConnection();
             // 创建通道
             Channel channel = connection.createChannel()) {
            // 声明交换机
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

            // 如果没有传入参数，则默认发送"info: Hello World!"
            String message = argv.length < 1 ? "info: Hello World!" :
                    // 否则将参数拼接成一个字符串
                    String.join(" ", argv);
            // 打印发送的消息
            System.out.println(" [x] Sent '" + message + "'");
            // 发送消息
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes(StandardCharsets.UTF_8));

        }
    }
}