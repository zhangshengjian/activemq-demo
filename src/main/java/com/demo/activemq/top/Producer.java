package com.demo.activemq.top;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 *
 * 消费者需要提前订阅，所以先运行消费者才能消费
 *
 * Created by zhangshengjian on 2019-06-20.
 */
public class Producer {

    public static void main(String[] args) throws JMSException {

        // 创建一个连接工厂对象，需要指定服务的 ip 及端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD, ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        // 使用工厂对象创建一个 Connection 对象
        Connection connection = connectionFactory.createConnection();
        // 开启连接，调用 Connection 对象的 start 方法
        connection.start();
        // 创建一个 Session 对象
        // 第一个参数：是否开启事务。如果 true 开启事务，第二个参数无意义。一般不开启事务 false
        // 第二个参数：应答模式。自动应答或者手动应答。一般自动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 使用 Session 对象创建一个 Destination 对象。两种形式 queue、topic，现在应该使用 topic
        Topic topic = session.createTopic("test-topic");
        // 使用 Session 对象创建一个 Producer 对象
        MessageProducer producer = session.createProducer(topic);
        // 创建一个 Message 对象，可以使用 TextMessage
        for (int i = 0; i < 100; i++) {
            TextMessage textMessage = session.createTextMessage("ActiveMQ Test Topic Produce Message " + i);
            // 发送消息
            producer.send(textMessage);
            System.out.println(textMessage);
        }
        // 关闭资源
        producer.close();
        session.close();
        connection.close();
    }
}
