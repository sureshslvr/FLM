
# ✔ Unit Testing vs Integration Testing

| Feature         | Unit Testing                                     | Integration Testing                                          |
| --------------- | ------------------------------------------------ | ------------------------------------------------------------ |
| Purpose         | Test **individual components** (methods/classes) | Test **complete workflow** (interactions between components) |
| Tools           | JUnit 5 + Mockito                                | JUnit 5 + Spring Test + MockMvc                              |
| Scope           | Small & isolated                                 | Combined modules (DB, controllers, services)                 |
| Speed           | Fast                                             | Slow                                                         |
| External System | Mocked                                           | Real/Embedded DB                                             |
| Complexity      | Simple                                           | Complex                                                      |

---

## 1️⃣ Unit Testing

### Goal:

➡ Test only one component (e.g., **Service layer**) without connecting to database/external services.

---

### Important Annotations (JUnit & Mockito)

| Annotation                            | Meaning                                        |
| ------------------------------------- | ---------------------------------------------- |
| `@Test`                               | Marks method as test case                      |
| `@ExtendWith(MockitoExtension.class)` | Enables Mockito features                       |
| `@Mock`                               | Create mock for dependency                     |
| `@InjectMocks`                        | Inject mocks into the tested class             |
| `@BeforeEach`                         | Runs before every test method                  |
| `@AfterEach`                          | Runs after every test                          |
| `@BeforeAll`                          | Runs once before test class execution (static) |
| `@AfterAll`                           | Runs once after test class execution (static)  |
| `@Disabled`                           | Skip/ignore a test                             |

---

### Example: Unit Test for Service Layer

#### RestaurantService.java (SUT - System Under Test)

```java
@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public Restaurant getRestaurantById(Long id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
    }
}
```

#### RestaurantServiceTest.java (Unit Test)

```java
@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    private Restaurant restaurant;

    @BeforeEach
    void setup() {
        restaurant = new Restaurant(1L, "BBQ Nation");
    }

    @Test
    void testGetRestaurantById() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant));

        Restaurant result = restaurantService.getRestaurantById(1L);

        assertEquals("BBQ Nation", result.getName());
        verify(restaurantRepository, times(1)).findById(1L);
    }
}
```

✔ Repository calls mocked → no DB involved

---

## 2️⃣ Integration Testing

### Goal:

➡ Test **complete flow** with real DB, controller, service, repository interactions.

Uses:

* `@SpringBootTest` (full app context)
* `@AutoConfigureMockMvc`

---

### Important Annotations

| Annotation              | Meaning                                        |
| ----------------------- | ---------------------------------------------- |
| `@SpringBootTest`       | Loads full Spring container                    |
| `@AutoConfigureMockMvc` | Allows REST API testing without server startup |
| `@Sql`                  | Load test data before running tests            |
| `@Transactional`        | Rollback DB changes after each test            |

---

### Example: Integration Test for REST Controller

#### RestaurantController.java

```java
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantService.getRestaurantById(id));
    }
}
```

#### RestaurantControllerIntegrationTest.java

```java
@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetRestaurantAPI() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/restaurant/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("BBQ Nation"));
    }
}
```

✔ Full application flow tested
✔ Uses **real bean** + **mock HTTP call**

---

## Key Assertions (JUnit)

| Method                         | Usage                      |
| ------------------------------ | -------------------------- |
| `assertEquals()`               | Compare expected vs actual |
| `assertTrue() / assertFalse()` | Boolean testing            |
| `assertThrows()`               | Exception testing          |
| `assertNotNull()`              | Object should not be null  |

---

## Mockito Useful Methods

| Method                   | Usage                          |
| ------------------------ | ------------------------------ |
| `when(x).thenReturn()`   | Mock return data               |
| `verify(mock, times(n))` | Ensure method invocation count |
| `any(), eq()`            | Parameter matchers             |
| `doThrow(), doNothing()` | Void methods mocking           |

---

## Which Testing When?

| When to use?                    | Type             |
| ------------------------------- | ---------------- |
| Testing logic inside a service  | Unit Test        |
| Testing exception behavior      | Unit Test        |
| Testing API → service → DB flow | Integration Test |
| Testing configuration & wiring  | Integration Test |

---

## Final Summary

| Feature  | Unit Test         | Integration Test    |
| -------- | ----------------- | ------------------- |
| How?     | Mock Dependencies | Use real beans/DB   |
| Tools    | JUnit + Mockito   | JUnit + MockMvc     |
| Speed    | ⚡ Fast            | 🐢 Slow             |
| Covers   | Single class      | Multiple layers     |
| Good For | Service logic     | End-to-End behavior |

---