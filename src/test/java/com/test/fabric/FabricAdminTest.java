package com.test.fabric;


import org.junit.Test;

import com.deploy.fabric.AdminInitType;
import com.deploy.fabric.FabricAdmin;

public class FabricAdminTest {
    @Test
    public void testInit() throws Exception{
        
        FabricAdmin.getFabricAdmin(AdminInitType.INIT_ONLY);
    }
    
    @Test
    public void testInitWithJoin() throws Exception{
        
        FabricAdmin.getFabricAdmin(AdminInitType.INIT_WITH_JOIN_PEER);
    }
    
    @Test
    public void testInitWithCreateJoin() throws Exception{
        
        FabricAdmin.getFabricAdmin(AdminInitType.INIT_WITH_CREATE_CHANNEL_JOIN_PEER);
    }
    
    @Test
    public void testInstall() throws Exception{
        
        FabricAdmin.getFabricAdmin(AdminInitType.INIT_ONLY).installChaincode();
    }
    
    @Test
    public void testInstantiate() throws Exception{
        
        FabricAdmin.getFabricAdmin(AdminInitType.INIT_ONLY).instantiateChaincode();
    }
    
    @Test
    public void testUpgrade() throws Exception{
        
        FabricAdmin.getFabricAdmin(AdminInitType.INIT_ONLY).upgradeChaincode();
    }
}
