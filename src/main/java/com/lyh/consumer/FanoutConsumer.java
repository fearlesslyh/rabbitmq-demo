package com.lyh.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
public class FanoutConsumer {
    private static final String FANOUT_EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        // 设置主机地址
        connectionFactory.setHost("localhost");

        Channel channel;
        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建通道
        channel = connection.createChannel();


        //声明交换机
        // 声明交换机类型为FANOUT
        channel.exchangeDeclare(FANOUT_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // 创建临时队列并绑定
        // 创建临时队列1
        String queue1 = channel.queueDeclare().getQueue();
        // 创建临时队列2
        String queue2 = channel.queueDeclare().getQueue();
        // 将队列1绑定到交换机
        channel.queueBind(queue1, FANOUT_EXCHANGE_NAME, "");
        // 将队列2绑定到交换机
        channel.queueBind(queue2, FANOUT_EXCHANGE_NAME, "");

        // 定义消费者1的回调函数
        DeliverCallback callback = (tag, delivery) -> {
            // 打印消费者1收到的消息
            System.out.println("消费者1收到： " + new String(delivery.getBody()));
        };
        // 消费者1开始消费消息
        channel.basicConsume(queue1, true, callback, tag -> {
        });

        // 定义消费者2的回调函数
        DeliverCallback callback2 = (tag, delivery) -> {
            // 打印消费者2收到的消息
            System.out.println("消费者2收到： " + new String(delivery.getBody()));
        };
        // 消费者2开始消费消息
        channel.basicConsume(queue2, true, callback2, tag -> {
        });

        // 打印等待接受广播消息
        System.out.println("等待接受广播消息......");
    }

    ;
}
