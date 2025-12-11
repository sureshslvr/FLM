# Microservices – Notes (Food Delivery App Example)

---

## 1. Microservices Basics

### 1.1 What are Microservices?

* **Microservices = Architecture style**
* Application is broken into **small, independent services**
* Each service:

    * Has its own **responsibility**
    * Can have its own **database**
    * Can be **developed, deployed, and scaled independently**

---

### 1.2 Monolithic vs Microservices

| Aspect           | Monolithic                                        | Microservices                                          |
| ---------------- | ------------------------------------------------- | ------------------------------------------------------ |
| Structure        | Single codebase, single deployable unit (WAR/JAR) | Many small services, each deployable independently     |
| Scalability      | Scale entire app                                  | Scale only the heavy/required services                 |
| Technology Stack | Usually one tech stack                            | Each service can use its own tech stack                |
| Deployment       | One deployment (tightly coupled)                  | Multiple deployments (loosely coupled)                 |
| Fault Impact     | One module failure can affect whole app           | Failure in one service doesn’t crash the entire system |
| Codebase         | Grows big, hard to manage                         | Smaller, focused codebases                             |
| Data Storage     | Single database                                   | Each service may have its own database                 |

---

## 2. Microservices – Pros & Cons

### 2.1 Advantages of Microservices

1. **Scalability**

    * Can scale only the services that need more resources (e.g., Order Service on weekends).

2. **Independence**

    * Each service can be developed, deployed, and maintained independently.
    * Teams can work in parallel.

3. **Fault Tolerance**

    * If one service goes down, other services can still work (with proper fallbacks).

4. **Faster Development**

    * Smaller codebase → faster builds and deployments.
    * Multiple teams can deliver features in parallel.

5. **Ease of Understanding Code**

    * Each service does one focused thing → easier to read and maintain.

6. **Technology Feasibility**

    * Different services can use different technologies (Java, Node, different DBs).

7. **Loose Coupling**

    * Services interact via APIs (REST, messaging), not direct method calls.

---

### 2.2 Disadvantages of Microservices

1. **Testing / Logging is Difficult**

    * End-to-end testing is harder because multiple services are involved.
    * Need centralized logging (ELK, etc.).

2. **Maintenance is High**

    * Many services → more repositories, more deployments, more monitoring.

3. **Cost**

    * More servers/containers, infra tools → higher cost.

4. **Management is Difficult**

    * Need service discovery, API gateway, config management, monitoring, etc.

5. **Data Inconsistency**

    * Each service has its own DB.
    * Distributed transactions are hard → eventual consistency patterns needed.

6. **Deployment Complexity**

    * Need CI/CD for multiple services.
    * Orchestration (Docker, Kubernetes) often required.

---

## 3. Food Delivery Application – Domain Design

Think of this as a base schema / entity model for microservices.

### 3.1 High-Level Microservices (Possible Split)

* **Restaurant Service**

    * Manages Restaurants and Items.
* **User Service**

    * Manages Users and Addresses.
* **Order Service**

    * Manages Orders and Order Items.
* **Delivery Service**

    * Manages Delivery Partners and Delivery Assignments.
* (Optional) **Rating/Review Service**

    * Manages ratings for restaurants/items/delivery partners.

---

## 4. Entities & Fields

### 4.1 Restaurant Service

#### 4.1.1 Restaurant

* `id`
* `name`
* `address` (can be a separate entity or embedded)
* `phoneNumber`
* `items` (List of menu items)
* `rating`

**JPA relationships:**

* `Restaurant` **1..* Items**

    * `Restaurant` → `@OneToMany(mappedBy = "restaurant")`
    * `Item` → `@ManyToOne @JoinColumn(name = "restaurant_id")`

---

#### 4.1.2 Address (for Restaurant)

* `id`
* `landmark`
* `city`
* `pincode`
* `state`
* `country`

Options in JPA:

