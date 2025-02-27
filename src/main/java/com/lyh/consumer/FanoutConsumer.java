package com.lyh.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.lyh.provider.FanoutProducer.EXCHANGE_NAME;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
@SuppressWarnings("value")
public class FanoutConsumer {
    private static final String FANOUT_EXCHANGE_NAME = "fanout_exchange";
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        // 创建临时队列并绑定
        String queue1 = channel.queueDeclare().getQueue();
        String queue2 = channel.queueDeclare().getQueue();
        channel.queueBind(queue1, EXCHANGE_NAME, "");
        channel.queueBind(queue2, EXCHANGE_NAME, "");

        DeliverCallback callback=(tag,delivery)->{
            System.out.println("消费者1收到： "+new String(delivery.getBody()));
        };
        channel.basicConsume(queue1,true,callback,tag->{});

        DeliverCallback callback2=(tag,delivery)->{
            System.out.println("消费者2收到： "+new String(delivery.getBody()));
        };
        channel.basicConsume(queue2,true,callback2,tag->{});

        System.out.println("等待接受广播消息......");
    };
}
