package com.onlineshop.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/** MySQL connection helper. Creates the tables and sample data on first use. */
public final class DBUtil {
    private static final Properties PROPS = new Properties();
    private static volatile boolean ready = false;

    static {
        try (InputStream in = DBUtil.class.getResourceAsStream("/db.properties")) {
            if (in != null) PROPS.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private DBUtil() {}

    private static String setting(String env, String key, String def) {
        String v = System.getenv(env);
        if (v == null || v.isBlank()) v = PROPS.getProperty(key, def);
        return v;
    }

    private static Connection open() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC driver not found", e);
        }
        return DriverManager.getConnection(
                setting("DB_URL", "db.url", "jdbc:mysql://localhost:3306/shopease"),
                setting("DB_USER", "db.user", "root"),
                setting("DB_PASSWORD", "db.password", ""));
    }

    /** Returns a new connection (the schema is created before the first one). */
    public static Connection getConnection() throws SQLException {
        if (!ready) initDatabase();
        return open();
    }

    /** Creates the tables if they do not exist and inserts the sample products once. */
    public static synchronized void initDatabase() throws SQLException {
        if (ready) return;
        try (Connection con = open(); Statement st = con.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS products ("
                    + "id INT PRIMARY KEY, name VARCHAR(100) NOT NULL, "
                    + "category VARCHAR(50) NOT NULL, price DECIMAL(10,2) NOT NULL, "
                    + "description VARCHAR(255), emoji VARCHAR(16)"
                    + ") DEFAULT CHARSET=utf8mb4");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS orders ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, customer_name VARCHAR(100) NOT NULL, "
                    + "phone VARCHAR(15) NOT NULL, address VARCHAR(500) NOT NULL, "
                    + "total DECIMAL(10,2) NOT NULL, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ") DEFAULT CHARSET=utf8mb4");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS order_items ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY, order_id INT NOT NULL, "
                    + "product_id INT NOT NULL, product_name VARCHAR(100) NOT NULL, "
                    + "quantity INT NOT NULL, price DECIMAL(10,2) NOT NULL, "
                    + "FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE"
                    + ") DEFAULT CHARSET=utf8mb4");
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM products")) {
                rs.next();
                if (rs.getInt(1) == 0) seedProducts(con);
            }
        }
        ready = true;
    }

    private static void seedProducts(Connection con) throws SQLException {
        Object[][] data = {
            {1, "Classic Backpack", "Bags", 899, "Strong everyday backpack", "🎒"},
            {2, "Wireless Headphones", "Electronics", 1499, "Bluetooth headphones with clear sound", "🎧"},
            {3, "Smart Watch", "Electronics", 1999, "Fitness tracking and notifications", "⌚"},
            {4, "Running Shoes", "Footwear", 1299, "Lightweight running shoes", "👟"},
            {5, "Cotton T-Shirt", "Fashion", 599, "Soft cotton regular-fit T-shirt", "👕"},
            {6, "Travel Bottle", "Accessories", 399, "Reusable leak-proof bottle", "🧴"},
            {7, "Sunglasses", "Accessories", 699, "UV-protection sunglasses", "🕶️"},
            {8, "Laptop Stand", "Electronics", 999, "Adjustable laptop stand", "💻"}
        };
        String sql = "INSERT INTO products (id, name, category, price, description, emoji) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (Object[] d : data) {
                ps.setInt(1, (Integer) d[0]);
                ps.setString(2, (String) d[1]);
                ps.setString(3, (String) d[2]);
                ps.setDouble(4, ((Number) d[3]).doubleValue());
                ps.setString(5, (String) d[4]);
                ps.setString(6, (String) d[5]);
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
