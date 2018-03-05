package com.abs.mq.service;

import com.abs.exception.SystemException;

public interface MQService {
    
    void publishBlockMessage(String message) throws SystemException;

    void publishTransactionMessage(String message) throws SystemException;

    void publishMessage(String topic, String message) throws SystemException;

}
