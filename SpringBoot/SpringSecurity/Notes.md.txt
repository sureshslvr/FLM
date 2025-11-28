---

# 1) High-level architecture / flow (step-by-step)

Client → **Security Filter Chain** (Spring Security filters)
→ **AuthenticationManager**
→ **AuthenticationProvider** (e.g., `DaoAuthenticationProvider`, `JwtAuthenticationProvider` etc.)
→ `UserDetailsService.loadUserByUsername(...)` → returns `UserDetails` (username, password, authorities)
→ `PasswordEncoder.matches(raw, encoded)` verifies password
→ on success → **SecurityContext** populated with Authentication → request continues to DispatcherServlet → Controller

When using JWT (stateless):

* Login: client posts username/password → `AuthenticationManager` authenticates → server generates JWT and returns it.
* For each subsequent request: client adds token to `Authorization: Bearer <token>` header → a JWT filter validates token → if valid, it creates an `Authentication` and sets `SecurityContextHolder.getContext().setAuthentication(...)` → request proceeds.

Important pieces:

* `UserDetails` / `UserDetailsService` — how Spring loads user info
* `PasswordEncoder` — how passwords are hashed (bcrypt)
* `SecurityFilterChain` / `HttpSecurity` — configure endpoints & auth
* `OncePerRequestFilter` for JWT validation
* `AuthenticationManager` / `AuthenticationProvider` — core authentication interfaces

---

# 2) Key concepts explained

### Authentication vs Authorization (simple real-world example)

* **Authentication** = Are you who you say you are? (Logging into bank using username/password)
* **Authorization** = Once authenticated, what are you allowed to do? (Can you view account details or transfer money?)

### Password hashing & salt

* Use `BCryptPasswordEncoder` — it salts & hashes (salt embedded in hash).
* Store only hashed password in DB.

### AuthenticationProvider types

* `DaoAuthenticationProvider` — standard username/password via `UserDetailsService`
* `JwtAuthenticationProvider` — custom provider if validating JWT as authentication source
* `OAuth2AuthenticationProvider` — for OAuth2 / third-party logins (Google, Facebook)
* `LdapAuthenticationProvider` — LDAP-based auth

### UserDetails & UserDetailsService

* `UserDetails` has username, password (encoded), and `Collection<? extends GrantedAuthority>` roles.
* Implement `UserDetailsService` to load user from DB.

### SecurityFilterChain (HttpSecurity)

* Use bean `SecurityFilterChain securityFilterChain(HttpSecurity http)` to configure:

  * `authorizeHttpRequests()`, `permitAll()`, `authenticated()`
  * `csrf().disable()` for stateless APIs (be careful)
  * `sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)` for JWT

### JWT basics

* Format: `header.payload.signature` (base64url)
* Header includes algorithm (e.g., HS256)
* Payload includes claims (e.g., sub (username), roles, iat, exp)
* Signature = HMAC_SHA256(header + "." + payload, secret)
* Keep `secret` safe (used to validate token)
* Token expiry: short lived access token + optional refresh token

### CSRF & CORS

* **CSRF**: required for stateful sessions (cookies). For stateless JWT APIs, we typically `csrf().disable()` and use tokens. If using cookies, handle CSRF.
* **CORS**: configure allowed origins for cross-origin browsers (e.g., front-end port differences).

---

# 3) Full sample Spring Boot project (canonical, minimal & complete)

This example uses:

* Spring Boot 3 (Jakarta namespaces)
* Java 17
* MySQL
* Lombok
* Spring Data JPA
* Spring Security
* jjwt (io.jsonwebtoken) libraries

