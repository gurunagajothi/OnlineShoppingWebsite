<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,com.onlineshop.model.CartItem,com.onlineshop.util.Html" %>
<%
  List<CartItem> cart = (List<CartItem>) request.getAttribute("cart");
  String ctx = request.getContextPath();
  double total = 0;
  for (CartItem i : cart) total += i.getSubtotal();
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Cart</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
</head>
<body>
<header>
  <div class="nav">
    <a class="brand" href="<%=ctx%>/shop">ShopEase</a>
    <nav>
      <a href="<%=ctx%>/shop">Home</a>
      <a href="<%=ctx%>/orders">My Orders</a>
    </nav>
  </div>
</header>

<section class="section narrow">
  <h1>🛒 Shopping Cart</h1>
  <% if (cart.isEmpty()) { %>
    <div class="empty">
      <h2>Your cart is empty</h2>
      <a class="btn" href="<%=ctx%>/shop">Continue Shopping</a>
    </div>
  <% } else { for (CartItem i : cart) { %>
    <div class="cart">
      <span><%=Html.esc(i.getProduct().getEmoji())%></span>
      <div class="grow">
        <h3><%=Html.esc(i.getProduct().getName())%></h3>
        <p>Quantity: <%=i.getQuantity()%></p>
      </div>
      <b>₹<%=String.format("%.0f", i.getSubtotal())%></b>
      <form method="post" action="<%=ctx%>/cart">
        <input type="hidden" name="action" value="remove">
        <input type="hidden" name="id" value="<%=i.getProduct().getId()%>">
        <button class="danger">Remove</button>
      </form>
    </div>
  <% } %>
    <div class="summary">
      <h2>Total: ₹<%=String.format("%.0f", total)%></h2>
      <form method="post" action="<%=ctx%>/cart">
        <input type="hidden" name="action" value="clear">
        <button class="secondary">Clear</button>
      </form>
      <a class="btn" href="<%=ctx%>/checkout">Checkout</a>
    </div>
  <% } %>
</section>
</body>
</html>