* As separate Entity: `Restaurant` → `@OneToOne`
* Or as embedded: `@Embeddable` + `@Embedded` in `Restaurant`

---

#### 4.1.3 Item

* `id`
* `name`
* `restaurantId` (or `Restaurant restaurant`)
* `price`
* `isAvailable`
* `category` (e.g., STARTER, MAIN_COURSE)
* `type` (e.g., VEG / NON_VEG)
* `rating`

**Relationships:**

* Many items belong to one restaurant:

    * `@ManyToOne`
    * `@JoinColumn(name = "restaurant_id")`

---

### 4.2 Order Service

#### 4.2.1 Order

* `id`
* `userId` (or `User user`)
* `restaurantId` (or `Restaurant restaurant`)
* `status` (CREATED, CONFIRMED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED)
* `orderItems` (List of `OrderItem`)
* `orderPrice`

**Relationships:**

* One user can have many orders → `User` **1..*** `Order`

    * In `Order`: `@ManyToOne User user`
* One restaurant can have many orders → `Restaurant` **1..*** `Order`

    * In `Order`: `@ManyToOne Restaurant restaurant`
* One order can have many order items:

    * `Order` → `@OneToMany(mappedBy = "order")`
    * `OrderItem` → `@ManyToOne @JoinColumn(name = "order_id")`

---

#### 4.2.2 OrderItem

* `id`
* `itemId` (or `Item item`)
* `quantity`

**Relationships:**

* `OrderItem` → `@ManyToOne Order order`
* `OrderItem` → `@ManyToOne Item item`

---

### 4.3 User Service

#### 4.3.1 User

* `id`
* `name`
* `phoneNumber`
* `addresses` (List of Address)
* `orders` (List of Order – or fetched via Order Service)
* `email`
* `password` (should be hashed)

**Relationships:**

* One user can have multiple addresses:

    * `User` → `@OneToMany(mappedBy = "user")`
    * `Address` → `@ManyToOne @JoinColumn(name = "user_id")`
* Orders often kept in Order Service, so here you may use:

    * Just `userId` in Order DB; no direct `List<Order>` in User entity
    * Or use DTOs when calling Order Service.

---

#### 4.3.2 Address (for User)

* `id`
* `landmark`
* `city`
* `pincode`
* `state`
* `country`

**Relationship:**

* Many addresses belong to one User:

    * `@ManyToOne User user`

---

### 4.4 Delivery Service

#### 4.4.1 DeliveryPerson

* `id`
* `name`
* `aadhar`
* `phone`
* `email`
* `isAvailable`
* `deliveryAssignments` (List of `DeliveryAssignment`)
* `rating`

**Relationship:**

* One delivery person can have many assignments:

    * `DeliveryPerson` → `@OneToMany(mappedBy = "deliveryPerson")`
    * `DeliveryAssignment` → `@ManyToOne @JoinColumn(name = "delivery_person_id")`

---

#### 4.4.2 DeliveryAssignment

* `assignmentId`
* `orderId`
* `deliveryPersonId` (or `DeliveryPerson deliveryPerson`)
* `status` (ASSIGNED, PICKED_UP, DELIVERED, CANCELLED)
* `assignedTime`

**Relationships:**

* Many assignments can be related to one order (if re-assigned) or one-to-one:

    * Simplest: one order → one assignment:

        * `DeliveryAssignment` has `orderId` (value, not entity)
* Many assignments can be done by one delivery person:

    * `@ManyToOne DeliveryPerson deliveryPerson`

---

## 5. JPA Relationships Summary (OneToMany / ManyToOne)

* **Restaurant – Item**

    * `Restaurant` **1..*** `Item`
* **User – Address**

    * `User` **1..*** `Address`
* **User – Order**

    * `User` **1..*** `Order`
* **Order – OrderItem**

    * `Order` **1..*** `OrderItem`
* **DeliveryPerson – DeliveryAssignment**

    * `DeliveryPerson` **1..*** `DeliveryAssignment`

