package com.onlineshop.model;

import java.util.List;

public class Order {
    private final int id;
    private final String customerName, phone, address, createdAt;
    private final double total;
    private final List<String> items;

    public Order(int id, String customerName, String phone, String address,
                 double total, String createdAt, List<String> items) {
        this.id = id;
        this.customerName = customerName;
        this.phone = phone;
        this.address = address;
        this.total = total;
        this.createdAt = createdAt;
        this.items = items;
    }

    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public double getTotal() { return total; }
    public String getCreatedAt() { return createdAt; }
    public List<String> getItems() { return items; }
}
