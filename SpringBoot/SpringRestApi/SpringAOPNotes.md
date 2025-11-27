
---

# **Spring AOP – Complete Notes**

Spring AOP (**Aspect Oriented Programming**) helps handle **cross-cutting concerns**—logic that is common across many business methods.

---

# **1. What is AOP?**

AOP is a programming paradigm that allows adding reusable logic **without modifying core business code**.

### **Cross-Cutting Concerns Include:**

* Logging
* Security
* Caching
* Transactions
* Performance Monitoring
* Auditing
* Exception Handling

These concerns *cross* through multiple modules → AOP keeps them separate and clean.

---

# **2. Key AOP Terminology**

| Concept                 | Meaning                                                       |
| ----------------------- | ------------------------------------------------------------- |
| **Aspect**              | Class containing cross-cutting logic                          |
| **Advice**              | The action to apply at a specific point (before/after/around) |
| **JoinPoint**           | Actual point of execution, e.g., method call                  |
| **Pointcut**            | Expression defining *where* advice should run                 |
| **ProceedingJoinPoint** | Used in `@Around` to execute target method                    |
| **Weaving**             | Linking aspects with application code                         |
| **Proxy**               | Spring creates dynamic proxies to apply AOP behavior          |

---

# **3. Aspect**

An Aspect is a class containing advices.

```java
@Aspect
@Component
public class LoggingAspect {

}
```

Aspect classes must be registered as Spring beans.

---

# **4. Types of Advice**

### **1. @Before**

Runs **before** the method executes.

```java
@Before("execution(* com.app.service.*.*(..))")
public void beforeAdvice(JoinPoint jp) {
    System.out.println("Before method: " + jp.getSignature());
}
```

---

### **2. @After**

Runs **after** the method finishes (regardless of exception).

```java
@After("execution(* com.app.service.*.*(..))")
public void afterAdvice(JoinPoint jp) {
    System.out.println("After method: " + jp.getSignature());
}
```

---

### **3. @AfterReturning**

Runs **after method returns successfully**.

```java
@AfterReturning(
   value="execution(* com.app.service.*.*(..))",
   returning="result"
)
public void afterReturnAdvice(Object result) {
    System.out.println("Returned: " + result);
}
```

---

### **4. @AfterThrowing**

Runs **when method throws an exception**.

```java
@AfterThrowing(
   value="execution(* com.app.service.*.*(..))",
   throwing="ex"
)
public void afterThrowingAdvice(Exception ex) {
    System.out.println("Exception: " + ex.getMessage());
}
```

---

### **5. @Around (Most Powerful Advice)**

Runs code **before and after** the method.

Uses `ProceedingJoinPoint` to control method execution:

```java
@Around("execution(* com.app.service.*.*(..))")
public Object aroundAdvice(ProceedingJoinPoint pjp) throws Throwable {
    System.out.println("Before calling: " + pjp.getSignature());

    Object result = pjp.proceed();  // execute actual method

    System.out.println("After calling: " + pjp.getSignature());
    return result;
}
```

---

# **5. JoinPoint**

Represents the actual point of execution like method calls.

Useful methods:

```java
jp.getArgs()
jp.getSignature()
jp.getTarget()
```

Used in Before/After advices (not in Around).

---

# **6. ProceedingJoinPoint**

Only available in **Around** advice.

Purpose:

* Controls execution of target method.
* Captures arguments.
* Allows pre/post logic.
* Can modify or return custom values.

Usage:

```java
Object result = pjp.proceed();
```

---

# **7. Pointcut Expressions (execution syntax)**

### **Basic syntax**

```
execution(modifiers-pattern? return-type-pattern declaring-type-pattern? method-name-pattern(param-pattern) throws-pattern?)
```

### **Examples**

#### 1. Match specific method

```
execution(* com.app.service.OrderService.placeOrder(..))
```

#### 2. Match all methods in a class

```
execution(* com.app.service.OrderService.*(..))
```

#### 3. Match all methods in a package

```
execution(* com.app.service.*.*(..))
```

#### 4. Match by return type

```
execution(String com.app.service.*.*(..))
```

#### 5. Match all public methods

```
execution(public * *(..))
```

#### 6. Match all methods in service layer

```
execution(* com.app.service..*(..))
```

---

# **8. Creating Reusable Pointcuts**

```java
@Pointcut("execution(* com.app.service.*.*(..))")
public void serviceLayer() {}
```

Using it:

```java
@Before("serviceLayer()")
public void logBefore() { ... }
```

---

# **9. Add Spring AOP Dependency**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

This provides:

* AspectJ support
* AOP proxy creation
* Auto-configuration

---

# **10. Enable AOP (Spring Boot Auto-Enabled)**

Spring Boot automatically enables AOP, but in manual config:

```java
@EnableAspectJAutoProxy
```

---

# **11. Real Use Cases for AOP**

### ✔ Logging

### ✔ Security checks

### ✔ Authentication / Authorization

### ✔ Transactions

### ✔ Caching

### ✔ Performance Monitoring (timers)

### ✔ Retry mechanisms

### ✔ Input validation

### ✔ Auditing (createdBy, updatedBy)

---

# **12. AOP Proxying (Important for Interviews)**

Spring AOP uses **Proxy-based AOP**:

* For interfaces → JDK dynamic proxies
* For classes → CGLIB proxies

Limitations:

* Only **method-level** interception
* Only **Spring beans** can be advised

---

# **13. Example: Performance Logging Aspect**

```java
@Aspect
@Component
public class PerformanceAspect {

    @Around("execution(* com.app.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = pjp.proceed();

        long end = System.currentTimeMillis();
        System.out.println("Executed " + pjp.getSignature() +
                           " in " + (end - start) + " ms");
        return result;
    }
}
```

---

# **14. Best Practices**

* Keep pointcuts small and focused.
* Use DTOs/entities in services, not within aspects.
* Avoid very broad pointcuts (e.g., `execution(* *(..))`).
* Do not put business logic in aspects.
* Use AOP for technical concerns only.
* Combine AOP with Actuator/Micrometer for monitoring.

---

# **15. Summary**

| Component               | Meaning                               |
| ----------------------- | ------------------------------------- |
| **Aspect**              | class containing cross-cutting logic  |
| **Advice**              | before/after/around actions           |
| **Pointcut**            | defines where to apply advice         |
| **JoinPoint**           | actual execution method               |
| **ProceedingJoinPoint** | used in @Around to call target method |
| **AOP Dependency**      | spring-boot-starter-aop               |

---