Always think:

* **Parent that has List<Child>** → `@OneToMany`
* **Child that has single Parent** → `@ManyToOne`

---

## 6. Spring Boot Dependencies (Food Delivery Microservices)

For each microservice (Restaurant, User, Order, Delivery), common dependencies:

1. `spring-boot-starter-web`

    * For building REST APIs (`@RestController`, `@RequestMapping`).

2. `spring-boot-starter-data-jpa`

    * For ORM mapping using JPA & Hibernate.
    * `@Entity`, `@Repository`, `@OneToMany`, etc.

3. Database Driver (e.g., MySQL):

    * `mysql-connector-j`
    * Or any other SQL driver (PostgreSQL, etc.).

4. `lombok`

    * To reduce boilerplate: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.

5. `spring-boot-devtools`

    * For auto-restart during development.

6. `spring-boot-starter-security`

    * For security (authentication/authorization).
    * Can secure APIs with JWT, etc.

---

## 7. `application.properties` / `application.yml` Basics

You must configure:

1. **Server Port** (optional; default is 8080)

```properties
server.port=8081
```

2. **Database Configuration**

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/food_delivery
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

> Each microservice can have **its own DB** (ex: `restaurant_db`, `order_db`, `user_db`, etc.).

---

## 8. Builder Pattern (Using Lombok `@Builder`)

### 8.1 Why Builder Pattern?

* Object creation is **readable**, especially when many fields exist.
* Avoids telescoping constructors.
* Very useful for:

    * DTOs (Request/Response objects)
    * Entities
    * Test data creation

### 8.2 Example with Lombok

```java
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private Boolean isAvailable;
    private String category;
    private String type;
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
```

Usage:

```java
Item item = Item.builder()
        .name("Paneer Butter Masala")
        .price(250.0)
        .isAvailable(true)
        .category("MAIN_COURSE")
        .type("VEG")
        .rating(4.5)
        .build();
```

---

## 9. Step-by-Step Flow to Design This Microservices Project

1. **Decide Architecture**

    * Choose microservices instead of monolith.
2. **Identify Services**

    * Restaurant, User, Order, Delivery (and optionally Rating).
3. **Design Entities & Relationships**

    * Restaurants, Items, Users, Addresses, Orders, OrderItems, DeliveryPersons, DeliveryAssignments.
    * Decide `@OneToMany` / `@ManyToOne`.
4. **Create Spring Boot Projects**

    * One project per microservice (or modules in one repo).
5. **Add Dependencies**

    * Web, JPA, DB driver, Lombok, Devtools, Security.
6. **Configure `application.properties`**

    * Server port, DB URL, username, password, JPA settings.
7. **Create Entities & Repositories**

    * Use JPA annotations and Lombok (`@Builder` etc.).
8. **Create Service & Controller Layers**

    * Business logic and REST endpoints.
9. **(Later) Add Communication**

    * REST calls between services, or message broker (RabbitMQ/Kafka).
10. **(Later) Add Security & Monitoring**

* Spring Security (JWT), logging, centralized configs, etc.

---

# Inter-Service Communication – Notes

Microservices mostly need to **talk to each other** → Inter-Service Communication

Two Types of Communication:

* **Synchronous** → request waits for response
* **Asynchronous** → request does not wait

---

## 1️⃣ Synchronous Communication

Order Service → Restaurant Service
Order **must wait** for Restaurant response before continuing.

### Techniques:

| Method           | Description                                               |
| ---------------- | --------------------------------------------------------- |
| **RestTemplate** | Traditional approach (sync). Manual load balancing.       |
| **FeignClient**  | Advanced client. Auto load balancing. Developer friendly. |

---

### 1.1 RestTemplate

Used to call other service **synchronously** using APIs.

> We pass URL and **return type** + **body (for POST/PUT)**

#### Common Methods

