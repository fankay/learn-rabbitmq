package com.kaishengit.rabbitmq.pubsub;

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

            //创建/使用交换机
            channel.exchangeDeclare("orders","fanout");
            //获取临时队列的名称
            String tempQueueName = channel.queueDeclare().getQueue();
            //绑定临时队列到交换机
            channel.queueBind(tempQueueName,"orders","");

            //消息消费者
            Consumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    System.out.println("消费者B: " + new String(body));
                }
            };
            //从临时队列中消费消息，并自动确认
            channel.basicConsume(tempQueueName,true,consumer);

        } catch (IOException | TimeoutException ex) {
            ex.printStackTrace();
        }

    }
}
