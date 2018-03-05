package com.test.fabric;

import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.abs.exception.BusinessException;
import com.abs.exception.SystemException;
import com.abs.fabric.FabricPublisher;



@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(locations = {"classpath:spring-mvc.xml","classpath:spring-activeMQ.xml","classpath:spring-mongodb.xml"})
public class FabricPublisherTest {
    @Autowired
    private FabricPublisher fabricPublisher;
	@Test
	public void testLinkFabric() throws SystemException, BusinessException, InvalidArgumentException, ProposalException, InterruptedException {
	    fabricPublisher.getClass();
	}
	
	
}
