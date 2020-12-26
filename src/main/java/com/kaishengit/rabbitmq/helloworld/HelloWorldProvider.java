package com.kaishengit.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class HelloWorldProvider {
    public static void main(String[] args) {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        //rabbitmq 主机地址
        connectionFactory.setHost("192.168.85.142");
        //rabbitmq 虚拟机名称
        connectionFactory.setVirtualHost("myhost");
        //rabbitmq 账号
        connectionFactory.setUsername("demo");
        //rabbitmq 密码
        connectionFactory.setPassword("demo");
        //rabbitmq 端口号
        connectionFactory.setPort(5672);

        try {
            //获取链接对象
            Connection connection = connectionFactory.newConnection();
            //建立消息通道
            Channel channel = connection.createChannel();
            //消息通道绑定消息队列,如果队列不存在，则会自动创建
            boolean durable = true; //队列持久化
            channel.queueDeclare("messageQueue",durable,false,false,null);
            //发送消息 MessageProperties.PERSISTENT_TEXT_PLAIN 设置持久化消息
            channel.basicPublish("","messageQueue", MessageProperties.PERSISTENT_TEXT_PLAIN,"Hello,message".getBytes(StandardCharsets.UTF_8));
            System.out.println("发送完成");
            //关闭通道和链接
            channel.close();
            connection.close();
        } catch (IOException | TimeoutException ex) {
            ex.printStackTrace();
        }
    }
}
