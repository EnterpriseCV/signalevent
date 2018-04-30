package nju.zxl.signalevent.util;


import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {
    static String fileName = "db.properties";
    static Properties propertie = null;

    static String dbDriver;
    static String dbUrl;
    static String dbUserName;
    static String dbPassword;
    static String modelPackageName;

    static {
        try {
            propertie = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            propertie.load(loader.getResourceAsStream(fileName));
            dbDriver = propertie.getProperty("dbDriver");
            dbUrl = propertie.getProperty("dbUrl");
            dbUserName = propertie.getProperty("dbUserName");
            dbPassword = propertie.getProperty("dbPassword");
            modelPackageName = propertie.getProperty("modelPackageName");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String getDbDriver() {
        return dbDriver;
    }

    public static void setDbDriver(String dbDriver) {
        PropertyUtils.dbDriver = dbDriver;
    }

    public static String getDbUrl() {
        return dbUrl;
    }

    public static void setDbUrl(String dbUrl) {
        PropertyUtils.dbUrl = dbUrl;
    }

    public static String getDbUserName() {
        return dbUserName;
    }

    public static void setDbUserName(String dbUserName) {
        PropertyUtils.dbUserName = dbUserName;
    }

    public static String getDbPassword() {
        return dbPassword;
    }

    public static void setDbPassword(String dbPassword) {
        PropertyUtils.dbPassword = dbPassword;
    }

    public static String getModelPackageName() {
        return modelPackageName;
    }

    public static void setModelPackageName(String modelPackageName) {
        PropertyUtils.modelPackageName = modelPackageName;
    }
}
