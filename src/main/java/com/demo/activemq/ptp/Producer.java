package com.demo.activemq.ptp;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by zhangshengjian on 2019-06-20.
 */
public class Producer {

    public static void main(String[] args) throws JMSException {
        /*
         * 实现步骤：
         * 1.建立 ConnectionFactory 工厂对象，需要填入用户名、密码、连接地址（一般使用默认，如果没有修改的话）
         * 2.通过 ConnectionFactory 对象创建一个 Connection 连接，并且调用 Connection 的 start 方法开启连接，Connection 方法默认是关闭的
         * 3.通过 Connection 对象创建 Session 会话（上下文环境对象），用于接收消息，参数1是是否启用事务，参数2是签收模式，一般设置为自动签收
         * 4.通过 Session 对象创建 Destination 对象，指的是一个客户端用来指定生产消息目标和消费消息来源的对象。在 PTP 的模式中，Destination 被称作队列，
         * 在 Pub/Sub 模式中，Destination 被称作主题（Topic）
         * 5.通过 Session 对象创建消息的发送和接收对象（生产者和消费者）
         * 6.通过 MessageProducer 的 setDeliver 方法为其设置持久化或者非持久化特性
         * 7.使用 JMS 规范的 TextMessage 形式创建数据（通过 Session 对象），并用 MessageProducer 的 send 方法发送数据。客户端同理，记得关闭
         */
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_USER,
                ActiveMQConnectionFactory.DEFAULT_PASSWORD, ActiveMQConnectionFactory.DEFAULT_BROKER_URL);

        Connection connection = connectionFactory.createConnection();
        connection.start();

        // 创建一个 session，第一个参数:是否支持事务
        // 如果为 true，则会忽略第二个参数，被 jms 服务器设置为 SESSION_TRANSACTED
        // 如果为 false，第二个参数值可为 Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE 其中一个
        // Session.AUTO_ACKNOWLEDGE 为自动确认，客户端发送和接收消息不需要做额外的工作。哪怕是接收端发生异常，也会被当作正常发送成功
        // Session.CLIENT_ACKNOWLEDGE 为客户端确认。客户端接收到消息后，必须调用 javax.jms.Message 的 acknowledge 方法。jms 服务器才会当作发送成功，并删除消息
        // Session.DUPS_OK_ACKNOWLEDGE 允许副本的确认模式。一旦接收方应用程序的方法调用从处理消息处返回，会话对象就会确认消息的接收；而且允许重复确认
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("test-queue");
        MessageProducer producer = session.createProducer(destination);
        // 设置生产者的模式，有两种可选
        // DeliveryMode.PERSISTENT 当 activemq 关闭的时候，队列数据将会被保存
        // DeliveryMode.NON_PERSISTENT 当 activemq 关闭的时候，队列里面的数据将会被清空
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        for (int i = 0; i < 100; i++) {
            TextMessage textMessage = session.createTextMessage();
            textMessage.setText("ActiveMQ Test Queue Produce Message " + i);
            producer.send(textMessage);
        }
        connection.close();
    }
}
