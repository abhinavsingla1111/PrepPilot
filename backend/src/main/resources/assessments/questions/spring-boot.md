# Spring Boot — 100 Interview MCQs

> Format: Each question has a difficulty tag `[Easy] / [Medium] / [Hard]`, options (or True/False, or one-word), the correct answer, and a one-line justification.

---

## Section A — Easy (Q1–Q35)

**Q1. [Easy]** Spring Boot's main advantage over classic Spring is:

- A) It replaces the JVM
- B) Auto-configuration and convention over configuration
- C) It removes dependency injection
- D) It only works with XML

**Answer:** B — Spring Boot auto-configures beans and reduces boilerplate.

**Q2. [Easy]** Which annotation marks the main Spring Boot application class?

- A) @Configuration
- B) @SpringBootApplication
- C) @Component
- D) @Service

**Answer:** B — `@SpringBootApplication` bootstraps the application.

**Q3. [Easy] (One word)** `@SpringBootApplication` is a combination of `@Configuration`, `@ComponentScan`, and `@______Configuration`.

**Answer:** EnableAuto — It includes `@EnableAutoConfiguration`.

**Q4. [Easy]** Which starter is used to build web applications with REST endpoints?

- A) spring-boot-starter-data-jpa
- B) spring-boot-starter-web
- C) spring-boot-starter-security
- D) spring-boot-starter-test

**Answer:** B — `spring-boot-starter-web` provides Spring MVC + embedded server.

**Q5. [Easy] (True/False)** Spring Boot includes an embedded server (e.g., Tomcat) by default for web apps.

**Answer:** True — It ships with an embedded servlet container.

**Q6. [Easy]** Which annotation declares a REST controller?

- A) @Controller
- B) @RestController
- C) @Service
- D) @Repository

**Answer:** B — `@RestController` = `@Controller` + `@ResponseBody`.

**Q7. [Easy]** Which annotation injects a dependency?

- A) @Inject/@Autowired
- B) @Bean
- C) @Entity
- D) @Value

**Answer:** A — `@Autowired` (or `@Inject`) performs dependency injection.

**Q8. [Easy]** The default configuration file for Spring Boot is:

- A) config.xml
- B) application.properties / application.yml
- C) settings.json
- D) boot.cfg

**Answer:** B — Properties/YAML files hold externalized configuration.

**Q9. [Easy] (One word)** The default embedded server in Spring Boot web starter is **\_\_**.

**Answer:** Tomcat — Apache Tomcat is the default.

**Q10. [Easy]** Which annotation maps HTTP GET requests?

- A) @PostMapping
- B) @GetMapping
- C) @RequestMapping(method=POST)
- D) @PutMapping

**Answer:** B — `@GetMapping` handles GET requests.

**Q11. [Easy]** `@Component`, `@Service`, `@Repository` are all:

- A) Unrelated
- B) Stereotype annotations for beans
- C) For testing only
- D) Deprecated

**Answer:** B — They are specializations of `@Component` for component scanning.

**Q12. [Easy] (True/False)** Spring beans are singleton-scoped by default.

**Answer:** True — The default scope is singleton (one instance per container).

**Q13. [Easy]** Which annotation reads a property value into a field?

- A) @Bean
- B) @Value
- C) @Entity
- D) @Scope

**Answer:** B — `@Value("${...}")` injects property values.

**Q14. [Easy]** Which build tools are commonly used with Spring Boot?

- A) Maven and Gradle
- B) Make and CMake
- C) npm and yarn
- D) Ant only

**Answer:** A — Maven and Gradle are the standard build tools.

**Q15. [Easy] (One word)** The starter that provides JUnit, Mockito, and testing utilities is spring-boot-starter-**\_\_**.

**Answer:** test — `spring-boot-starter-test` bundles testing libraries.

**Q16. [Easy]** Which annotation marks a JPA entity?

- A) @Table
- B) @Entity
- C) @Repository
- D) @Bean

**Answer:** B — `@Entity` maps a class to a database table.

**Q17. [Easy]** `@RequestMapping("/api")` at class level defines:

