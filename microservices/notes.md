# **📘 MASTER NOTES – MICROSERVICES ARCHITECTURE (Complete + Correct + Expanded)**

*(Everything from Spring Web → DB → Service Communication → Discovery → Load Balancing → Config Server → Gateway → Circuit Breakers → Testing)*

---

# **1. Microservices Architecture – Overview**

Microservices is an **architectural style** where applications are built as a collection of **small, independent, loosely coupled services**, each owning a single business functionality.

---

# **2. Monolithic vs Microservices**

## **Monolithic Architecture**

* Single application containing all modules: UI + Business Logic + DB.
* Deployed as one unit (WAR/JAR).
* Tight coupling.
* Scaling requires scaling entire app.
* Hard to maintain and understand once it grows.

## **Microservices Architecture**

* Application broken into multiple small services.
* Each microservice has its own codebase & database.
* Communicate using REST APIs or Messaging.

---

# **3. Advantages of Microservices**

* ✔ **Scalability** → scale only required services (Restaurant Service only).
* ✔ **Independent Deployment** → deploy without affecting others.
* ✔ **Fault Tolerance** → failure in one service does not affect others.
* ✔ **Faster Development** → teams work independently.
* ✔ **Better Code Maintainability** → small codebase is easy to manage.
* ✔ **Technology Flexibility** → each microservice can use different language/framework.
* ✔ **Loose Coupling**

---

# **4. Disadvantages of Microservices**

* ❌ **Complex to test** → involves multiple services.
* ❌ **Distributed Logging Difficult** → requires ELK, Splunk.
* ❌ **Complex Maintenance**
* ❌ **More Cost** → many containers, servers.
* ❌ **Service Management is Hard**
* ❌ **Data Inconsistency** → distributed databases.
* ❌ **Deployment Setup is Complex**

---

# **5. Domain Example – Food Delivery Application**

### **1. Restaurant Service**

* Restaurant → (id, name, phone, rating, list of items)
* Address → (id, landmark, city, state, country, pincode)
* Item → (id, name, price, category, isAvailable, rating, restaurantId)

### **2. Order Service**

* Order → (id, userId, restaurantId, status, price, list of orderItems)
* OrderItem → (id, itemId, quantity)

### **3. User Service**

* User → (id, name, email, phone, password, listOfAddresses)

### **4. Delivery Partner Service**

* DeliveryPerson → (id, name, aadhar, phone, email, isAvailable, rating)
* DeliveryAssignment → (assignmentId, orderId, deliveryPersonId, status, timestamp)

### **Relationships**

* Restaurant → Item (**One-to-Many**)
* User → Orders (**One-to-Many**)
* User → Address (**One-to-Many**)
* DeliveryPerson → DeliveryAssignment (**One-to-Many**)

---

# **6. Microservice Dependencies (Common for Every Service)**

```txt
spring-boot-starter-web
spring-boot-starter-data-jpa
mysql-connector-j
lombok
spring-boot-devtools
spring-boot-starter-security (optional - if using security)
```

**In application.properties**

```properties
server.port=8081
spring.datasource.url=jdbc:mysql://localhost:3306/db
spring.datasource.username=root
spring.datasource.password=1234
```

---

# **7. Builder Pattern**

Use Lombok:

```java
@Builder
public class Restaurant { ... }
```

---

# **8. Inter-Service Communication**

Microservices must talk to each other.

## **A. Synchronous Communication**

→ Wait for response
Used for **critical dependent flows**.

### **1) RestTemplate (Old but used widely)**

```java
restTemplate.getForObject(url, Response.class);
restTemplate.postForEntity(url, object, Response.class);
restTemplate.exchange(...);
```

### **2) OpenFeign (Recommended)**

* Declarative HTTP Client
* Auto load-balancing with Eureka
* Cleaner than RestTemplate

```java
@FeignClient(name="RESTAURANT-SERVICE")
public interface RestaurantClient {
    @GetMapping("/restaurants/{id}")
    RestaurantDto getRestaurant(@PathVariable Long id);
}
```

Enable Feign:

```java
@EnableFeignClients
```

## **B. Asynchronous Communication**

→ Fire & forget
Used for background activities (Notifications, Analytics).

### Tools:

* **WebClient (Reactive)**
* **RabbitMQ**
* **Kafka**

---

# **9. Service Discovery – Eureka**

Eureka helps services find each other dynamically.

## **A. Eureka Server**

Dependencies:

```txt
spring-cloud-starter-netflix-eureka-server
```

Enable:

```java
@EnableEurekaServer
```

Properties:

```properties
server.port=8761
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false
```

Access:

```
http://localhost:8761
```

## **B. Eureka Client**

Dependencies:

```txt
spring-cloud-starter-netflix-eureka-client
```

Properties:

