package com.abs.config;

import java.io.IOException;
import java.util.Properties;


public class Config {
    private Properties p = new Properties();


    public void load(Config config, String path) {
        Properties p = loadConfig(path);
        config.setP(p);
    }

    public String getProperty(String propertyName) {
        return p.getProperty(propertyName);
    }

    public Properties getP() {
        return p;
    }

    public void setP(Properties p) {
        this.p = p;
    }


    private static Properties loadConfig(String fileName) {
        Properties prop = new Properties();
        try {
            if (fileName.startsWith("/")) {
                prop.load(ConfigHelper.class.getResourceAsStream(fileName));
            } else {
                prop.load(ConfigHelper.class.getClassLoader().getResourceAsStream(fileName));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }
}
// end