| Method                                               | Use                                                      |
| ---------------------------------------------------- | -------------------------------------------------------- |
| `getForObject(url, responseType)`                    | GET → returns body directly                              |
| `getForEntity(url, responseType)`                    | GET → returns full HTTP response (status, headers, body) |
| `postForObject(url, requestBody, responseType)`      | POST → returns created object                            |
| `postForEntity(url, requestBody, responseType)`      | POST → returns full response                             |
| `exchange(url, method, requestEntity, responseType)` | For PUT/DELETE or custom headers                         |

---

## 2️⃣ Asynchronous Communication

Order → Notification Service
Order **does not wait** for notification service processing.

Examples:

* **RabbitMQ**
* **Kafka**
* WebClient (async HTTP client)

Used for:

* Background tasks
* Event-driven architecture

---

## 3️⃣ Discovery Service – Eureka (Spring Cloud)

Used to detect available microservices dynamically.
All services register here → **Central Monitoring Dashboard**

---

### 3.1 Eureka Server Setup

Dependencies:

* Spring Web
* Eureka Server

Main class:

```java
@EnableEurekaServer
```

Properties:

```properties
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
server.port=8761
```

Access dashboard:

```
http://localhost:8761/eureka/
```

---

### 3.2 Eureka Client Setup

Dependencies:

* Eureka Client

Properties:

```properties
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
```

Eureka checks heartbeat every **60/90 sec**
If no response → service marked **DOWN**

#### For dynamic instance naming:

```properties
eureka.instance.instance-id=${spring.application.name}:${spring.instance.instance-id:${random.value}}
```

---

## 4️⃣ Spring Cloud Load Balancing

Used when multiple instances of same service exist:

Order → Restaurant Service (ports: 8002, 8003, 8004…)

Two Types:

| Type               | Managed By          | Cost     |
| ------------------ | ------------------- | -------- |
| **Client-side LB** | Application         | Cheaper  |
| **Server-side LB** | K8s, AWS ALB, Azure | Costlier |

Spring uses **Round Robin** by default
Others:

* Random
* Availability Zone

---

### Enable Load Balanced RestTemplate

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

➡ Now URL doesn’t need hostname/port
Example:

```
http://RestaurantManagement/orders
```

Not like:

```
http://localhost:8002/orders
```

---

### RestTemplate Drawbacks

* Manual load balancing without Eureka
* Complex with headers/exchange
* Deprecated direction (future Feign/WebClient focus)

---

## 5️⃣ Open FeignClient

> Upgraded version of RestTemplate
> Handles Load Balancing automatically
> Easy → just call a Java method

### Setup

Add dependency: **OpenFeign**

Main class:

```java
@EnableFeignClients
```

### Create Feign Client interface

```java
@FeignClient(name = "RestaurantManagement")
public interface RestaurantClient {

    @GetMapping("/restaurant/{id}")
    RestaurantResponse getRestaurant(@PathVariable Long id);
}
```

### Usage in service:

```java
@Autowired
private RestaurantClient restaurantClient;

RestaurantResponse res = restaurantClient.getRestaurant(10L);
```

✔ No URL writing
✔ No RestTemplate
✔ Automatic Load Balancing
✔ Cleaner Code

---

## Summary Table

| Feature               | RestTemplate           | FeignClient   | WebClient   |
| --------------------- | ---------------------- | ------------- | ----------- |
| Sync Communication    | ✔                      | ✔             | ✔           |
| Async                 | ❌                      | ❌             | ✔           |
| Auto Load Balancing   | ❌ (manual)             | ✔             | ✔           |
| Easy to use           | Medium                 | Very Easy     | Medium      |
| Future Recommendation | ❌ Deprecated direction | ⭐ Recommended | ⭐ For async |

---

# Spring Boot Profiles – Notes

## Why Profiles?

Used to **switch environments** without changing code.

Common environments:

* **dev** → local development
* **qa** → testing environment
* **prod** → production environment

Each environment can have:

