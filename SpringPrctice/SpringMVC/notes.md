# **Spring MVC — Complete Notes**

---

# **1. Overview**

Spring MVC is a web framework based on the **Model–View–Controller** pattern.
Uses **DispatcherServlet** as Front Controller, and maps all incoming requests to controllers.

---

# **2. Required Maven Dependencies (MOST IMPORTANT SECTION)**

Tomcat 10+ uses **Jakarta EE (jakarta.servlet)** → Spring 6 / Spring MVC 6 uses **jakarta.servlet**.

### ✔ Use these dependencies if using **Tomcat 10 / Java 17 / Spring 6**:

```xml
<dependencies>
    <!-- Spring Web MVC -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
        <version>6.1.2</version>
    </dependency>

    <!-- For Dependency Injection, Core Spring -->
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>6.1.2</version>
    </dependency>

    <!-- Servlet API (provided by Tomcat) -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>
    </dependency>

    <!-- JSP Support -->
    <dependency>
        <groupId>jakarta.servlet.jsp</groupId>
        <artifactId>jakarta.servlet.jsp-api</artifactId>
        <version>3.1.1</version>
        <scope>provided</scope>
    </dependency>

    <!-- JSTL (for JSP tags) -->
    <dependency>
        <groupId>org.eclipse.ee4j.jstl</groupId>
        <artifactId>jakarta.servlet.jsp.jstl</artifactId>
        <version>2.0.0</version>
    </dependency>

    <!-- Logging (Spring needs SLF4J) -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>2.0.9</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>2.0.9</version>
    </dependency>

</dependencies>
```

### ✔ If using Tomcat 9 or below (**javax.servlet**), use older Spring (5.x).

But **for Java 17**, Tomcat 10 is recommended → so use Spring 6 (jakarta).

---

# **3. Project Setup**

* Maven Project → `maven-archetype-webapp`
* Java Version → **17**
* Dynamic Web Module → **6.0**
* Target Runtime → **Tomcat 10+**
* Update `pom.xml` to include **jakarta** dependencies

---

# **4. Directory Structure**

```
src/main/java
    com.example.config
    com.example.controller
    com.example.model

src/main/webapp
    WEB-INF/
        views/
            home.jsp
```

---

# **5. Request Flow (Front Controller Pattern)**

```
Client → DispatcherServlet (Front Controller)
       → Controller
       → Returns view name or JSON
       → ViewResolver → JSP
       → Response
```

---

# **6. Java-based Configuration (NO XML)**

## **6.1 WebAppInitializer (replaces web.xml)**

```java
public class MyWebInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) {

        AnnotationConfigWebApplicationContext ctx =
                new AnnotationConfigWebApplicationContext();
        ctx.register(WebConfig.class);

        ServletRegistration.Dynamic servlet =
                container.addServlet("dispatcher", new DispatcherServlet(ctx));

        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");
    }
}
```

---

## **6.2 Spring MVC Config**

```java
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.example")
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver vr = new InternalResourceViewResolver();
        vr.setPrefix("/WEB-INF/views/");
        vr.setSuffix(".jsp");
        return vr;
    }
}
```

---

# **7. Controllers**

### **Controller returning JSP (View)**

```java
@Controller
@RequestMapping("/home")
public class HomeController {

    @GetMapping
    public String showHome(Model model) {
        model.addAttribute("msg", "Welcome!");
        return "home";  // /WEB-INF/views/home.jsp
    }
}
```

---

### **Controller returning JSON or Text**

```java
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello API";
    }
}
```

---

# **8. HTTP Methods**

| Method | Annotation       | Usage          |
| ------ | ---------------- | -------------- |
| GET    | `@GetMapping`    | fetch data     |
| POST   | `@PostMapping`   | create data    |
| PUT    | `@PutMapping`    | full update    |
| PATCH  | `@PatchMapping`  | partial update |
| DELETE | `@DeleteMapping` | delete data    |

---

# **9. HTTP Status Codes**

### **Success**

* **200 OK**
* **201 Created**
* **202 Accepted**
* **204 No Content**

### **Client Errors**

* **400 Bad Request**
* **401 Unauthorized**
* **403 Forbidden**
* **404 Not Found**
* **405 Method Not Allowed**
* **409 Conflict**

### **Server Errors**

* **500 Internal Server Error**
* **502 Bad Gateway**

---

# **10. Model**

```java
public class User {
    private int id;
    private String name;
}
```

---

# **11. Passing Data**

### **Via @RequestParam**

```java
@PostMapping("/save")
public String save(@RequestParam String name, Model model) {
    model.addAttribute("name", name);
    return "success";
}
```

### **Via @PathVariable**

```java
@GetMapping("/user/{id}")
public String getUser(@PathVariable int id, Model model) {
    model.addAttribute("id", id);
    return "profile";
}
```

### **JSON Body**

```java
@PostMapping("/add")
public User add(@RequestBody User user) {
    return user;
}
```

---

# **12. JSP View Resolution**

`InternalResourceViewResolver` converts:

```
return "home";
```

into:

```
/WEB-INF/views/home.jsp
```

---

# **13. Summary of Spring MVC Annotations**

| Annotation        | Meaning              |
| ----------------- | -------------------- |
| `@Controller`     | returns JSP          |
| `@RestController` | returns JSON         |
| `@ResponseBody`   | return body directly |
| `@EnableWebMvc`   | enables Spring MVC   |
| `@ComponentScan`  | scans for beans      |
| `@RequestMapping` | class/method mapping |
| `@GetMapping`     | GET                  |
| `@PostMapping`    | POST                 |
| `@PutMapping`     | PUT                  |
| `@DeleteMapping`  | DELETE               |
| `@PatchMapping`   | PATCH                |
| `@RequestParam`   | form params          |
| `@RequestBody`    | JSON body            |
| `@PathVariable`   | path param           |
| `@ModelAttribute` | form binding         |

---

# **14. Final Request Flow**

```
Client  
 → DispatcherServlet  
 → HandlerMapping  
 → Controller  
 → ModelAndView  
 → ViewResolver  
 → JSP  
 ← Response  
```

---

# **15. Extra (Optional but Useful)**

### Add Spring Web dependency bundle:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>6.1.2</version>
</dependency>
```

### Add Jackson for JSON:

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.16.1</version>
</dependency>
```

---
