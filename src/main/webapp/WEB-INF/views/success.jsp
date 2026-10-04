<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.onlineshop.util.Html" %>
<% String ctx = request.getContextPath(); %>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Success</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
</head>
<body>
<section class="section narrow">
  <div class="success">
    <div class="check">✓</div>
    <h1>Order Placed Successfully!</h1>
    <p>Order ID: <b>#<%=request.getAttribute("orderId")%></b></p>
    <p>Thank you, <b><%=Html.esc(request.getAttribute("customerName"))%></b>.</p>
    <p>Total: <b>₹<%=String.format("%.0f", (Double) request.getAttribute("total"))%></b></p>
    <p>We will contact you at <%=Html.esc(request.getAttribute("phone"))%>.</p>
    <a class="btn" href="<%=ctx%>/shop">Continue Shopping</a>
    <a class="btn secondary-btn" href="<%=ctx%>/orders">My Orders</a>
  </div>
</section>
</body>
</html>
