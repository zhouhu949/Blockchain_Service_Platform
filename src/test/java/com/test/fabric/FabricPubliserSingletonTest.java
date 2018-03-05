package com.test.fabric;

import org.junit.Test;

import com.abs.exception.BusinessException;
import com.abs.exception.SystemException;

public class FabricPubliserSingletonTest {
    
    @Test
    public void testSendInvokeTx(){
        
//        String txID = FabricPublisherSingleton.getFabricPublisher().sendTransaction("LOAN_APPROVE", "orgJD", "asset01", "a", "CAT_LOAN", "4868fa70cc142c67d8e3f9b6676058769a175b6aea18f693828558cf7bba50ce", "a", "a");
//        String txID = FabricPublisherSingleton.getFabricPublisher().sendTransaction("LOAN_RESULT_NOTIFY", "orgJY", "asset01", "a", "CAT_LOAN", "4171dc622aae928045cd14167d4f35224d848452db5c0067db6741952345f4a4", "a", "a");
//        String txID = FabricPublisherSingleton.getFabricPublisher().sendTransaction("LOAN_RESULT_CONFIRM", "orgJD", "asset01", "a", "CAT_LOAN", "fbd8c4f843b8a4d419eefe3fdba2deb3d8a5e1c75fd41d6b9332b98546f54457", "a", "a");        
//    	String txID = FabricPublisherSingleton.getFabricPublisher().sendTransaction("MORTGAGE_DOC_UPLOAD", "orgJY", "asset01", "a", "CAT_MORT", "88e58bcb727a05cd4c68566592559b6ff866df16059f839d1d505c554df864af", "a", "a");        
//    	String txID = FabricPublisherSingleton.getFabricPublisher().sendTransaction("MORTGAGE_DOC_REJECT", "orgJD", "asset01", "a", "CAT_MORT", "2c03cf0d2f9d07a55d17e42afdca041e0fe281aa6a2f0f694f6f39cd77a04e21", "a", "a");        
    	try {
            String txID = FabricPublisherSingleton.getFabricPublisher().sendInvokeTransaction("LOAN_APPLY", "orgJY", "asset_01", "a", "CAT_LOAN", "", "a", "a");
          
//    		String txID = FabricPublisherSingleton.getFabricPublisher().sendInvokeTransaction("LOAN_APPROVE", "orgJD", "asset01", "a", "CAT_LOAN", "3ec3ac626f12b74b4f8ba9f01261e731fce98ad0a8cfe9ebe07ee5623ee49a1d", "a", "a");

            //            String txID = FabricPublisherSingleton.getFabricPublisher().sendTransaction("MORTGAGE_DOC_UPLOAD", "orgJY", "asset01", "a", "CAT_MORT", "ad0ed87ca8d3300ca7f9f308af94b41df84dddba2f18e2abb27e9ac7d4157f50", "a", "a");
            System.out.println(txID);
    	} catch (BusinessException e) {
			System.out.println(e.getErrorMsg());
		} catch (SystemException e) {
			System.out.println(e.getErrorMsg());
		}       
    }

    @Test
    public void testSendQueryTx() throws Exception{
    	String queryResult = FabricPublisherSingleton.getFabricPublisher().sendQueryTransaction("asset_01_CAT_LOAN");
    	System.out.println(queryResult);
    }
    
    @Test
    public void testGetBlockHash() throws Exception{
    	FabricPublisherSingleton.getFabricPublisher().getCurrentBlockHash();
    }

    @Test
    public void testGetBlockHashByHeight() throws Exception{
    	FabricPublisherSingleton.getFabricPublisher().getBlockHashByHeight(4);
    }
    
}

