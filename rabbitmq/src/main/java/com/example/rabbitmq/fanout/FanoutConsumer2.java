package com.example.rabbitmq.fanout;

import com.example.rabbitmq.rabbitmq.RabbitMQConnection;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//广播模式
public class FanoutConsumer2 {
    private static final String QUEUE_NAME2 = "fanout_queue2";

    public static void main(String[] args) throws IOException, TimeoutException, IOException, TimeoutException {
        // 1.创建连接
        Connection connection = RabbitMQConnection.getConnection();
        // 2.设置通道
        Channel channel = connection.createChannel();
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("消费者获取消息:" + msg);
//                // 消费者完成 消费者通知给mq服务器端删除该消息
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        // 3.监听队列
        channel.basicConsume(QUEUE_NAME2, false, defaultConsumer);

    }
}
