package com.demo.activemq.top;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * Created by zhangshengjian on 2019-06-20.
 */
public class Consumer {

    public static void main(String[] args) throws JMSException, IOException {

        // 创建一个 ConnectionFactory 对象连接 MQ 服务器
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD, ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        // 创建一个连接对象
        Connection connection = connectionFactory.createConnection();
        // 开启连接
        connection.start();
        // 使用 Connection 对象创建一个 Session 对象
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 创建一个 Destination 对象，Topic 对象
        Topic topic = session.createTopic("test-topic");
        // 使用 Session 对象创建一个消费者对象
        MessageConsumer consumer = session.createConsumer(topic);
        // 接收消息
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                // 打印结果
                TextMessage textMessage = (TextMessage) message;
                String text;
                try {
                    text = textMessage.getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.out.println("ActiveMQ Test Topic Consumer Start...");
        // 等待接收消息
        System.in.read();
        // 关闭资源
        consumer.close();
        session.close();
        connection.close();
    }
}
