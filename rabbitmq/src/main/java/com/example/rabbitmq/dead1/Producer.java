package com.example.rabbitmq.dead1;

import com.example.rabbitmq.rabbitmq.RabbitMQConnection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

//死信队列 超出队列长度
//死信队列 先启动消费者 创建交换机和队列 生产者没设置
//模拟时消费者1启动后关闭 然后生产者发布消息 超出队列长度 消息会进入到死信队列
public class Producer {
    private static final String NORMAL_EXCHANGE="normal_exchange";

    public static void main(String[] args) {
        try {
            //1.创建一个新连接
            Connection connection = RabbitMQConnection.getConnection();
            //2.设置channel
            Channel channel = connection.createChannel();
            //开启发布确认
            channel.confirmSelect();
            //3.发送消息
            for (int i = 0; i <10 ; i++) {

            String msg = "消息"+i;
            channel.basicPublish(NORMAL_EXCHANGE,"normal_routing_key",null, msg.getBytes());
            }
            boolean result = channel.waitForConfirms();
            if (result) {
                System.out.println("消息投递成功");
            } else {
                System.out.println("消息投递失败");
            }
            channel.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
