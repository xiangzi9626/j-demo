package com.example.rabbitmq.dead2;

import com.example.rabbitmq.rabbitmq.RabbitMQConnection;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

//死信队列 消息被拒绝
public class Consumer2 {
    private static final String DEAD_QUEUE = "dead_queue";
    public static void main(String[] args) throws IOException, TimeoutException, IOException, TimeoutException {
        // 1.创建连接
        Connection connection = RabbitMQConnection.getConnection();
        // 2.设置通道
        Channel channel = connection.createChannel();
          channel.queueDeclare(DEAD_QUEUE, true, false, false, null);
         DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("消费者1获取消息:" + msg);
//                // 消费者完成 消费者通知给mq服务器端删除该消息
                //参数1 消息的标识
                //basicAck第二个参数 是否批量应答 建议使用false
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        /* 3.监听队列
        * 参数 1 队列名称
        * 2 是否自动确认
        */
        channel.basicConsume(DEAD_QUEUE, false, defaultConsumer);

    }
}
