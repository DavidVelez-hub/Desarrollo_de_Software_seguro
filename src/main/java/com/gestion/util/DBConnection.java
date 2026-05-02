package com.gestion.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.err.println("❌ Error: No se encontró application.properties en el classpath.");
            } else {
                properties.load(input);
                // Opcional: cargar explícitamente el driver
                Class.forName("com.mysql.cj.jdbc.Driver");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Error al inicializar DBConnection.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.user"),
                    properties.getProperty("db.password")
            );
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener nueva conexión con la base de datos.");
            e.printStackTrace();
        }
        return connection;
    }
}