* Different **server port**
* Different **database configuration**
* Different **logging level**
* Different **3rd party service endpoints**

---

## How Profiles Work in Spring Boot?

Configuration files based on environment:

| File                   | Purpose                   |
| ---------------------- | ------------------------- |
| `application.yml`      | Main / default file       |
| `application-dev.yml`  | Dev environment settings  |
| `application-qa.yml`   | QA environment settings   |
| `application-prod.yml` | Prod environment settings |

---

## Example

### `application.yml`

```yaml
spring:
  profiles:
    active: qa  # choose which environment to activate
```

Set **qa** profile active → Application uses `application-qa.yml`

---

### `application-dev.yml`

```yaml
server:
  port: 8000

spring:
  datasource:
    url: jdbc:h2:mem:testdb
```

Used:
✔ Local
✔ H2 DB → No installation needed

---

### `application-qa.yml`

```yaml
server:
  port: 8500

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/food_delivery
```

Used:
✔ Testing environment
✔ PostgreSQL database

---

### `application-prod.yml`

```yaml
server:
  port: 9000

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/food_delivery
```

Used:
✔ Production deployment
✔ MySQL database (stable)

---

## How to Run with Different Profiles?

### Option 1 → Change in `application.yml`

```yaml
spring.profiles.active=dev
```

### Option 2 → Command Line

```
java -jar app.jar --spring.profiles.active=prod
```

### Option 3 → In IntelliJ/STS Run Configuration

Add VM Option:

```
-Dspring.profiles.active=qa
```

---

## Summary Table

| Profile | Port | Database   |
| ------- | ---- | ---------- |
| dev     | 8000 | H2         |
| qa      | 8500 | PostgreSQL |
| prod    | 9000 | MySQL      |

---

## Benefits of Profiles

* Makes deployment flexible
* No code change required for different environments
* Secure & organized configuration
* Easy database switching

---

# Spring Cloud Config – Notes

Config Server is used for **Centralized Configuration Management**
👉 One location to manage configuration for all microservices.

---

## Why Config Server?

Without config server:

* Each service has its own `.properties/.yml`
* Hard to maintain
* For changes → restart every service
* Risk of inconsistency between environments

With config server:

* **All configs stored in one place**
* Dynamic updates possible
* No redeployment required for config changes

---

## Components

| Component         | Purpose                                                        |
| ----------------- | -------------------------------------------------------------- |
| **Config Server** | Reads configuration from external source and serves to clients |
| **Config Client** | Microservices that fetch config from Config Server             |

---

### Configuration Storage Options

Configs can be stored in:

* **GitHub** (most common)
* File System (local)
* AWS / Cloud Repos
* `.yml` / `.properties` files

Naming convention:

```
applicationName-environment.yml
```

Example:

```
Restaurant-dev.yml
Order-qa.yml
User-prod.yml
```

---

## 1️⃣ Config Server Setup

### Dependencies

* Spring Web
* Spring Cloud Config Server
* Actuator

### Enable in Main Class

```java
@EnableConfigServer
@SpringBootApplication
public class ConfigServerApplication { }
```

### `application.properties` (Config Server)

```properties
server.port=8888

spring.cloud.config.server.git.uri=https://github.com/your-repo/config-files
spring.cloud.config.server.git.clone-on-start=true  # Eager loading
spring.cloud.config.server.git.default-label=main   # Git branch
```

---

### Test Endpoint

Format:

```
http://localhost:8888/{applicationName}/{profile}
```

Example:

```
http://localhost:8888/Restaurant/dev
```

If success → returns JSON config

---

## 2️⃣ Config Client Setup

Used in each microservice like Restaurant, Order…

### Dependencies

* Config Client
* Actuator

### Add to `application.properties` or `application.yml`:

```properties
spring.config.import=optional:configserver:http://localhost:8888
```

✔ Allows fetching configs from Config Server
✔ Optional avoids startup exceptions if config server down

---

