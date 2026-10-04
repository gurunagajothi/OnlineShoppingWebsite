<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,com.onlineshop.model.Product,com.onlineshop.util.Html" %>
<%
  List<Product> products = (List<Product>) request.getAttribute("products");
  String ctx = request.getContextPath();
%>
<!doctype html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>ShopEase</title>
  <link rel="stylesheet" href="<%=ctx%>/css/style.css">
</head>
<body>
<header>
  <div class="nav">
    <a class="brand" href="<%=ctx%>/shop">ShopEase</a>
    <nav>
      <a href="<%=ctx%>/shop">Home</a>
      <a href="#products">Products</a>
      <a href="<%=ctx%>/cart">🛒 Cart</a>
      <a href="<%=ctx%>/orders">My Orders</a>
    </nav>
  </div>
</header>

<section class="hero">
  <div>
    <small>ONLINE SHOPPING</small>
    <h1>Everything you need,<br>in one place.</h1>
    <p>Quality products at simple and affordable prices.</p>
    <a class="btn" href="#products">Shop Now</a>
  </div>
  <div class="heroicon">🛍️</div>
</section>

<section id="products" class="section">
  <div class="head">
    <div><small>OUR COLLECTION</small><h2>Popular Products</h2></div>
    <input id="search" placeholder="Search products..." onkeyup="filterProducts()">
  </div>
  <div class="cats">
    <button onclick="filterCat('All')">All</button>
    <button onclick="filterCat('Electronics')">Electronics</button>
    <button onclick="filterCat('Fashion')">Fashion</button>
    <button onclick="filterCat('Footwear')">Footwear</button>
    <button onclick="filterCat('Accessories')">Accessories</button>
    <button onclick="filterCat('Bags')">Bags</button>
  </div>
  <div class="grid">
  <% for (Product x : products) { %>
    <div class="card" data-name="<%=Html.esc(x.getName().toLowerCase())%>" data-cat="<%=Html.esc(x.getCategory())%>">
      <div class="pic"><%=Html.esc(x.getEmoji())%></div>
      <div class="body">
        <small><%=Html.esc(x.getCategory())%></small>
        <h3><%=Html.esc(x.getName())%></h3>
        <p><%=Html.esc(x.getDescription())%></p>
        <div class="row">
          <b>₹<%=String.format("%.0f", x.getPrice())%></b>
          <form method="post" action="<%=ctx%>/shop">
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="id" value="<%=x.getId()%>">
            <button class="add">Add to Cart</button>
          </form>
        </div>
      </div>
    </div>
  <% } %>
  </div>
</section>

<footer>© 2026 ShopEase | Internet Programming Laboratory</footer>

<script>
let cat = 'All';
function filterProducts() {
  let s = document.getElementById('search').value.toLowerCase();
  document.querySelectorAll('.card').forEach(c =>
    c.style.display = c.dataset.name.includes(s) &&
                      (cat === 'All' || c.dataset.cat === cat) ? '' : 'none');
}
function filterCat(x) { cat = x; filterProducts(); }
</script>
</body>
</html>
