package com.onlineshop.servlet;

import com.onlineshop.dao.OrderDAO;
import com.onlineshop.dao.ProductDAO;
import com.onlineshop.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet({"/shop", "/cart", "/checkout", "/orders"})
public class ShopServlet extends HttpServlet {
    private static final Pattern PHONE = Pattern.compile("[0-9]{10}");
    private final ProductDAO products = new ProductDAO();
    private final OrderDAO orders = new OrderDAO();

    @SuppressWarnings("unchecked")
    private List<CartItem> cart(HttpSession s) {
        List<CartItem> c = (List<CartItem>) s.getAttribute("cart");
        if (c == null) {
            c = new ArrayList<>();
            s.setAttribute("cart", c);
        }
        return c;
    }

    private double total(List<CartItem> c) {
        return c.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    private String text(HttpServletRequest r, String name) {
        String v = r.getParameter(name);
        return v == null ? "" : v.trim();
    }

    private int number(HttpServletRequest r, String name) {
        try {
            return Integer.parseInt(text(r, name));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void view(HttpServletRequest r, HttpServletResponse p, String page)
            throws ServletException, IOException {
        r.getRequestDispatcher("/WEB-INF/views/" + page + ".jsp").forward(r, p);
    }

    private void showCheckout(HttpServletRequest r, HttpServletResponse p, List<CartItem> c)
            throws ServletException, IOException {
        r.setAttribute("cart", c);
        r.setAttribute("total", total(c));
        view(r, p, "checkout");
    }

    private void showOrders(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException, SQLException {
        String phone = text(r, "phone");
        if (r.getParameter("phone") != null) {
            if (PHONE.matcher(phone).matches()) {
                r.setAttribute("orders", orders.findByPhone(phone));
            } else {
                r.setAttribute("formError", "Enter a valid 10 digit phone number.");
            }
        }
        view(r, p, "orders");
    }

    private void fail(HttpServletRequest r, HttpServletResponse p, SQLException e)
            throws ServletException, IOException {
        log("Database error", e);
        r.setAttribute("message", "Database error: " + e.getMessage()
                + ". Check the MySQL settings in db.properties (or DB_URL, DB_USER, DB_PASSWORD).");
        p.setStatus(500);
        view(r, p, "error");
    }

    @Override
    protected void doGet(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        List<CartItem> c = cart(r.getSession());
        try {
            switch (r.getServletPath()) {
                case "/cart":
                    r.setAttribute("cart", c);
                    view(r, p, "cart");
                    break;
                case "/checkout":
                    showCheckout(r, p, c);
                    break;
                case "/orders":
                    showOrders(r, p);
                    break;
                default:
                    r.setAttribute("products", products.findAll());
                    view(r, p, "shop");
            }
        } catch (SQLException e) {
            fail(r, p, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest r, HttpServletResponse p)
            throws ServletException, IOException {
        r.setCharacterEncoding("UTF-8");
        String a = text(r, "action");
        List<CartItem> c = cart(r.getSession());
        String base = r.getContextPath();
        try {
            if ("add".equals(a)) {
                Product q = products.findById(number(r, "id"));
                if (q != null) {
                    boolean found = false;
                    for (CartItem i : c) {
                        if (i.getProduct().getId() == q.getId()) {
                            i.setQuantity(i.getQuantity() + 1);
                            found = true;
                        }
                    }
                    if (!found) c.add(new CartItem(q, 1));
                }
                p.sendRedirect(base + "/shop");
            } else if ("remove".equals(a)) {
                int id = number(r, "id");
                c.removeIf(i -> i.getProduct().getId() == id);
                p.sendRedirect(base + "/cart");
            } else if ("clear".equals(a)) {
                c.clear();
                p.sendRedirect(base + "/cart");
            } else if ("placeOrder".equals(a)) {
                if (c.isEmpty()) {
                    p.sendRedirect(base + "/shop");
                    return;
                }
                String name = text(r, "customerName");
                String phone = text(r, "phone");
                String address = text(r, "address");
                if (name.isEmpty() || name.length() > 100 || address.isEmpty()
                        || address.length() > 500 || !PHONE.matcher(phone).matches()) {
                    r.setAttribute("formError",
                            "Please fill all the fields and enter a valid 10 digit phone number.");
                    showCheckout(r, p, c);
                    return;
                }
                double total = total(c);
                int orderId = orders.save(name, phone, address, c, total);
                c.clear();
                r.setAttribute("orderId", orderId);
                r.setAttribute("customerName", name);
                r.setAttribute("phone", phone);
                r.setAttribute("total", total);
                view(r, p, "success");
            } else {
                p.sendRedirect(base + "/shop");
            }
        } catch (SQLException e) {
            fail(r, p, e);
        }
    }
}
