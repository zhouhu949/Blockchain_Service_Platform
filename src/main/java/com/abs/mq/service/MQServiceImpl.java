package com.abs.mq.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.abs.exception.SystemException;

@Service(value = "mqServiceImpl")
public class MQServiceImpl implements MQService {
    Logger log = org.slf4j.LoggerFactory.getLogger(MQServiceImpl.class);
    @Autowired
    private JmsTemplate blockJmsTemplate;
    @Autowired
    private JmsTemplate transactionJmsTemplate;

    @Override
    public void publishBlockMessage(String message) throws SystemException {
        sendForJmsTemplate(blockJmsTemplate, message);
    }

    @Override
    public void publishTransactionMessage(String message) throws SystemException {
        sendForJmsTemplate(transactionJmsTemplate, message);
    }

    @Override
    public void publishMessage(String topic, String message) throws SystemException {
        sendForTopic(blockJmsTemplate, message, topic);
    }

    private void sendForJmsTemplate(final JmsTemplate jmsTemplate, final String sendmsg)
            throws SystemException {
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage msg = session.createTextMessage();
                // 设置消息内容
                msg.setText(sendmsg);
                log.info("发送消息：" + sendmsg + " -" + sendmsg.length() + "byte");
                return msg;
            }
        });
    }

    private void sendForTopic(final JmsTemplate jmsTemplate, final String sendmsg,
            final String topicName) throws SystemException {
        jmsTemplate.send(topicName, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage msg = session.createTextMessage();
                // 设置消息内容
                msg.setText(sendmsg);
                log.info("发送消息：" + sendmsg + " -" + sendmsg.length() + "byte");
                return msg;
            }
        });
    }

}

// end