```properties
eureka.client.fetch-registry=true
eureka.client.register-with-eureka=true
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

server.port=0   # generate random port
eureka.instance.instance-id=${spring.application.name}:${random.value}
```

---

# **10. Load Balancing**

## **Client-Side Load Balancer (Spring Cloud LoadBalancer)**

Used by:

* RestTemplate (with @LoadBalanced)
* FeignClient

Round Robin by default.

```java
@Bean
@LoadBalanced
public RestTemplate restTemplate() {
    return new RestTemplate();
}
```

Now instead of:

```
http://localhost:8002/orders
```

Use service name:

```
http://ORDER-SERVICE/orders
```

## **Server-Side Load Balancing**

* Kubernetes (Pods)
* AWS Elastic Load Balancer

---

# **11. API Gateway (Spring Cloud Gateway)**

Acts as a single entry point to all microservices.
Supports:

* Routing
* Load balancing
* JWT validation
* Rate limiting
* Filters

Dependencies:

```
spring-cloud-starter-gateway
spring-cloud-starter-netflix-eureka-client
```

Properties:

```properties
spring.cloud.gateway.routes[0].id=restaurant_route
spring.cloud.gateway.routes[0].uri=lb://RESTAURANT-SERVICE
spring.cloud.gateway.routes[0].predicates[0]=Path=/restaurants/**
```

---

# **12. Profiles (dev, qa, prod)**

application.yml

```yaml
spring:
  profiles:
    active: dev
```

application-dev.yml
application-qa.yml
application-prod.yml

Used for:

* DB configuration
* Server ports
* Service URLs

---

# **13. Config Server (Externalized Configuration)**

### **Config Server**

Dependencies:

```
spring-cloud-config-server
spring-boot-starter-web
spring-boot-starter-actuator
```

Enable:

```java
@EnableConfigServer
```

Properties:

```properties
server.port=8888
spring.cloud.config.server.git.uri=https://github.com/user/config-repo
spring.cloud.config.server.git.clone-on-start=true
spring.cloud.config.server.git.default-label=main
```

Check config:

```
http://localhost:8888/restaurant-service/dev
```

### **Config Clients**

Add:

```
spring-cloud-starter-config
spring-boot-starter-actuator
```

Properties:

```properties
spring.config.import=optional:configserver:http://localhost:8888
management.endpoints.web.exposure.include=refresh
```

To refresh:

```
POST /actuator/refresh
```

---

# **14. Circuit Breaker (Resilience4j)**

When a service is down, prevent continuous failed calls.

States:

* **Closed** → normal
* **Open** → stop calling service
* **Half-Open** → test if service is back

Example Config:

```properties
resilience4j.circuitbreaker.instances.restaurantCB.failure-rate-threshold=50
resilience4j.circuitbreaker.instances.restaurantCB.sliding-window-size=10
resilience4j.circuitbreaker.instances.restaurantCB.wait-duration-in-open-state=10s
resilience4j.circuitbreaker.instances.restaurantCB.permitted-number-of-calls-in-half-open-state=3
management.endpoints.web.exposure.include=*
```

Usage:

```java
@CircuitBreaker(name = "restaurantCB", fallbackMethod = "restaurantFallback")
public String getRestaurant(Long id) {
    return restaurantClient.getRestaurant(id);
}

public String restaurantFallback(Long id, Throwable t) {
    return "Restaurant service temporarily unavailable. Try later.";
}
```

---

# **15. Unit Testing vs Integration Testing**

## **Unit Testing**

Tools:

* JUnit5
* Mockito

Annotations:

* `@Test`
* `@Mock`
* `@InjectMocks`
* `@ExtendWith(MockitoExtension.class)`
* `@BeforeEach`, `@AfterEach`
* `@BeforeAll`, `@AfterAll`
* `@Disabled`

## **Integration Testing**

* Tests full service flow
* Uses:

    * `@SpringBootTest`
    * TestContainers (recommended)

---

# **16. Missing but Important Microservices Concepts**

### **API Versioning**

`/api/v1/orders`, `/api/v2/orders`

### **Distributed Tracing**

* Sleuth
* Zipkin
* OpenTelemetry

### **Centralized Logging**

* ELK Stack (Elastic + Logstash + Kibana)
* Splunk

### **Service Mesh (Advanced)**

* Istio
* Linkerd

### **Containerization**

* Dockerfile
* Kubernetes deployment

---

# **17. FINAL SUMMARY DIAGRAM — Full Microservice Flow**

```
[Client / Frontend]
        |
        v
  [API Gateway]  <-- JWT validation, routing, rate limit
        |
        v
  [Eureka Discovery Server]
        |
        +--> Order Service ----> Restaurant Service
        |          ^                |
        |          |                |
        |       Resilience4j    DB (MySQL)
        |
        +--> User Service
        |
        +--> Delivery Service
```

