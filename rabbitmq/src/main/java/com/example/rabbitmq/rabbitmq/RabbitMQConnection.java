package com.example.rabbitmq.rabbitmq;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @ClassName RabbitMQConnection
 * @Author 蚂蚁课堂余胜军 QQ644064779 www.mayikt.com
 * @Version V1.0
 **/
public class RabbitMQConnection {

    /**
     * 创建连接
     *
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        //1.创建connectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //2.配置Host
        connectionFactory.setHost("127.0.0.1");
        //3.设置Port
        connectionFactory.setPort(5672);
        //4.设置账户和密码
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        //5.设置VirtualHost
        connectionFactory.setVirtualHost("/");
        return connectionFactory.newConnection();
    }
}