- A) A bean scope
- B) A base URL path for all handler methods
- C) A database table
- D) A security rule

**Answer:** B — It sets a common path prefix.

**Q18. [Easy] (True/False)** Spring Boot Actuator provides production-ready endpoints like `/health`.

**Answer:** True — Actuator exposes health, metrics, and monitoring endpoints.

**Q19. [Easy]** How do you extract a path variable?

- A) @RequestParam
- B) @PathVariable
- C) @RequestBody
- D) @Header

**Answer:** B — `@PathVariable` binds URI template variables.

**Q20. [Easy]** How do you read a query parameter `?id=5`?

- A) @PathVariable
- B) @RequestParam
- C) @RequestBody
- D) @Value

**Answer:** B — `@RequestParam` binds query parameters.

**Q21. [Easy] (One word)** The annotation to bind a JSON request body to an object is @Request**\_\_**.

**Answer:** Body — `@RequestBody` deserializes the request payload.

**Q22. [Easy]** Which port does Spring Boot use by default?

- A) 80
- B) 8080
- C) 3000
- D) 5000

**Answer:** B — The default embedded server port is 8080.

**Q23. [Easy]** Which property changes the server port?

- A) server.address
- B) server.port
- C) app.port
- D) spring.port

**Answer:** B — `server.port` sets the listening port.

**Q24. [Easy] (True/False)** `@Autowired` can be used on constructors, fields, and setters.

**Answer:** True — Spring supports constructor, field, and setter injection.

**Q25. [Easy]** Which is the recommended injection type?

- A) Field injection
- B) Constructor injection
- C) Static injection
- D) Reflection injection

**Answer:** B — Constructor injection is preferred for immutability and testability.

**Q26. [Easy]** `@Configuration` classes are used to:

- A) Define entities
- B) Declare beans via @Bean methods
- C) Handle HTTP
- D) Configure logging only

**Answer:** B — They define Java-based bean configuration.

**Q27. [Easy] (One word)** The interface Spring Data JPA repositories commonly extend is **\_\_**Repository.

**Answer:** Jpa — `JpaRepository` provides CRUD + JPA features (also `CrudRepository`).

**Q28. [Easy]** Which annotation handles exceptions in a controller?

- A) @ExceptionHandler
- B) @Catch
- C) @Error
- D) @Try

**Answer:** A — `@ExceptionHandler` maps exceptions to responses.

**Q29. [Easy]** `@ControllerAdvice` is used for:

- A) Global exception handling / cross-cutting controller logic
- B) Database access
- C) Security only
- D) Caching

**Answer:** A — It centralizes handling across controllers.

**Q30. [Easy] (True/False)** Spring Boot supports YAML configuration.

**Answer:** True — `application.yml` is fully supported.

**Q31. [Easy]** Which annotation enables scheduling of tasks?

- A) @Async
- B) @EnableScheduling
- C) @Scheduled
- D) @Timer

**Answer:** B — `@EnableScheduling` activates scheduled task support.

**Q32. [Easy]** Which annotation runs a method on a schedule?

- A) @EnableScheduling
- B) @Scheduled
- C) @Async
- D) @Cron

**Answer:** B — `@Scheduled(cron=...)` schedules method execution.

**Q33. [Easy] (One word)** The default HTTP status for a successful GET is **\_\_** (number).

**Answer:** 200 — HTTP 200 OK.

**Q34. [Easy]** Which returns a custom HTTP status and body flexibly?

- A) ResponseEntity
- B) String
- C) void
- D) ModelAndView

**Answer:** A — `ResponseEntity` controls status, headers, and body.

**Q35. [Easy]** Spring Boot DevTools primarily provides:

- A) Production monitoring
- B) Automatic restart and live reload during development
- C) Database migration
- D) Security scanning

**Answer:** B — DevTools speeds development with auto-restart.

---

## Section B — Medium (Q36–Q75)

**Q36. [Medium]** How does Spring Boot decide which auto-configurations to apply?

- A) Randomly
- B) `@Conditional` annotations based on classpath and beans
- C) Only via XML
- D) By file names

