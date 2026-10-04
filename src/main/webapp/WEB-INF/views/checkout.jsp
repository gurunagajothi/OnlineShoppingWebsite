<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,com.onlineshop.model.CartItem,com.onlineshop.util.Html" %>
<%
  List<CartItem> cart = (List<CartItem>) request.getAttribute("cart");
  Double total = (Double) request.getAttribute("total");
  String formError = (String) request.getAttribute("formError");
  String ctx = request.getContextPath();
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Checkout</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
</head>
<body>
<header>
  <div class="nav">
    <a class="brand" href="<%=ctx%>/shop">ShopEase</a>
    <nav>
      <a href="<%=ctx%>/cart">🛒 Cart</a>
    </nav>
  </div>
</header>

<section class="section narrow">
  <div class="formbox">
    <h1>Checkout</h1>
    <% if (cart.isEmpty()) { %>
      <p>Your cart is empty.</p>
      <a class="btn" href="<%=ctx%>/shop">Shop Now</a>
    <% } else { %>
      <% if (formError != null) { %><p class="error"><%=Html.esc(formError)%></p><% } %>
      <form method="post" action="<%=ctx%>/checkout" onsubmit="return valid()">
        <input type="hidden" name="action" value="placeOrder">
        <label>Full Name</label>
        <input name="customerName" maxlength="100" required
               value="<%=Html.esc(request.getParameter("customerName"))%>">
        <label>Phone Number</label>
        <input id="phone" name="phone" pattern="[0-9]{10}" maxlength="10" required
               value="<%=Html.esc(request.getParameter("phone"))%>">
        <label>Delivery Address</label>
        <textarea name="address" maxlength="500" required><%=Html.esc(request.getParameter("address"))%></textarea>
        <h2>Order Total: ₹<%=String.format("%.0f", total)%></h2>
        <button class="btn full">Place Order</button>
      </form>
    <% } %>
  </div>
</section>

<script>
function valid() {
  if (!/^[0-9]{10}$/.test(document.getElementById('phone').value)) {
    alert('Enter valid 10 digit phone number');
    return false;
  }
  return true;
}
</script>
</body>
</html>
