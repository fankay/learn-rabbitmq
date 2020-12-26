package com.kaishengit.rabbitmq.workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Provider {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.85.142");
        connectionFactory.setVirtualHost("myhost");
        connectionFactory.setUsername("demo");
        connectionFactory.setPassword("demo");
        connectionFactory.setPort(5672);

        try {
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare("messageQueue",false,false,false,null);

            //循环发送10条消息到队列中
            for (int i = 0; i < 10; i++) {
                channel.basicPublish("","messageQueue",null,("Hello,message -> " + i).getBytes(StandardCharsets.UTF_8));
            }

            System.out.println("发送完成");

            channel.close();
            connection.close();
        } catch (IOException | TimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
