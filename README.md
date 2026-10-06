# Product Management REST API (Spring Boot - SOA)

This project implements a product management catalog using a layered architecture (Controller, Service, Repository, DTO/Mapper), persistence with **Spring Data JPA**, and cross-cutting security features via **Spring AOP** (Aspect-Oriented Programming).

---

## 🛠️ Tech Stack

- **Java**: 17
- **Framework**: [Spring Boot 3.5.7](https://spring.io/projects/spring-boot)
  - Spring Web (RESTful API)
  - Spring Data JPA (Data persistence)
  - Spring AOP (Aspect-Oriented Programming)
  - Spring Boot DevTools
- **Supported Databases**:
  - [PostgreSQL](https://www.postgresql.org/) (configured by default)
  - [MySQL](https://www.mysql.com/) (connector included)
- **Tools & Libraries**:
  - [MapStruct 1.6.0](https://mapstruct.org/): Automatic DTO <-> Entity mapping
  - [Lombok 1.18.34](https://projectlombok.org/): Boilerplate code reduction (constructors, dependency injection)
  - [Maven](https://maven.apache.org/): Dependency management and build tool

---

## 📂 Project Structure

```text
src/main/java/org/tp2/
├── Tp2Application.java              # Main Spring Boot entry point
├── controller/
│   └── ProductController.java       # REST controller exposing /api/products endpoints
├── dto/
│   └── ProductDTO.java              # Data Transfer Object (DTO)
├── mapper/
│   └── ProductMapper.java           # MapStruct interface for Entity <-> DTO conversions
├── model/
│   └── Product.java                 # JPA Entity mapped to the 'products' table
├── repository/
│   └── ProductRepository.java       # Spring Data JPA repository (CRUD & pagination)
├── security/
│   ├── CheckAuthorization.java     # Custom annotation for access authorization
│   └── AuthorizationAspect.java     # AOP aspect intercepting requests (IP / Country filtering)
└── service/
    ├── ProductService.java          # Service interface
    └── impl/
        └── ProductServiceImpl.java  # Business logic implementation
src/main/resources/
└── application.properties           # Database and JPA configuration
```

---

## 🚀 Key Features

1. **Layered Architecture**:
   - Strict decoupling between domain entities (`Product`) and client-facing models (`ProductDTO`) using **MapStruct**.
   - Clean constructor-based dependency injection powered by **Lombok** (`@RequiredArgsConstructor`).
2. **Full CRUD & Pagination**:
   - Create, read, update, and delete products.
   - Paginated product retrieval powered by `PageRequest`.
3. **Aspect-Oriented Security (AOP)**:
   - Custom `@CheckAuthorization` annotation allowing access restriction based on blocked IPs (`blockedIPs`) or allowed countries (`countries`).
   - The `AuthorizationAspect` intercepts method execution using `@Before` and inspects client IP information via `HttpServletRequest.getRemoteAddr()`.

---

## 📡 REST API Documentation

Base URL: `http://localhost:8080/api/products`

| HTTP Method | Endpoint | Description | Parameters / Payload |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/products/add` | Create a new product | JSON Request Body (`ProductDTO`) |
| `GET` | `/api/products/{id}` | Get product by ID | `id` (Path variable) |
| `GET` | `/api/products` | Retrieve products with pagination | Query params: `page` (default: 0), `size` (default: 10) |
| `PUT` | `/api/products/{id}` | Update an existing product | `id` (Path variable) + JSON Body (`ProductDTO`) |
| `DELETE`| `/api/products/{id}` | Delete a product | `id` (Path variable) |

### Request & Response Examples

#### 1. Create a Product
- **Request**: `POST /api/products/add`
```json
{
  "name": "Mechanical Keyboard",
  "description": "RGB mechanical keyboard with red switches",
  "price": 89.99
}
```
- **Response (200 OK)**:
```json
{
  "id": 1,
  "name": "Mechanical Keyboard",
  "description": "RGB mechanical keyboard with red switches",
  "price": 89.99
}
```

#### 2. Get Product by ID
- **Request**: `GET /api/products/1`
- **Response (200 OK)**:
```json
{
  "id": 1,
  "name": "Mechanical Keyboard",
  "description": "RGB mechanical keyboard with red switches",
  "price": 89.99
}
```

#### 3. List Products (Paginated)
- **Request**: `GET /api/products?page=0&size=5`
- **Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "Mechanical Keyboard",
    "description": "RGB mechanical keyboard with red switches",
    "price": 89.99
  }
]
```

#### 4. Update a Product
- **Request**: `PUT /api/products/1`
```json
{
  "name": "Wireless Mechanical Keyboard",
  "description": "Wireless RGB mechanical keyboard",
  "price": 109.99
}
```
- **Response (200 OK)**:
```json
{
  "id": 1,
  "name": "Wireless Mechanical Keyboard",
  "description": "Wireless RGB mechanical keyboard",
  "price": 109.99
}
```

#### 5. Delete a Product
- **Request**: `DELETE /api/products/1`
- **Response**: `204 No Content`

---

## ⚙️ Prerequisites & Configuration

### Prerequisites
- **JDK 17** or higher installed and properly configured (`JAVA_HOME`).
- **PostgreSQL** running locally (or MySQL adapted).
- **Maven** (or use the included wrapper `./mvnw` / `mvnw.cmd`).

### Database Configuration
Review or update your credentials in `src/main/resources/application.properties`:

```properties
spring.application.name=tp2

# PostgreSQL Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/test
spring.datasource.username=postgres
spring.datasource.password=your_password

# JPA / Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

> **Note:** Ensure that the database `test` exists before starting the application:
> ```sql
> CREATE DATABASE test;
> ```

---

## 🔨 Build & Run

### 1. Build the Project
On Windows (PowerShell / CMD):
```cmd
mvnw.cmd clean package
```
On Linux / macOS:
```bash
./mvnw clean package
```

### 2. Run the Application
On Windows:
```cmd
mvnw.cmd spring-boot:run
```
On Linux / macOS:
```bash
./mvnw spring-boot:run
```

The application will be accessible by default at `http://localhost:8080`.

---

## 🔒 AOP Security Module (`@CheckAuthorization`)

The application provides a declarative security annotation to safeguard controller methods:

```java
@CheckAuthorization(countries = {"FR", "US"}, blockedIPs = {"192.168.1.100"})
public ResponseEntity<ProductDTO> someSecuredMethod() { ... }
```

- **Blocked IPs**: Rejects incoming requests with a `SecurityException` if the client IP is on the blocked list.
- **Country Filter**: Verifies if the client country (resolved from the IP) belongs to the authorized countries list.
