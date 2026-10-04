# ShopEase – Online Shopping Website

CS2307 – Internet Programming Lab mini project.

**Technologies:** HTML, CSS, JavaScript, Java Servlet + JSP, MySQL (JDBC), Maven, Apache Tomcat 10.1, JDK 17.

**Features:** product listing from MySQL, search + category filter (JavaScript), shopping cart (add / remove / clear),
checkout form with validation, orders saved in MySQL, "My Orders" page (search orders by phone number).

## How to run

### Option 1 – Docker (easiest, nothing else to install)
```
docker compose up --build
```
Open **http://localhost:8080**  (MySQL runs automatically in a second container).

### Option 2 – Manually (Tomcat + MySQL installed on your PC)
1. Install **JDK 17**, **Maven**, **MySQL 8** and **Apache Tomcat 10.1**.
2. Start MySQL. Open `src/main/resources/db.properties` and set `db.user` / `db.password` to your MySQL login.
   (The database `shopease`, the tables and the 8 sample products are created automatically.)
3. Build:  `mvn clean package`
4. Copy `target/OnlineShoppingWebsite.war` to `<Tomcat>/webapps/` and start Tomcat.
5. Open **http://localhost:8080/OnlineShoppingWebsite/**

(In NetBeans: open the project, set the server to Tomcat 10.1, then *Run*.)

### Online deployment
Deploy the Dockerfile on any host and give it a MySQL database through the environment variables
`DB_URL`, `DB_USER`, `DB_PASSWORD` (for example a free MySQL from Aiven or TiDB Cloud).

## Project structure
```
src/main/java/com/onlineshop/
    model/    Product, CartItem, Order
    dao/      ProductDAO, OrderDAO         (JDBC queries)
    util/     DBUtil, Html                 (MySQL connection + schema, HTML escaping)
    servlet/  ShopServlet, AppListener     (controller, startup)
src/main/webapp/
    index.jsp                              (redirects to /shop)
    WEB-INF/views/  shop, cart, checkout, success, orders, error  (.jsp)
    css/style.css
database.sql                               (optional manual DB script)
```
