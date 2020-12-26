package com.kaishengit.rabbitmq.workqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerB {
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
            //每次消费1条消息
            channel.basicQos(1);

            channel.queueDeclare("messageQueue",false,false,false,null);

            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("Consumer B 收到消息： " + new String(body));
                    //手动确认消息
                    //envelope.getDeliveryTag() 用来获取消息的Id
                    //false表示一次确认单条消息
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            };

            //获取消息，将autoAck设置为false，表示将手动确认消息
            channel.basicConsume("messageQueue",false,consumer);

        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