**Answer:** B — Conditional annotations (e.g., `@ConditionalOnClass`) drive auto-config.

**Q37. [Medium] (One word)** Auto-configuration classes are listed in `META-INF/spring/...AutoConfiguration.______` (Boot 2.7+/3).

**Answer:** imports — Boot 3 uses `AutoConfiguration.imports` (previously `spring.factories`).

**Q38. [Medium]** Which bean scope creates a new instance per request in a web app?

- A) singleton
- B) prototype
- C) request
- D) session

**Answer:** C — `request` scope yields one bean per HTTP request.

**Q39. [Medium]** `prototype` scope means:

- A) One shared instance
- B) A new instance every time the bean is requested
- C) One per session
- D) One per thread

**Answer:** B — Each injection/lookup produces a new bean.

**Q40. [Medium] (True/False)** A prototype bean injected into a singleton is re-created on every method call automatically.

**Answer:** False — It's injected once; you need lookup/`ObjectProvider` for fresh instances.

**Q41. [Medium]** `@Transactional` by default rolls back on:

- A) All exceptions
- B) Only unchecked (RuntimeException) and Error
- C) Only checked exceptions
- D) Never

**Answer:** B — By default it rolls back on runtime exceptions/errors, not checked.

**Q42. [Medium]** Which propagation starts a new transaction, suspending any existing one?

- A) REQUIRED
- B) REQUIRES_NEW
- C) SUPPORTS
- D) NEVER

**Answer:** B — `REQUIRES_NEW` suspends the current and starts a new transaction.

**Q43. [Medium]** `@Qualifier` is used to:

- A) Rename beans
- B) Disambiguate among multiple candidate beans
- C) Set scope
- D) Enable caching

**Answer:** B — It selects a specific bean when several match.

**Q44. [Medium]** `@Primary` indicates:

- A) The main class
- B) The default bean to inject when multiple candidates exist
- C) A primary key
- D) A required property

**Answer:** B — It marks the preferred autowiring candidate.

**Q45. [Medium]** Which annotation binds a group of properties to a POJO?

- A) @Value
- B) @ConfigurationProperties
- C) @PropertySource
- D) @Bean

**Answer:** B — `@ConfigurationProperties` maps hierarchical properties to fields.

**Q46. [Medium] (True/False)** `@Profile("dev")` restricts a bean/config to the `dev` profile.

**Answer:** True — Profiles conditionally activate beans/config.

**Q47. [Medium]** Which activates a profile at runtime?

- A) spring.profiles.active
- B) server.profile
- C) app.env
- D) spring.env.active

**Answer:** A — `spring.profiles.active=dev` enables the profile.

**Q48. [Medium]** In Spring Data JPA, the method `findByLastNameOrderByAgeDesc` is:

- A) Invalid
- B) A derived query from the method name
- C) A native SQL only
- D) A stored procedure

**Answer:** B — Spring derives the query from the method name convention.

**Q49. [Medium]** `@Query` in a repository lets you:

- A) Define custom JPQL/native SQL
- B) Configure beans
- C) Handle exceptions
- D) Schedule tasks

**Answer:** A — It declares custom JPQL or native queries.

**Q50. [Medium] (One word)** The default JSON (de)serialization library in Spring Boot is **\_\_**.

**Answer:** Jackson — Jackson handles JSON binding by default.

**Q51. [Medium]** Which annotation enables method-level validation of a `@RequestBody`?

- A) @Validated / @Valid
- B) @Verify
- C) @Check
- D) @Constraint

**Answer:** A — `@Valid`/`@Validated` triggers Bean Validation (JSR-380).

**Q52. [Medium]** A `MethodArgumentNotValidException` is thrown when:

- A) A bean is missing
- B) `@Valid` request body validation fails
- C) The DB is down
- D) A profile is inactive

**Answer:** B — Validation failures on request bodies raise this exception.

**Q53. [Medium]** Which Actuator endpoint exposes application metrics?

- A) /info
- B) /metrics
- C) /beans
- D) /env

