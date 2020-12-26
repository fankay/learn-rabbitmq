package com.kaishengit.rabbitmq.helloworld;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HelloWorldConsumer {
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
            //绑定通道和队列
            boolean durable = true;//和消息提供者保持一致
            channel.queueDeclare("messageQueue",durable,false,false,null);

            //消息消费者
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("收到消息： " + new String(body));
                }
            };

            //获取消息
            channel.basicConsume("messageQueue",true,consumer);

        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