## 3.1 pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <modelVersion>4.0.0</modelVersion>

  <groupId>com.example</groupId>
  <artifactId>spring-security-jwt-demo</artifactId>
  <version>0.0.1-SNAPSHOT</version>
  <packaging>jar</packaging>

  <properties>
    <java.version>17</java.version>
    <spring.boot.version>3.2.0</spring.boot.version>
    <jjwt.version>0.11.5</jjwt.version>
  </properties>

  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-dependencies</artifactId>
        <version>${spring.boot.version}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>

  <dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Data JPA -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <!-- Spring Security -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <!-- MySQL Driver -->
    <dependency>
      <groupId>com.mysql</groupId>
      <artifactId>mysql-connector-j</artifactId>
      <scope>runtime</scope>
    </dependency>

    <!-- Lombok -->
    <dependency>
      <groupId>org.projectlombok</groupId>
      <artifactId>lombok</artifactId>
      <optional>true</optional>
    </dependency>

    <!-- JWT (JJWT) -->
    <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-api</artifactId>
      <version>${jjwt.version}</version>
    </dependency>
    <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-impl</artifactId>
      <version>${jjwt.version}</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-jackson</artifactId>
      <version>${jjwt.version}</version>
      <scope>runtime</scope>
    </dependency>

    <!-- Devtools (optional) -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-devtools</artifactId>
      <scope>runtime</scope>
      <optional>true</optional>
    </dependency>

    <!-- For testing -->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
    </dependency>

  </dependencies>

  <build>
    <plugins>
      <!-- Compiler plugin -->
      <plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.11.0</version>
        <configuration>
          <release>${java.version}</release>
        </configuration>
      </plugin>

      <!-- Spring Boot Maven plugin -->
      <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
      </plugin>
    </plugins>
  </build>
</project>
```

## 3.2 application.properties (src/main/resources/application.properties)

```properties
# Server
server.port=8080

# Datasource - change to your DB details
spring.datasource.url=jdbc:mysql://localhost:3306/spring_security_jwt?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=your_mysql_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# JWT properties
app.jwt.secret=ReplaceThisWithAStrongSecretKey_ChangeMe
app.jwt.expiration-ms=600000   # 10 minutes

# Logging
logging.level.org.springframework.security=INFO
```

> **Important:** Replace `app.jwt.secret` with a secure secret (keep in env vars / vault in real apps).

---

## 3.3 Entities (User & Authority) — many-to-many

`com.example.demo.model.Person.java`

```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password; // encoded

    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
        name = "p_authorities",
        joinColumns = @JoinColumn(name = "person_id"),
        inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities;
}
```

`com.example.demo.model.Authority.java`

```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authorities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // e.g., ROLE_USER, ROLE_ADMIN
}
```

## 3.4 Repositories

`PersonRepository.java`

```java
package com.example.demo.repository;

import com.example.demo.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByUsername(String username);
}
```

`AuthorityRepository.java`

```java
package com.example.demo.repository;

import com.example.demo.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByName(String name);
}
```

## 3.5 DTOs

`AuthRequest.java` (login)

```java
package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {
    private String username;
    private String password;
}
```

`AuthResponse.java`

```java
package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
}
```

`RegisterRequest.java`

```java
package com.example.demo.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
}
```

## 3.6 UserDetailsService Implementation

`CustomUserDetails.java` (implements UserDetails)

```java
package com.example.demo.security;

import com.example.demo.model.Person;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class CustomUserDetails implements UserDetails {

    private final Person person;

    public CustomUserDetails(Person person) {
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (person.getAuthorities() == null) return Collections.emptyList();
        return person.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return person.isEnabled(); }
}
```

`PersonUserDetailsService.java`

```java
package com.example.demo.security;

import com.example.demo.model.Person;
import com.example.demo.repository.PersonRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class PersonUserDetailsService implements UserDetailsService {

    private final PersonRepository personRepository;

    public PersonUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Person p = personRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new CustomUserDetails(p);
    }
}
```

## 3.7 JWT Utility (JwtService)

`JwtService.java`

```java
package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.Date;

@Service
public class JwtService {

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    private Key getSigningKey() {
        // use HS256
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String username, Collection<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        Date now = new Date();
        Date expiry = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            // log or handle invalid token
            return false;
        }
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey())
                .build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
