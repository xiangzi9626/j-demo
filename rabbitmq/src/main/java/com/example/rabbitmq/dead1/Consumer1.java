package com.example.rabbitmq.dead1;

import com.example.rabbitmq.rabbitmq.RabbitMQConnection;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

//死信队列 超出队列长度
public class Consumer1 {
    private static final String NORMAL_QUEUE = "normal_queue";
    private static final String DEAD_QUEUE = "dead_queue";
    private static final String NORMAL_EXCHANGE="normal_exchange";
    private static final String DEAD_EXCHANGE="dead_exchange";

    public static void main(String[] args) throws IOException, TimeoutException, IOException, TimeoutException {
        // 1.创建连接
        Connection connection = RabbitMQConnection.getConnection();
        // 2.设置通道
        Channel channel = connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT,true,false,false,null);
        channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT,true,false,false,null);
        //声明队列 参数要和生产者声明队列参数一致
        Map<String,Object> arguments=new HashMap<>();
        //过期时间 毫秒 可以在生产者指定过期时间
        //arguments.put("x-message-ttl",10000);
        //设置死信交换机
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信routingkey
        arguments.put("x-dead-letter-routing-key","dead_routing_key");
        //设置队列的长度
        arguments.put("x-max-length",6);
        channel.queueDeclare(NORMAL_QUEUE, true, false, false, arguments);
        channel.queueDeclare(DEAD_QUEUE, true, false, false, null);
        //绑定普通的交换机和队列
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"normal_routing_key");
        //绑定死信的交换机和队列
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"dead_routing_key");
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
        channel.basicConsume(NORMAL_QUEUE, false, defaultConsumer);

    }
}
