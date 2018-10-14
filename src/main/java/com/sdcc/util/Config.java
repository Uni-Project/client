package com.sdcc.util;

import java.util.Properties;

public class Config
{
    Properties configFile;

    public Config() {
        configFile = new Properties();
        try {
            configFile.load(this.getClass().getClassLoader().
                    getResourceAsStream("config.properties"));
        } catch(Exception eta) {
            eta.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return this.configFile.getProperty(key);
    }
}
