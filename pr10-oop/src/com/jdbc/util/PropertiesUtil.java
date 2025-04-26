package com.jdbc.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {
    private static final Properties PROPERTIES = new Properties();
    private PropertiesUtil() {}

    static {
        loadProperties();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try(InputStream resource = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")){
            PROPERTIES.load(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Properties getProperties() {
        return PROPERTIES;
    }
}
