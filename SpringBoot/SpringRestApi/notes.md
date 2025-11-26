# Spring REST API — Clear step-by-step

A compact, practical guide to building REST APIs with Spring / Spring Boot. I cleaned up your notes, corrected mistakes, added missing best practices and examples.

---

# 1. What is an API / REST?

* **API (Application Programming Interface):** contract that lets a client (frontend, mobile, another service) talk to a server.
* **REST (Representational State Transfer):** architectural style for web APIs that uses HTTP methods, URIs, and standard status codes.
* Common payload format: **JSON** (JavaScript Object Notation). SOAP (XML-based) is older and less common for modern microservices.

---

# 2. Key REST principles (short)

* **Resources** identified by URIs (e.g. `/users/42`).
* Use **HTTP methods** to express intent (GET, POST, PUT, PATCH, DELETE).
* **Stateless** server: each request contains all info to process it.
* Use appropriate **HTTP status codes**.
* Representations (usually JSON) of resources are returned/accepted.

---

# 3. HTTP methods & semantics

* **GET** — fetch resource(s). Safe, idempotent. 200 OK (or 204 No Content).
* **POST** — create new resource. Not idempotent. 201 Created + `Location` header recommended.
* **PUT** — full update/replace of resource. Idempotent. 200/204.
* **PATCH** — partial update. Not necessarily idempotent (but can be). 200/204.
* **DELETE** — delete a resource. Idempotent. 204 No Content common.
* **HEAD/OPTIONS** — metadata/negotiation.

---

# 4. Status code highlights

* **2xx** Success: 200 OK, 201 Created, 204 No Content
* **4xx** Client error: 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found, 409 Conflict
* **5xx** Server error: 500 Internal Server Error

Best practice: return meaningful status codes and a JSON error body on errors.

---

# 5. Spring controller types

* `@Controller` — for MVC views (JSP/Thymeleaf). Returns view names.
* `@RestController` — shortcut for `@Controller + @ResponseBody`. Controller methods return data (JSON) directly.

---

# 6. DTO (Data Transfer Object)

* A DTO is a **data shape** used to transfer data between client and server.
* Usually a subset or rearrangement of the entity (no persistence annotations).
* Use DTOs to avoid exposing internal entity structure, control fields, and validate input.

Example:

```java
public class UserDto {
    private String name;
    private String email;
    // getters/setters
}
```

---

# 7. Request body & JSON binding

* `@RequestBody` on a controller parameter tells Spring to deserialize JSON to a Java object (via Jackson by default).
* Example:

```java
@PostMapping("/users")
public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto dto) { ... }
```

* Use `@Valid` + validation annotations (`@NotNull`, `@Email`, etc.) to validate input.

---

# 8. Path vs Query parameters

* `@PathVariable` — use when the value is part of the resource path (required).

  ```java
  @GetMapping("/users/{id}")
  public UserDto get(@PathVariable Long id) { ... }
  ```
* `@RequestParam` — use for optional query parameters or filters:

  ```java
  @GetMapping("/users")
  public Page<UserDto> list(@RequestParam(required=false) String name) { ... }
  ```

---

# 9. BeanUtils and mapping

* `BeanUtils.copyProperties(source, target)` copies matching properties (shallow copy). Works for simple fields but has limitations (no deep copy, different property names).
* Better alternatives for robust mapping:

  * **MapStruct** (compile-time mapper)
  * **ModelMapper** or manual mapping for clarity and type-safety

---

# 10. Repositories, @Query, @Modifying, @Transactional

* Use Spring Data JPA repositories:

  ```java
  public interface UserRepository extends JpaRepository<User, Long> {}
  ```
* `@Query` for custom JPQL/SQL:

  ```java
  @Query("UPDATE User u SET u.active = false WHERE u.lastLogin < :date")
  @Modifying
  @Transactional
  int deactivateOldUsers(@Param("date") LocalDate date);
  ```
* Notes:

  * `@Modifying` required for update/delete queries.
  * `@Transactional` needed for modifying queries (ensures commit/rollback). Service layer typically carries transactions.

---

# 11. ResponseEntity — custom status & headers

* Use `ResponseEntity<T>` to control HTTP status, headers, and body.

```java
@PostMapping("/users")
public ResponseEntity<UserDto> create(@RequestBody UserDto dto) {
    UserDto created = service.create(dto);
    URI location = URI.create("/users/" + created.getId());
    return ResponseEntity.created(location).body(created); // 201 + Location header
}
```

---

# 12. Exception handling (global)

* Centralize exception handling with `@ControllerAdvice` or `@RestControllerAdvice`.
* Use `@ExceptionHandler` methods for specific exceptions.

Example:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        ErrorResponse err = new ErrorResponse("NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        // build validation errors list
        return ResponseEntity.badRequest().body(...);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ErrorResponse("ERROR", "Unexpected error"));
    }
}
```

* `ErrorResponse` is a custom DTO containing `timestamp`, `status`, `message`, `details` etc.

---

# 13. Validation flow

* Annotate request DTO fields with `javax.validation` annotations (jakarta in newer versions):

  ```java
  public class UserDto {
     @NotBlank private String name;
     @Email @NotBlank private String email;
  }
  ```
* On controller method: `@RequestBody @Valid UserDto dto`
* Handle validation errors in `@ExceptionHandler(MethodArgumentNotValidException.class)`.

---

# 14. CORS (Cross-Origin Resource Sharing)

* For frontends hosted elsewhere, configure CORS:

  * At controller level: `@CrossOrigin(origins = "http://localhost:3000")`
  * Globally via `WebMvcConfigurer` or Spring Security config.

---

# 15. Content types & Accept headers

* Use `@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)` to require JSON.
* Use `produces = MediaType.APPLICATION_JSON_VALUE` to declare response content type.
* Respect `Accept` header from clients.

---

# 16. HATEOAS (optional)

* Hypermedia links in responses (Spring HATEOAS) — useful for discoverability but optional.

---

# 17. Security & Auth (brief)

* Authenticate/authorize APIs with Spring Security (JWT/OAuth2).
* Return 401 for unauthenticated, 403 for unauthorized.

---

# 18. Best practices / checklist

* Use DTOs for API surface; don’t expose entities directly.
* Validate input (`@Valid`) and return descriptive errors.
* Use appropriate HTTP status codes.
* Use `ResponseEntity` for explicit control.
* Keep controllers thin — move business logic to services.
* Centralize exception handling with `@ControllerAdvice`.
* Log errors with context (request id, user id).
* Version your APIs (`/api/v1/...`).
* Document APIs with OpenAPI/Swagger.
* Write integration tests (mock MVC / TestRestTemplate).

---

# 19. Example: Minimal REST controller + DTO + service sketch

```java
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService svc;
    public UserController(UserService svc) { this.svc = svc; }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> get(@PathVariable Long id) {
        UserDto dto = svc.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto dto) {
        UserDto created = svc.create(dto);
        URI loc = URI.create("/api/users/" + created.getId());
        return ResponseEntity.created(loc).body(created);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> patch(@PathVariable Long id, @RequestBody Map<String,Object> changes) {
        UserDto updated = svc.partialUpdate(id, changes);
        return ResponseEntity.ok(updated);
    }
}
```


