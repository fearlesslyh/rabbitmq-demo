package com.lyh.provider;

/**
 * @author 梁懿豪
 * @version 1.0
 * @Github https://github.com/fearlesslyh
 */
import com.rabbitmq.client.*;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

public class RPCClient implements AutoCloseable {
    // 定义连接和通道
    private Connection connection;
    private Channel channel;
    // 定义请求队列名称
    private String requestQueueName = "rpc_queue";

    // 构造函数，创建连接和通道
    public RPCClient() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    // 主函数，创建RPCClient对象，发送请求并接收响应
    public static void main(String[] args) throws TimeoutException{
        try (RPCClient client = new RPCClient()) {
            for (int i = 0; i < 5; i++) {
                String message = Integer.toString(i);
                System.out.println("客户端发送请求: " + message);
                String response = client.call(message);
                System.out.println("收到响应: '" + response + "'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 发送请求并接收响应
    public String call(String message) throws IOException, InterruptedException {
        // 生成唯一标识
        final String corrId = UUID.randomUUID().toString();
        // 声明回复队列
        String replyQueueName = channel.queueDeclare().getQueue();

        // 设置消息属性
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();

        // 发送消息
        channel.basicPublish("", requestQueueName, props, message.getBytes("UTF-8"));

        // 创建阻塞队列，用于接收响应
        final BlockingQueue<String> response = new ArrayBlockingQueue<>(1);
        // 消费回复队列
        String ctag = channel.basicConsume(replyQueueName, true, (consumerTag, delivery) -> {
            if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                response.offer(new String(delivery.getBody(), "UTF-8"));
            }
        }, consumerTag -> {});

        // 获取响应
        String result = response.take();
        // 取消消费
        channel.basicCancel(ctag);
        return result;
    }

    // 关闭连接
    @Override
    public void close() throws TimeoutException{
        try {
            connection.close();
        } catch ( IOException e) {
            e.printStackTrace();
        }
    }
}