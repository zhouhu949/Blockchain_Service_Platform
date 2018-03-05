package com.abs.config;

public class ConfigHelper {
    private static MQConfig mqConfig;
    private static JDBCConfig jdbcConfig;
    private static FabricConfig fabricConfig;
    
    public static MQConfig getMQConfig() {
        if (mqConfig == null) {
            synchronized (ConfigHelper.class) {
                if (mqConfig == null) {
                    mqConfig = new MQConfig();
                    mqConfig.load(mqConfig, MQConfig.FILE_PATH);
                }
            }
        }
        return mqConfig;
    }


    public static JDBCConfig getJDBCConfig() {
        if (jdbcConfig == null) {
            synchronized (ConfigHelper.class) {
                if (jdbcConfig == null) {
                    jdbcConfig = new JDBCConfig();
                    jdbcConfig.load(jdbcConfig, JDBCConfig.FILE_PATH);
                }
            }
        }
        return jdbcConfig;
    }
    
    public static FabricConfig getFabricConfig() {
        if (fabricConfig == null) {
            synchronized (ConfigHelper.class) {
                if (fabricConfig == null) {
                	fabricConfig = new FabricConfig();
                	fabricConfig.load(fabricConfig, FabricConfig.FILE_PATH);
                }
            }
        }
        return fabricConfig;
    }
}

// end