```

## 3.8 JWT Authentication Filter

`JwtAuthFilter.java`

```java
package com.example.demo.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final PersonUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, PersonUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            if (jwtService.validateToken(token)) {
                username = jwtService.extractUsername(token);
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtService.validateToken(token)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

## 3.9 Security Configuration

`SecurityConfig.java`

```java
package com.example.demo.config;

import com.example.demo.security.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
public class SecurityConfig {

    private final PersonUserDetailsService userDetailsService;
    private final JwtService jwtService;

    public SecurityConfig(PersonUserDetailsService uds, JwtService jwtService) {
        this.userDetailsService = uds;
        this.jwtService = jwtService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtFilter = new JwtAuthFilter(jwtService, userDetailsService);

        http
            .csrf(csrf -> csrf.disable()) // stateless REST - disable or configure token
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // for H2 console (development only)
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
```

## 3.10 Auth Controller (login & register)

`AuthController.java`

```java
package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.security.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final PersonRepository personRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authManager,
                          PersonRepository personRepository,
                          AuthorityRepository authorityRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.authManager = authManager;
        this.personRepository = personRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {

        if (personRepository.findByUsername(req.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        Authority userRole = authorityRepository.findByName("ROLE_USER")
                .orElseGet(() -> authorityRepository.save(Authority.builder().name("ROLE_USER").build()));

        Person person = Person.builder()
                .username(req.getUsername())
                .password(passwordEncoder.encode(req.getPassword()))
                .authorities(Set.of(userRole))
                .enabled(true)
                .build();

        personRepository.save(person);

        return ResponseEntity.ok("Registered");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
            );

            // build roles list
            Person person = personRepository.findByUsername(req.getUsername()).get();
            List<String> roles = person.getAuthorities().stream().map(a -> a.getName()).collect(Collectors.toList());

            String token = jwtService.generateToken(req.getUsername(), roles);

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    // an endpoint to test auth
    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication auth) {
        return ResponseEntity.ok(auth.getPrincipal());
    }
}
```

## 3.11 A protected demo controller

`HelloController.java`

```java
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, secure world!";
    }
}
```

## 3.12 Main application class

`DemoApplication.java`

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {
      SpringApplication.run(DemoApplication.class, args);
  }
}
```

---

# 4) How to run & quick curl examples

1. Create MySQL database:

```sql
CREATE DATABASE spring_security_jwt;
```

2. Update `spring.datasource.*` in `application.properties` with your MySQL credentials.
3. Build & run:

```bash
mvn clean package
mvn spring-boot:run
```

### Register user:

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"pass123"}'
```

### Login to get token:

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"john","password":"pass123"}'
```

Response:

```json
{"token":"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."}
```

### Call protected endpoint:

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/hello
```

---

# 5) Notes, best practices & security tips

* **Never** store plain secrets in properties — use environment variables or secret manager.
* Use short-lived access tokens + long-lived refresh tokens (store refresh tokens securely; consider rotating).
* In production, use HTTPS for all requests.
* Protect `/actuator` endpoints or disable them.
* Use role-based authorities: `ROLE_USER`, `ROLE_ADMIN`. Use `@PreAuthorize("hasRole('ADMIN')")` for method-level checks.
* Implement logout by client deleting token; consider blacklisting tokens (DB) if necessary.
* Consider token revocation strategies for long-lived tokens.
* Use `PasswordEncoder` (BCrypt) to hash passwords. Do not custom roll your own hashing.
* In high-scale systems, consider opaque tokens + session store, or JWT with short TTL + refresh token.

---

# 6) Common Spring Security Interview Q & A

**Q1: What’s the difference between authentication and authorization?**
A1: Authentication verifies identity (login). Authorization checks permissions (what actions are allowed).

**Q2: What is `UserDetailsService`?**
A2: Interface to load user-specific data. Implement `loadUserByUsername` to return `UserDetails`.

**Q3: Why use `PasswordEncoder`?**
A3: To hash & salt passwords before storing in DB; avoid storing plaintext.

**Q4: How does Spring Security know a user is authenticated across requests with JWT?**
A4: A filter validates the JWT on each request and sets the `Authentication` in `SecurityContextHolder`.

**Q5: Why stateless REST apps often disable CSRF?**
A5: CSRF attacks target cookie/session-based auth. Token-based stateless APIs (Authorization header) are not vulnerable the same way; careful though.

**Q6: How do you refresh JWT?**
A6: Use a separate refresh token (longer-lived) to request new access tokens securely. Store refresh tokens safely (e.g., httpOnly cookies).

**Q7: What is `AuthenticationManager`?**
A7: Core interface that performs authentication by delegating to `AuthenticationProvider`s.

**Q8: What is `SecurityFilterChain`?**
A8: Configures order of security filters, authorization rules, CSRF, cors, session management etc.

**Q9: Which password encoding algorithm is recommended?**
A9: BCrypt (`BCryptPasswordEncoder`) — adaptive and secure.

**Q10: Where should secrets be stored in production?**
A10: Secret manager (AWS Secrets Manager, Vault) or environment variables. Not in code or public repos.

---

# 7) Extra tips & follow-ups

* If you want role-based method security, add `@EnableMethodSecurity` in config and use `@PreAuthorize(...)` annotations.
* For social logins, use `spring-boot-starter-oauth2-client`.
* For token introspection or OIDC, use `spring-security-oauth2-resource-server`.
* To store token revocation/blacklist, create a `revoked_tokens` table and check in filter.

---