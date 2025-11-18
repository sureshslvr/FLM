# Spring JDBC — Step-by-step notes

## 1) Core idea

Spring’s **JdbcTemplate** simplifies JDBC by handling boilerplate: creating connections, preparing statements, executing queries, mapping rows to objects, and converting checked SQLExceptions into Spring’s **DataAccessException** (unchecked).

---

## 2) Maven dependencies (minimum)

```xml
<!-- Spring JDBC + Spring Context -->
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-jdbc</artifactId>
  <version>REPLACE_WITH_YOUR_VERSION</version>
</dependency>

<!-- MySQL driver -->
<dependency>
  <groupId>mysql</groupId>
  <artifactId>mysql-connector-java</artifactId>
  <version>REPLACE_WITH_YOUR_VERSION</version>
</dependency>
```

(You may add `spring-context` or use Spring Boot which manages versions.)

---

## 3) DataSource

JdbcTemplate needs a **DataSource** (connection factory). You can use:

* `DriverManagerDataSource` (simple, for tests/dev only)
* Connection pool (recommended): HikariCP, TomcatCP, etc.

### Java config example (recommended)

```java
@Configuration
public class DbConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/mydb");
        ds.setUsername("user");
        ds.setPassword("pass");
        return ds;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }
}
```

If using a pool:

```java
@Bean
public HikariDataSource dataSource() {
    HikariConfig cfg = new HikariConfig();
    cfg.setJdbcUrl(...); cfg.setUsername(...); cfg.setPassword(...);
    return new HikariDataSource(cfg);
}
```

---

## 4) @Component vs @Bean

* `@Component` — used on your own classes (scanned by component-scan). Example: `@Repository`, `@Service`, `@Component`.
* `@Bean` — used in `@Configuration` methods to *produce* instances (e.g., DataSource, JdbcTemplate) when type is created/configured in code.

---

## 5) Basic JdbcTemplate methods

* **DML (INSERT/UPDATE/DELETE)**
  `int rows = jdbcTemplate.update(sql, arg1, arg2, ...);`

* **Query single object**
  `T obj = jdbcTemplate.queryForObject(sql, rowMapper, args...);`
  Use when query returns exactly one row (throws exception if 0 or >1).

* **Query list of objects**
  `List<T> list = jdbcTemplate.query(sql, rowMapper, args...);`

* **Query for primitive / single column**
  `Integer cnt = jdbcTemplate.queryForObject(sql, Integer.class, args...);`

* **Batch update**
  `jdbcTemplate.batchUpdate(sql, batchArgs);`

* **Insert and get generated key**
  Use `KeyHolder` with `PreparedStatementCreator`.

---

## 6) RowMapper

`RowMapper<T>` maps a `ResultSet` row to a POJO. It’s a functional interface; you can use lambdas.

### Example POJO

```java
public class Employee {
    private int id;
    private String name;
    private double salary;
    // constructors, getters, setters
}
```

### RowMapper as lambda

```java
RowMapper<Employee> mapper = (rs, rowNum) ->
    new Employee(rs.getInt("id"), rs.getString("name"), rs.getDouble("salary"));

// Usage
Employee e = jdbcTemplate.queryForObject(
    "SELECT id, name, salary FROM employee WHERE id = ?",
    mapper, 123);
```

### Inline anonymous class (older style)

```java
jdbcTemplate.queryForObject(
  "SELECT id, name, salary FROM employee WHERE id = ?",
  new RowMapper<Employee>() {
      @Override
      public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
          return new Employee(rs.getInt(1), rs.getString(2), rs.getDouble(3));
      }
  }, 123);
```

### BeanPropertyRowMapper (convenient)

Maps columns to properties by name (camel case). Example:

```java
List<Employee> employees = jdbcTemplate.query(
    "SELECT id, name, salary FROM employee",
    new BeanPropertyRowMapper<>(Employee.class));
```

---

## 7) Example repository using JdbcTemplate

```java
@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbc;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbc = jdbcTemplate;
    }

    public int save(Employee emp) {
        return jdbc.update(
            "INSERT INTO employee(name, salary) VALUES(?, ?)",
            emp.getName(), emp.getSalary());
    }

    public Employee findById(int id) {
        return jdbc.queryForObject(
            "SELECT id, name, salary FROM employee WHERE id = ?",
            (rs, rn) -> new Employee(rs.getInt("id"),
                                     rs.getString("name"),
                                     rs.getDouble("salary")),
            id);
    }

    public List<Employee> findAll() {
        return jdbc.query("SELECT id, name, salary FROM employee",
            new BeanPropertyRowMapper<>(Employee.class));
    }

    public int updateSalary(int id, double newSalary) {
        return jdbc.update("UPDATE employee SET salary = ? WHERE id = ?", newSalary, id);
    }

    public int delete(int id) {
        return jdbc.update("DELETE FROM employee WHERE id = ?", id);
    }
}
```

---

## 8) Transactions

Annotate service layer methods with `@Transactional` (from `spring-tx`) to group multiple JDBC operations into one transaction.

```java
@Service
public class EmployeeService {
    @Transactional
    public void transferSalary(int fromId, int toId, double amount) {
        // multiple jdbcTemplate.update(...) calls
    }
}
```

---

## 9) Exception handling

Spring converts SQLExceptions into `DataAccessException` hierarchy (runtime). You can catch `DataAccessException` if you want to handle DB errors.

```java
try {
    jdbc.update(...);
} catch (DataAccessException ex) {
    // log and handle
}
```

---

## 10) Prepared statements & security

Always use `?` placeholders with `jdbcTemplate.update/query(...)` to avoid SQL injection. JdbcTemplate uses PreparedStatement under the hood.

---

## 11) Useful advanced features (short list)

* `NamedParameterJdbcTemplate` — use named parameters (`:name`) instead of `?`.
* `SimpleJdbcInsert` / `SimpleJdbcCall` — helpers for inserts/procedures.
* `BatchPreparedStatementSetter` or `batchUpdate` for bulk operations.
* `KeyHolder` with `PreparedStatementCreator` to get generated keys.
* Connection pooling (Hikari) for production performance.

---

## 12) Quick checklist for setup

1. Add `spring-jdbc` and JDBC driver dependencies.
2. Configure `DataSource` bean (prefer Hikari for prod).
3. Configure `JdbcTemplate` bean.
4. Create repository classes annotated with `@Repository` (or `@Component`).
5. Use `RowMapper` / `BeanPropertyRowMapper` to map result sets.
6. Use `@Transactional` in services when multiple DB ops must be atomic.
7. Prefer `NamedParameterJdbcTemplate` for readability when many params.

---
