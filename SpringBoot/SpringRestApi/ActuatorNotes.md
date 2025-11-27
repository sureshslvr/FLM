---

# **Spring Boot Actuator – Complete Notes**

Spring Boot Actuator provides **built-in endpoints** to monitor & manage applications in development and production.

---

# **1. What is Actuator?**

* A module in Spring Boot that exposes **application insights** and **operational information** over HTTP.
* Shows:

  * `beans`
  * `mappings`
  * `configprops`
  * `health`
  * `metrics`
  * `env`
  * `info`
  * `loggers`
  * `caches`
  * plus many more…

Actuator makes Spring apps **production-ready** by default.

---

# **2. Add Actuator Dependency**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

---

# **3. Actuator Base URL**

```
http://localhost:8080/actuator
```

### **Default behavior**

* Only `/actuator/health` is exposed by default for security reasons.

---

# **4. Exposing Actuator Endpoints**

Enable endpoints in `application.properties`:

### **Expose selected endpoints**

```properties
management.endpoints.web.exposure.include=health,beans,metrics,info
```

### **Expose all endpoints**

```properties
management.endpoints.web.exposure.include=*
```

### **Exclude some endpoints**

```properties
management.endpoints.web.exposure.exclude=env,beans
```

---

# **5. Important Actuator Endpoints**

| Endpoint          | Purpose                                  |
| ----------------- | ---------------------------------------- |
| `/health`         | Application health (DB, disk, ping)      |
| `/metrics`        | System metrics (CPU, JVM, HTTP requests) |
| `/metrics/{name}` | Specific metric details                  |
| `/beans`          | List of all Spring beans                 |
| `/env`            | Environment variables & properties       |
| `/mappings`       | All controller mappings                  |
| `/configprops`    | Configuration properties                 |
| `/loggers`        | View/modify log levels                   |
| `/info`           | Custom build/info data                   |
| `/caches`         | Cache details                            |

---

# **6. Changing Actuator Port**

Run actuator on a **different management port**:

```properties
management.server.port=13000
```

Then actuator URLs become:

```
http://localhost:13000/actuator
http://localhost:13000/actuator/health
```

> Useful in microservices to separate app traffic & monitoring traffic.

---

# **7. Custom Information in /actuator/info**

```properties
management.info.env.enabled=true
info.app.name=OrderService
info.app.version=1.0.0
info.app.owner=Team-A
```

Now `/actuator/info` returns the above.

---

# **8. Custom Metrics (Micrometer)**

Spring Boot uses **Micrometer** under the hood to collect metrics.

Micrometer provides:

* **Counters** → how many times something happened
* **Timers** → how long an operation takes
* **Gauges** → real-time values (cache size, queue size)

### **Add Micrometer dependency (usually included by default)**

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-core</artifactId>
</dependency>
```

---

# **9. Creating Custom Timers & Counters Using Annotations**

Micrometer provides annotations:

### ⚡ Timer — measure execution time

```java
@Timed(value = "order.placed.time")
public void placeOrder() { ... }
```

### ⚡ Counter — count events

```java
@Counted(value = "order.placed.count")
public void placeOrder() { ... }
```

---

# **10. Required: TimedAspect & CountedAspect Registration**

These annotations **do NOT work automatically** unless you register AOP aspects.

### Add configuration class:

```java
@Configuration
public class MetricsConfig {

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }
}
```

Now timers and counters will be registered properly.

---

# **11. Manual Metrics (Programmatic way)**

### **Counter example**

```java
@Autowired
MeterRegistry registry;

Counter orderCounter;

@PostConstruct
public void init() {
    orderCounter = Counter.builder("orders.created.count")
                          .description("Number of orders placed")
                          .register(registry);
}

public void placeOrder() {
    orderCounter.increment();
}
```

### **Timer example**

```java
Timer timer = registry.timer("orders.process.time");

public void processOrder() {
    timer.record(() -> {
        // business logic
    });
}
```

---

# **12. Viewing Metrics**

Go to:

```
/actuator/metrics
```

To view specific metric:

```
/actuator/metrics/order.placed.time
/actuator/metrics/order.placed.count
```

Also includes built-in metrics:

* `jvm.memory.used`
* `system.cpu.usage`
* `http.server.requests`
* `process.uptime`
* `tomcat.sessions.active.current`

---

# **13. Securing Actuator**

Recommended for production:

```properties
management.endpoints.web.exposure.include=health,info,metrics
management.endpoint.health.show-details=never
```

Use Spring Security to restrict access.

---

# **14. Real-World Usage with Prometheus & Grafana**

* Micrometer integrates with:

  * Prometheus
  * Grafana
  * Datadog
  * New Relic
  * CloudWatch
* Export metrics with Prometheus endpoint:

  ```
  management.endpoint.prometheus.enabled=true
  management.endpoints.web.exposure.include=prometheus
  ```

---

# **15. Summary**

### Actuator gives:

* Health checks
* Detailed metrics
* Bean list
* Mappings
* Loggers
* System info
* Environment
* Cache data

### Benefits:

* Production-ready
* Monitoring
* Performance insights
* Supports custom metrics
* Integrates with monitoring tools

---

