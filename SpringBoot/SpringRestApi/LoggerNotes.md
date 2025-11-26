# **Spring Logging – Complete Notes**

Spring Boot uses a powerful and flexible logging system by combining:

* **SLF4J** → Facade (API for logging)
* **Logback** → Default logging implementation
* (Optional) **Log4j2** → Alternative implementation

---

# **1. Logging Frameworks Overview**

### **1.1 Log4j**

* **Log4j 1.x** — Deprecated (EOL), not recommended.
* **Log4j 2.x** — Modern, safe, improved performance.

To use Log4j2, you need to exclude Logback from Spring Boot.

---

### **1.2 Logback (Default in Spring & Spring Boot)**

* Created by the same author as Log4j.
* Built-in support in Spring Boot.
* Faster + more configuration options.
* Config file: `logback-spring.xml` OR `logback.xml`.

---

### **1.3 SLF4J**

**S**imple **L**ogging **F**acade for **J**ava

* SLF4J is **NOT** a logging library.
* It is just a **specification / abstraction layer**.
* Allows switching backend (Logback, Log4j2, JUL) without changing code.

---

# **2. Logging Levels (Lowest → Highest)**

| Level     | Meaning                                           |
| --------- | ------------------------------------------------- |
| **TRACE** | Very detailed, used for debugging internal flows  |
| **DEBUG** | Development-level logs, shows internal operations |
| **INFO**  | General information; used in production           |
| **WARN**  | Indicates potential issue; not an exception       |
| **ERROR** | An actual failure occurred; exception logs        |
| **FATAL** | System crash or shutdown (Log4j2 only)            |

**Important rule:**
If the log level is set to `WARN`, it shows:

```
WARN, ERROR, FATAL
```

But it **does not show** INFO, DEBUG, TRACE.

---

# **3. Using Logger in Spring Boot**

### **3.1 Injecting Logger**

Use SLF4J:

```java
private static final Logger logger = LoggerFactory.getLogger(MyService.class);
```

### **3.2 Logging examples**

```java
logger.trace("Trace log...");
logger.debug("Debug log...");
logger.info("User created: {}", username);
logger.warn("Stock is low for productId: {}", productId);
logger.error("User not found: {}", id, ex);
```

---

# **4. Default Logging Behavior in Spring Boot**

* Default logging level: **INFO**
* Default appender: **Console**
* Default logging engine: **Logback**

---

# **5. Changing Logging Levels (application.properties)**

### **5.1 Global logging level**

```properties
logging.level.root=DEBUG
```

### **5.2 Per-package logging**

```properties
logging.level.com.myapp.service=TRACE
logging.level.com.myapp.repository=DEBUG
```

### **5.3 Per-class level**

```properties
logging.level.com.myapp.service.impl.UserServiceImpl=INFO
```

---

# **6. Logging to File**

Spring Boot supports file logging out of the box.

### **6.1 Write logs to a specific file**

```properties
logging.file.name=app.log
```

Creates the file in the root of the project.

---

### **6.2 Log file in a directory**

```properties
logging.file.path=/logs
```

Creates: `/logs/spring.log`

---

# **7. Custom log pattern**

Format logs with pattern:

```properties
logging.pattern.file=%d{yyyy-MM-dd HH:mm:ss} %-5level [%thread] %logger{36} - %msg%n
```

You can also change console format:

```properties
logging.pattern.console=%d{HH:mm:ss} %clr(%5p) %logger{36} - %msg%n
```

---

# **8. Using Logback (Advanced)**

Create `src/main/resources/logback-spring.xml`:

```xml
<configuration>
    <appender name="FILE" class="ch.qos.logback.core.FileAppender">
        <file>logs/app.log</file>
        <encoder>
            <pattern>%d %-5level %logger - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="INFO">
        <appender-ref ref="FILE"/>
    </root>
</configuration>
```

Spring Boot automatically loads it.

---

# **9. Switching to Log4j2 (Optional)**

### **Step 1: Remove Logback**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### **Step 2: Add Log4j2 Starter**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>
```

### **Step 3: Add log4j2-spring.xml**

---

# **10. Monitoring Tools (Log Aggregation)**

Enterprises use monitoring/log-management tools:

* **Splunk**
* **ELK / Elastic Stack (Elasticsearch + Logstash + Kibana)**
* **Grafana Loki**
* **Graylog**

These tools:

* Centralize logs
* Provide dashboards
* Provide alerting

---

# **11. Best Practices**

* Use **INFO** in production for business events.
* Use **DEBUG** only for local dev/testing.
* Never log passwords or sensitive data.
* Use structured logs (JSON) in microservices.
* Daily rolling logs (via Logback RollingFileAppender).
* Avoid `System.out.println()` — use proper loggers.

---

# **12. Example: Recommended Logger Usage**

```java
@Service
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public void createOrder(OrderRequest request){
        log.info("Creating order for userId {}", request.getUserId());

        try {
            // logic...
            log.debug("Order request details: {}", request);
        } catch (Exception ex) {
            log.error("Error creating order: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
```