### Dynamic Refresh Support

Add Actuator refresh endpoint:

```properties
management.endpoints.web.exposure.include=refresh
```

➡ Allows runtime update (no restart required)

### Refresh URL

When config changed in Git → trigger refresh:

```
POST: http://localhost:8000/actuator/refresh
```

(Port differs per service)

---

## Flow Diagram

```
GitHub Repo (YMLs)
        ↓
   Config Server
        ↓
   Config Clients
  (Restaurant, Order ...)
```

---

## Example Setup Breakdown

| Project            | Role           | Config filename example | Port |
| ------------------ | -------------- | ----------------------- | ---- |
| config-server      | Central server | *none*                  | 8888 |
| restaurant-service | Client         | Restaurant-dev.yml      | 8000 |
| order-service      | Client         | Order-dev.yml           | 8100 |

---

## Benefits of Spring Cloud Config

✔ Centralized config for all microservices
✔ Version controlled config (Git)
✔ Dynamic updates with `/actuator/refresh`
✔ Environment-based profiles supported
✔ Zero downtime config changes

---

# API Gateway – Notes

## Why API Gateway in Microservices?

In microservices, we have:

* Many services
* Many ports
* Many URLs

Frontend cannot call each service directly → **complex**

👉 **API Gateway** acts as a **single entry point** for all client requests.

Example:

```
Frontend → Gateway: http://localhost:8500
Gateway → Routes to respective services
```

Gateway responsibilities:

* Routing (service → service)
* Load balancing
* Authentication / JWT validation
* Logging / Monitoring
* Rate limiting
* Security
* Centralized API calls

---

## Tools for API Gateway

| Tool                 | Status  | Architecture |
| -------------------- | ------- | ------------ |
| Netflix Zuul         | Old     | Non-Reactive |
| Spring Cloud Gateway | Current | Reactive     |

---

## Reactive vs Non-Reactive

| Mode         | Nature       | Used In        |
| ------------ | ------------ | -------------- |
| Reactive     | Asynchronous | Spring WebFlux |
| Non-Reactive | Synchronous  | Spring MVC     |

Reactive types:

* **Mono** → Single object
* **Flux** → Stream/List of objects

Spring Cloud Gateway = **Reactive**, high-performance

---

## Architecture Diagram

```
Client / UI
     ↓
API Gateway (8500)
     ↓
(lb://) Load Balanced Routing
     ↓
Eureka Discovery
     ↓
Microservices (User, Order, Restaurant, Delivery)
```

---

## API Gateway Setup

### Dependencies Required

* Spring Cloud Gateway
* Eureka Discovery Client
* Spring Boot Starter WebFlux (auto included)
* (Optional) Spring Security for JWT Validation

---

### Add Discovery Client Annotation

```java
@EnableDiscoveryClient
@SpringBootApplication
public class ApiGatewayApplication {}
```

---

### application.properties for Gateway

```properties
server.port=8500

# Eureka Configuration
eureka.client.register-with-eureka=true
eureka.client.fetch-registry=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# Gateway Routes (Restaurant Service)
spring.cloud.gateway.routes[0].id=RestaurantManagement
spring.cloud.gateway.routes[0].uri=lb://RESTAURANTMANAGEMENT
spring.cloud.gateway.routes[0].predicates[0]=Path=/restaurant/**
```

✔ `lb://` → tells gateway to do **Load Balancing using Eureka**
✔ Matches ALL restaurant related API requests

Example:

```
Request  → http://localhost:8500/restaurant/10
Gateway  → Routes to 8002 / 8003 / 8004 (Round Robin)
```

---

## Why JWT at Gateway?

* Gateway is the **entry point**
* Validate token once → forward auth user to microservices
* Avoid repeated authentication calls in every service

Benefits:
✔ Security centralized
✔ Less code duplication
✔ No direct access to internal services

---

## Advantages of API Gateway

