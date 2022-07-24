package com.example.rabbitmq.fanout;

import com.example.rabbitmq.rabbitmq.RabbitMQConnection;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

//广播模式
public class FanoutProducer {
    private static final String QUEUE_NAME1 = "fanout_queue1";
    private static final String QUEUE_NAME2 = "fanout_queue2";
    private static final String EXCHANGE_NAME = "fanout_exchange";

    public static void main(String[] args) {
        try {
            //1.创建一个新连接
            Connection connection = RabbitMQConnection.getConnection();
            //2.设置channel
            Channel channel = connection.createChannel();
            /* 声明交换机
             * ~ exchange 交换器的名称。
             * ~ type 交换器的类型，常见的如 fanout direct topic
             * ~ durable: 设置是否持久 durab 设置为 true 表示持久化， 反之是非持久 。持 可以将交换器存盘，在服务器重启 的时候不会丢失 关信息。
             * ~ autoDelete 设置是否自动删除。 autoDelete 设置为 true 表示自动删除。自动 删除的前提是至少有 个队列或者交换器与这个交换器绑定 之后所有与这个交换器绑 定的队列或者交换器都与 解绑。注意不能错误地 这个参数理解为 "当与此交换器 连接的客户端都断开时 RabbitMQ 会自动 除本交换器
             * ~ internal 设置是否是内置的。如果设置为 true ，则表示是内置的交换器，客户端程 序无法直接发送消息到这个交换器中，只能通过交换器路由到交换器这种方式。
             * ~ argument 其他一些结构化参数，比如 ternate exchange
              */
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT,true,false,false,null);
            /* 声明队列
             * 参数说明
             * 1. queue： 队列的名称 ；
             * 2. durable： 是否持久化 ；
             * 当durable = false时，队列非持久化。因为队列是存放在内存中的，所以当RabbitMQ重启或者服务器重启时该队列就会丢失 ；
             * 当durable = true时，队列持久化。当RabbitMQ重启后队列不会丢失。RabbitMQ退出时它会将队列信息保存到 Erlang自带的Mnesia数据库中，当RabbitMQ重启之后会读取该数据库 ；
             * 3. exclusive： 是否排外的
             * 当exclusive = true则设置队列为排他的。如果一个队列被声明为排他队列，该队列 仅对首次声明它的连接（Connection）可见，是该Connection私有的，类似于加锁，并在连接断开connection.close()时自动删除 ；
             * 当exclusive = false则设置队列为非排他的，此时不同连接（Connection）的管道Channel可以使用该队列 ；
             * 4. autoDelete： 是否自动删除 ；如果autoDelete = true，当所有消费者都与这个队列断开连接时，这个队列会自动删除。注意： 不是说该队列没有消费者连接时该队列就会自动删除，因为当生产者声明了该队列且没有消费者连接消费时，该队列是不会自动删除的。
              * 5. arguments： 设置队列的其他一些参数，如 x-rnessage-ttl 、x-expires 、x-rnax-length 、x-rnax-length-bytes、 x-dead-letter-exchange、 x-deadletter-routing-key 、 x-rnax-priority 等。
             */
            channel.queueDeclare(QUEUE_NAME1, true, false, false, null);
            channel.queueDeclare(QUEUE_NAME2, true, false, false, null);
            /*绑定队列
            * 1 队列名称
            * 2 交换机名称
            * 3 路由key topic模式下#号匹配一个或多个单词 *号匹配一个单词
             */
            channel.queueBind(QUEUE_NAME1,EXCHANGE_NAME,"");
            channel.queueBind(QUEUE_NAME2,EXCHANGE_NAME,"");
            //开启发布确认
            channel.confirmSelect();
            //3.发送消息
            String msg = "你好世界 广播模式";
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
            boolean result = channel.waitForConfirms();
            if (result) {
                System.out.println("fanout模式消息投递成功");
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