**Answer:** B — `/actuator/metrics` shows metrics data.

**Q54. [Medium] (True/False)** By default in Boot 3, most Actuator endpoints are exposed over HTTP.

**Answer:** False — Only `health` (and `info`) are exposed by default; others need `management.endpoints.web.exposure.include`.

**Q55. [Medium]** `@SpringBootTest` loads:

- A) No context
- B) A slice only
- C) The full application context
- D) Only the web layer

**Answer:** C — It bootstraps the complete context for integration tests.

**Q56. [Medium]** `@WebMvcTest` is a test slice for:

- A) The persistence layer
- B) The web/controller layer only
- C) Full context
- D) Security only

**Answer:** B — It loads MVC components without the full context.

**Q57. [Medium]** `@DataJpaTest` is a slice for:

- A) Controllers
- B) JPA repositories with an embedded DB
- C) Security
- D) Caching

**Answer:** B — It configures an in-memory DB and JPA components.

**Q58. [Medium] (One word)** To replace a bean with a mock in a Spring test you use @**\_\_**Bean (Spring Boot).

**Answer:** Mock — `@MockBean` adds/replaces a bean with a Mockito mock.

**Q59. [Medium]** Which starter provides authentication/authorization?

- A) spring-boot-starter-web
- B) spring-boot-starter-security
- C) spring-boot-starter-actuator
- D) spring-boot-starter-aop

**Answer:** B — `spring-boot-starter-security` adds Spring Security.

**Q60. [Medium]** After adding Spring Security with defaults, endpoints are:

- A) Fully open
- B) Secured with a generated password and HTTP Basic/form login
- C) Disabled
- D) Read-only

**Answer:** B — It secures all endpoints with a default user by default.

**Q61. [Medium]** Which handles cross-origin requests configuration?

- A) @CrossOrigin
- B) @Cors
- C) @Origin
- D) @AllowOrigin

**Answer:** A — `@CrossOrigin` configures CORS at controller/method level.

**Q62. [Medium] (True/False)** `application-dev.yml` is automatically loaded when the `dev` profile is active.

**Answer:** True — Profile-specific config files are merged when active.

**Q63. [Medium]** Which annotation enables caching abstraction?

- A) @Cacheable
- B) @EnableCaching
- C) @CachePut
- D) @CacheConfig

**Answer:** B — `@EnableCaching` activates the caching support.

**Q64. [Medium]** `@Cacheable` on a method:

- A) Always executes the method
- B) Returns cached result if present, else executes and caches
- C) Clears the cache
- D) Disables caching

**Answer:** B — It short-circuits execution using cached values.

**Q65. [Medium]** `@Async` methods require:

- A) @EnableAsync and typically return void or Future/CompletableFuture
- B) @Transactional
- C) A new thread manually
- D) A REST endpoint

**Answer:** A — `@EnableAsync` plus a proxy-friendly return type.

**Q66. [Medium] (One word)** Spring AOP is typically implemented at runtime using dynamic **\_\_** (proxies).

**Answer:** Proxies — Spring uses JDK dynamic or CGLIB proxies.

**Q67. [Medium]** Why might a self-invocation of an `@Transactional` method not start a transaction?

- A) Transactions are disabled
- B) The proxy is bypassed on internal calls
- C) JPA is missing
- D) The DB rejects it

**Answer:** B — Internal calls don't pass through the proxy, so advice isn't applied.

**Q68. [Medium]** Which starter auto-configures a connection pool by default (Boot 2+)?

- A) Uses HikariCP
- B) Uses C3P0
- C) Uses DBCP2
- D) No pooling

**Answer:** A — HikariCP is the default connection pool.

**Q69. [Medium]** `spring.jpa.hibernate.ddl-auto=update` will:

- A) Drop the schema
- B) Update the schema to match entities
- C) Do nothing
- D) Only validate

**Answer:** B — It alters the schema to reflect entity changes (avoid in prod).

**Q70. [Medium] (True/False)** `@RestControllerAdvice` combines `@ControllerAdvice` and `@ResponseBody`.

**Answer:** True — It returns response bodies for global exception handling.

