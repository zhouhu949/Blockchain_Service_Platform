package com.abs.config;

public class JDBCConfig extends Config{
    public final static String FILE_PATH = "jdbc.properties";
    
    private final static String MYSQL_DRIVER = "dirver";
    private final static String MYSQL_URL = "url";
    private final static String MYSQL_USERNAME = "username";
    private final static String MYSQL_PASSWORD = "password";

    public String getMysqlDriver(){
        return getProperty(MYSQL_DRIVER);
    }
    public String getMysqlUrl(){
        return getProperty(MYSQL_URL);
    }
    public String getMysqlUserName(){
        return getProperty(MYSQL_USERNAME);
    }
    public String getMysqlPassword(){
        return getProperty(MYSQL_PASSWORD);
    }
    
    
}

