# 🏛️ System Architecture & Design Patterns

## 1. Overview
The University Management System (UMS) is designed as a robust, enterprise-grade web application following the **Model-View-Controller (MVC)** architectural pattern. It ensures a strict **Separation of Concerns (SoC)**, separating user interface rendering (JSP/HTML), request routing and business logic (Servlets/Controllers), and data persistence layers (DAO/MySQL).

---

## 2. Request Lifecycle & MVC Flow
- **View (Client Tier):** HTML, CSS, JavaScript, and dynamic JSP pages handle user interaction and present dashboards for Students, Lecturers, and Administrators.
- **Controller (Web Tier):** Servlets intercept HTTP requests, manage user sessions, validate incoming data, and orchestrate business operations.
- **Model / DAO (Data Tier):** DAO classes encapsulate database access logic, utilizing SQL queries and PreparedStatements to safely interact with the MySQL database.

---

## 3. Database Architecture & Concurrency (`DatabaseConnection.java`)

- **Local Execution & Development:** 
  The repository includes a utility class (`DatabaseConnection.java`) utilizing standard JDBC `DriverManager` connections. This ensures robust, straightforward local execution and rapid testing on developer machines.

- **Enterprise Production Scaling (GlassFish / Connection Pooling):** 
  In the enterprise deployment running on the GlassFish application server, the connection layer transitions to a **JNDI DataSource** configured in `glassfish-resources.xml`. 
  * **Why?** Instead of opening and closing physical sockets for every request (which drains resources under high concurrency), GlassFish manages a **Connection Pool** pre-allocated in memory, significantly improving response time, security, and scalability for thousands of concurrent users.
 
---

### 4. Architectural Review: `StudentDAO.java` & `Student.java`

- **Robust Transaction Management:** In `registerStudent` and `updateProfileWithValidation`, you correctly manage transaction boundaries by disabling auto-commit (`conn.setAutoCommit(false)`), executing multi-table operations across students and users, handling rollbacks explicitly on failure, and restoring auto-commit in a finally block. This guarantees ACID compliance and prevents orphaned records.
- **Security & Prevention of SQL Injection:** All dynamic queries use `PreparedStatement` with parameterized placeholders (`?`), neutralizing SQL injection vectors entirely.
- **Resource Leak Prevention:** Consistent usage of Java 7+ `try-with-resources` blocks for `Connection`, `PreparedStatement`, and `ResultSet` ensures that database cursors and connections are closed reliably, avoiding connection leaks in a long-running application server like GlassFish.
- **Comprehensive Domain Modeling:** `Student.java` serves as a complete rich domain entity that cleanly maps academic demographics, guardian records, qualifications, and financial statuses.