**Q71. [Medium]** Which returns 404 cleanly from a controller?

- A) throw new ResponseStatusException(HttpStatus.NOT_FOUND)
- B) return null
- C) System.exit(404)
- D) throw new Error()

**Answer:** A — `ResponseStatusException` maps to the given status.

**Q72. [Medium]** The N+1 select problem in JPA is mitigated by:

- A) Ignoring it
- B) Fetch joins / entity graphs / batch fetching
- C) Larger heap
- D) More controllers

**Answer:** B — Eager fetch joins or `@EntityGraph` reduce extra queries.

**Q73. [Medium] (One word)** The starter for reactive, non-blocking web is spring-boot-starter-**\_\_**.

**Answer:** webflux — WebFlux provides reactive web support.

**Q74. [Medium]** Spring MVC is **\_\_** while WebFlux is **\_\_**:

- A) reactive / blocking
- B) blocking (servlet) / non-blocking (reactive)
- C) both blocking
- D) both reactive

**Answer:** B — MVC is servlet/blocking; WebFlux is reactive/non-blocking.

**Q75. [Medium]** Which bean lets you run code after startup?

- A) CommandLineRunner / ApplicationRunner
- B) @PostMapping
- C) @Entity
- D) @Bean only

**Answer:** A — `CommandLineRunner`/`ApplicationRunner` run on startup.

---

## Section C — Hard (Q76–Q100)

**Q76. [Hard]** Bean creation order for a circular dependency between two singletons using constructor injection results in:

- A) Both created fine
- B) `BeanCurrentlyInCreationException` (unresolvable via constructors)
- C) A prototype bean
- D) Lazy proxy automatically

**Answer:** B — Constructor-based circular dependencies can't be resolved and fail.

**Q77. [Hard]** How can a circular dependency sometimes be resolved?

- A) Field/setter injection or `@Lazy` on one dependency
- B) Adding more constructors
- C) Using @Primary
- D) Disabling DI

**Answer:** A — Setter/field injection or `@Lazy` breaks the constructor cycle.

**Q78. [Hard] (True/False)** In Spring Boot 3, `@ConfigurationProperties` binding supports relaxed binding (kebab-case to camelCase).

**Answer:** True — Relaxed binding maps `my-prop` to `myProp`.

**Q79. [Hard]** Which condition ensures a bean is created only if a specific bean is missing?

- A) @ConditionalOnBean
- B) @ConditionalOnMissingBean
- C) @ConditionalOnClass
- D) @ConditionalOnProperty

**Answer:** B — `@ConditionalOnMissingBean` lets users override defaults.

**Q80. [Hard]** The default transaction isolation level typically used is:

- A) Explicitly SERIALIZABLE
- B) `DEFAULT` (delegates to the database default)
- C) READ_UNCOMMITTED always
- D) NONE

**Answer:** B — `Isolation.DEFAULT` uses the underlying DB's default.

**Q81. [Hard]** A `LazyInitializationException` occurs when:

- A) Beans are lazy
- B) A lazy JPA association is accessed outside an open persistence context/session
- C) Caching fails
- D) A profile is inactive

**Answer:** B — Accessing lazy fields after the session closes triggers it.

**Q82. [Hard] (One word)** The pattern where the persistence context stays open during view rendering is called Open-Session-In-**\_\_**.

**Answer:** View — OSIV (Open Session In View), enabled by default but often discouraged.

**Q83. [Hard]** Why is `spring.jpa.open-in-view=true` sometimes problematic?

- A) It disables JPA
- B) It holds DB connections during view rendering, hurting scalability
- C) It corrupts data
- D) It disables transactions

**Answer:** B — OSIV can exhaust the connection pool under load.

**Q84. [Hard]** Which best describes `@Transactional(readOnly = true)` benefit?

- A) Prevents all writes forever
- B) Hints the provider to optimize (e.g., skip dirty checking / flush)
- C) Encrypts data
- D) Enables caching

**Answer:** B — It's an optimization hint for read-only operations.

