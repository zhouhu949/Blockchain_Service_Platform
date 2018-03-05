package com.test.subscription.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SendActivemqUtil {

    private static ExecutorService threadPool=Executors.newFixedThreadPool(50);
    
    // ConnectionFactory ：连接工厂，JMS用它创建连接
    private static ConnectionFactory connectionFactory;
    // Connection ：JMS客户端到JMS Provider的连接
    private static Connection connection = null;
    static {
        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现
        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, "tcp://localhost:61616");
    }
    public static void sendForJms(String msg, Map<String, Object> map) {
        // Session：一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // MessageProducer：消息发送者
        MessageProducer producer;
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 获取session，FirstQueue是一个服务器的queue
            destination = session.createTopic("Online.Notice.Topic");
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置持久化
            producer.setDeliveryMode(DeliveryMode.PERSISTENT);
            // 构造消息
            sendMessage(session, producer, msg, map);
            // session.commit();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    // TODO Auto-generatedcatch block
                    e.printStackTrace();
                }
            }
        }
    }

    private static void sendMessage(Session session, MessageProducer producer, String msg,
            Map<String, Object> map) throws Exception {
        TextMessage message = session.createTextMessage(msg);
        Set<String> keys = map.keySet();
        for (String key : keys) {
            message.setObjectProperty(key, map.get(key));
        }
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    producer.send(message);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                
            }
        });
    }
}

// end
