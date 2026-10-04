package com.onlineshop.servlet;

import com.onlineshop.util.DBUtil;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/** Prepares the MySQL database (tables + sample products) when the application starts. */
@WebListener
public class AppListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent e) {
        try {
            DBUtil.initDatabase();
            e.getServletContext().log("ShopEase: MySQL database is ready.");
        } catch (Exception ex) {
            e.getServletContext().log("ShopEase: could not prepare the MySQL database - "
                    + "check db.properties / DB_URL, DB_USER, DB_PASSWORD", ex);
        }
    }
}