**Q85. [Hard] (True/False)** Two `@Transactional` methods with `REQUIRED` propagation share the same physical transaction when nested via proxies.

**Answer:** True — `REQUIRED` joins the existing transaction if present.

**Q86. [Hard]** In Spring Security, the filter chain order matters because:

- A) Filters run alphabetically
- B) Each filter processes the request/response sequentially (auth before authorization)
- C) Order is irrelevant
- D) Only one filter runs

**Answer:** B — The ordered chain enforces authentication before authorization.

**Q87. [Hard]** Which enables method-level security like `@PreAuthorize`?

- A) @EnableWebSecurity
- B) @EnableMethodSecurity (or @EnableGlobalMethodSecurity pre-6)
- C) @Secured only
- D) @RolesAllowed only

**Answer:** B — Method security must be explicitly enabled.

**Q88. [Hard]** A bean implementing `BeanPostProcessor` can:

- A) Only log
- B) Modify/wrap beans after instantiation (e.g., create proxies)
- C) Replace the JVM
- D) Only run at shutdown

**Answer:** B — It hooks into initialization to customize beans.

**Q89. [Hard] (One word)** The interface for programmatically registering bean definitions before beans are created is BeanFactory**\_\_**Processor.

**Answer:** Post — `BeanFactoryPostProcessor` modifies bean definitions.

**Q90. [Hard]** Why prefer `@ConditionalOnProperty` for feature toggles?

- A) It's faster
- B) It enables/disables beans based on config without code changes
- C) It's required for DI
- D) It disables auto-config

**Answer:** B — It gates bean creation on property values.

**Q91. [Hard]** In WebFlux, blocking calls on the event loop threads cause:

- A) Faster responses
- B) Thread starvation and degraded throughput
- C) Automatic scaling
- D) No effect

**Answer:** B — Blocking reactive threads defeats non-blocking benefits.

**Q92. [Hard] (True/False)** `Mono` represents 0..1 elements and `Flux` represents 0..N in Reactor.

**Answer:** True — Reactor's core publishers model single vs multiple values.

**Q93. [Hard]** Which explains why `@Value` injection into a `static` field doesn't work directly?

- A) Static fields belong to the class, not the managed instance
- B) `@Value` is deprecated
- C) The property is missing
- D) Spring forbids all injection

**Answer:** A — Spring injects into instances; use a setter workaround for statics.

**Q94. [Hard]** The `@Bean` method's return type is used by Spring to:

- A) Determine the bean's type for autowiring
- B) Set the scope
- C) Configure logging
- D) Nothing

**Answer:** A — The declared return type registers the bean type.

**Q95. [Hard] (One word)** To externalize secrets, Spring Boot integrates with Spring Cloud **\_\_** (server for centralized config).

**Answer:** Config — Spring Cloud Config centralizes configuration.

**Q96. [Hard]** Why can proxied `final` methods break Spring AOP with CGLIB?

- A) final methods can't be overridden by the subclass proxy
- B) final is deprecated
- C) CGLIB is not used
- D) It always works

**Answer:** A — CGLIB subclasses the target; `final` methods can't be proxied/advised.

**Q97. [Hard] (True/False)** `@PostConstruct` runs after dependency injection completes.

**Answer:** True — It's an initialization callback after wiring.

**Q98. [Hard]** Graceful shutdown in Spring Boot ensures:

- A) Immediate kill
- B) In-flight requests complete before the server stops
- C) DB is dropped
- D) Cache is cleared

**Answer:** B — `server.shutdown=graceful` drains active requests.

**Q99. [Hard]** Which is TRUE about `@Lazy` on a singleton bean?

- A) It's created eagerly at startup
- B) Its creation is deferred until first use
- C) It becomes prototype
- D) It's never created

**Answer:** B — `@Lazy` delays instantiation until needed.

**Q100. [Hard]** Boot 3's native image support (GraalVM) primarily improves:

- A) Runtime reflection freedom
- B) Startup time and memory footprint via AOT compilation
- C) Number of beans
- D) HTTP protocol versions

**Answer:** B — AOT/native images cut startup time and memory usage.
