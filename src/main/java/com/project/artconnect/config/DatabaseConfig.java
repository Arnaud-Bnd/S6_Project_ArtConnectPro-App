package com.project.artconnect.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfig {

    private static final Properties props = new Properties();

    static {
        try (InputStream in = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Impossible de charger db.properties : " + e.getMessage());
        }
    }

    public static final String URL = props.getProperty("db.url","jdbc:mysql://localhost:3306/artconnect_db");
    public static final String USER = props.getProperty("db.user", "root");
    public static final String PASSWORD = props.getProperty("db.password", "1234");
    public static final String DRIVER = props.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
}