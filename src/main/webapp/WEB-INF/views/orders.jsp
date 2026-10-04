<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,com.onlineshop.model.Order,com.onlineshop.util.Html" %>
<%
  List<Order> orders = (List<Order>) request.getAttribute("orders");
  String formError = (String) request.getAttribute("formError");
  String ctx = request.getContextPath();
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>My Orders</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
</head>
<body>
<header>
  <div class="nav">
    <a class="brand" href="<%=ctx%>/shop">ShopEase</a>
    <nav>
      <a href="<%=ctx%>/shop">Home</a>
      <a href="<%=ctx%>/cart">🛒 Cart</a>
    </nav>
  </div>
</header>

<section class="section narrow">
  <h1>📦 My Orders</h1>
  <form class="lookup" method="get" action="<%=ctx%>/orders">
    <input id="phone" name="phone" placeholder="Enter your 10 digit phone number"
           pattern="[0-9]{10}" maxlength="10" required
           value="<%=Html.esc(request.getParameter("phone"))%>">
    <button class="btn">Find Orders</button>
  </form>
  <% if (formError != null) { %><p class="error"><%=Html.esc(formError)%></p><% } %>

  <% if (orders != null && orders.isEmpty()) { %>
    <div class="empty"><h2>No orders found for this phone number</h2></div>
  <% } else if (orders != null) { for (Order o : orders) { %>
    <div class="order">
      <div class="orderhead">
        <b>Order #<%=o.getId()%></b>
        <span><%=Html.esc(o.getCreatedAt())%></span>
        <b>₹<%=String.format("%.0f", o.getTotal())%></b>
      </div>
      <p><b><%=Html.esc(o.getCustomerName())%></b> – <%=Html.esc(o.getAddress())%></p>
      <ul>
        <% for (String line : o.getItems()) { %><li><%=Html.esc(line)%></li><% } %>
      </ul>
    </div>
  <% } } %>
</section>
</body>
</html>