| Advantage                       | Benefit                       |
| ------------------------------- | ----------------------------- |
| Single entry point              | Simple communication          |
| Auto load balancing             | High availability             |
| Integrated security             | JWT, rate limiting            |
| Service hiding                  | Internal services not exposed |
| Monitoring/Logging at one place | Easy maintenance              |

---

## Summary

| Feature                      | API Gateway    |
| ---------------------------- | -------------- |
| Routing                      | ✔              |
| Load Balancing               | ✔ (via Eureka) |
| Security (JWT)               | ✔              |
| Reactive Performance         | ✔              |
| Centralized request handling | ✔              |

---

# Circuit Breaker – Notes

Microservices call each other frequently → If one fails, others may also fail = **Cascading Failure**
To avoid this → Use **Circuit Breaker Pattern**

---

## Tools

| Tool         | Status                            |
| ------------ | --------------------------------- |
| Hystrix      | **Deprecated (Old, Netflix OSS)** |
| Resilience4j | **Modern & recommended**          |

➡ Spring Boot uses **Resilience4j** with Spring Cloud Circuit Breaker

---

## Why Circuit Breaker?

If a service is down or slow:

* Stop sending requests continuously
* Protect calling service from crashes/timeouts
* Provide fallback response
* Auto-recover once service becomes normal

---

## Circuit Breaker States

| State         | Meaning                     | Behavior                                       |
| ------------- | --------------------------- | ---------------------------------------------- |
| **Closed**    | Normal state                | Requests go through                            |
| **Open**      | Service is failing too much | No requests forwarded — send **fallback**      |
| **Half-Open** | Trial mode                  | Send a few requests → check if service is back |

---

### Example Behavior

* Threshold: **50% failures**
* Sliding window: **10 calls**
* If **5/10 calls fail → Goes to OPEN**
* Wait duration: **10 seconds**
* After 10s → Move to **HALF-OPEN** state and allow a few test calls
* If successful → return to **CLOSED**
* If failed → remain **OPEN**

---

## Setup in Spring Boot

### Dependencies

* Spring Cloud Circuit Breaker Reactive/Resilience4j
* Actuator (for health & monitoring)

---

### Properties (Configuration)

```properties
resilience4j.circuitbreaker.instances.restaurantCB.register-health-indicator=true
resilience4j.circuitbreaker.instances.restaurantCB.sliding-window-size=5
resilience4j.circuitbreaker.instances.restaurantCB.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.restaurantCB.wait-duration-in-open-state=10s
resilience4j.circuitbreaker.instances.restaurantCB.permitted-number-of-calls-in-half-open-state=2
resilience4j.circuitbreaker.instances.restaurantCB.automatic-transition-from-open-to-half-open-enabled=true
resilience4j.circuitbreaker.instances.restaurantCB.minimum-number-of-calls=3
```

Enable actuator endpoints:

```properties
management.endpoints.web.exposure.include=*
```

---

### Applying Circuit Breaker in Service Method

```java
@CircuitBreaker(name = "restaurantCB", fallbackMethod = "fallbackForRestaurant")
public String getRestaurantDetails(Long restaurantId) {
    // logic calling Restaurant Service
    return "Restaurant Details";
}
```

---

### Fallback Method

```java
public String fallbackForRestaurant(Long restaurantId, Throwable throwable) {
    return "Restaurant Service is currently down, please try again later!";
}
```

⚠ **Important Rules**

* Method name must match fallback method name
* Return type must be **same**
* Fallback method must include `Throwable` argument at the end

---

## Flow Summary

```
Order Service → Restaurant Service

Restaurant down →
Circuit Breaker becomes OPEN →
Order service returns friendly fallback message →
After wait time → HALF OPEN →
Test calls →
Recovered → CLOSED else stay OPEN
```

---

## Benefits

✔ No cascading failures
✔ Better user experience with fallback messages
✔ Protects dependent services
✔ Automatic recovery support
✔ Monitoring possible via Actuator

---
