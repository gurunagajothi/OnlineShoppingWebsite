package com.onlineshop.dao;

import com.onlineshop.model.CartItem;
import com.onlineshop.model.Order;
import com.onlineshop.util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    /** Saves an order and its items in one transaction and returns the new order id. */
    public int save(String name, String phone, String address,
                    List<CartItem> items, double total) throws SQLException {
        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false);
            try {
                int orderId;
                String sql = "INSERT INTO orders (customer_name, phone, address, total) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, name);
                    ps.setString(2, phone);
                    ps.setString(3, address);
                    ps.setDouble(4, total);
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        keys.next();
                        orderId = keys.getInt(1);
                    }
                }
                String itemSql = "INSERT INTO order_items (order_id, product_id, product_name, quantity, price) "
                               + "VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                    for (CartItem i : items) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, i.getProduct().getId());
                        ps.setString(3, i.getProduct().getName());
                        ps.setInt(4, i.getQuantity());
                        ps.setDouble(5, i.getProduct().getPrice());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }
                con.commit();
                return orderId;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    /** Returns the latest orders placed with the given phone number. */
    public List<Order> findByPhone(String phone) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT id, customer_name, phone, address, total, created_at "
                   + "FROM orders WHERE phone = ? ORDER BY id DESC LIMIT 20";
        String itemSql = "SELECT product_name, quantity FROM order_items WHERE order_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             PreparedStatement pi = con.prepareStatement(itemSql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    List<String> lines = new ArrayList<>();
                    pi.setInt(1, id);
                    try (ResultSet ri = pi.executeQuery()) {
                        while (ri.next()) {
                            lines.add(ri.getInt("quantity") + " × " + ri.getString("product_name"));
                        }
                    }
                    list.add(new Order(id, rs.getString("customer_name"), rs.getString("phone"),
                            rs.getString("address"), rs.getDouble("total"),
                            String.valueOf(rs.getTimestamp("created_at")), lines));
                }
            }
        }
        return list;
    }
}
