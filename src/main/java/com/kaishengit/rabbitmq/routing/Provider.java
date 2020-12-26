package com.kaishengit.rabbitmq.routing;

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
            //创建/使用 名称为orders的交换机，交换机类型为direct
            channel.exchangeDeclare("orders","direct");
            //发布消息到orders交换机中，指定routing key的名称
            channel.basicPublish("orders","routerA",null,"路由A".getBytes(StandardCharsets.UTF_8));
            channel.basicPublish("orders","routerB",null,"路由B".getBytes(StandardCharsets.UTF_8));

            channel.close();
            connection.close();
        } catch (IOException | TimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
