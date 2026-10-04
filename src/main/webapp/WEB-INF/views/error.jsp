<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.onlineshop.util.Html" %>
<% String ctx = request.getContextPath(); %>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Error</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
</head>
<body>
<section class="section narrow">
  <div class="empty">
    <h1>⚠️ Something went wrong</h1>
    <p class="error"><%=Html.esc(request.getAttribute("message"))%></p>
    <a class="btn" href="<%=ctx%>/shop">Back to Home</a>
  </div>
</section>
</body>
</html>
