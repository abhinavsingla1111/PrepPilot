# Java — 100 Interview MCQs

> Format: Each question has a difficulty tag `[Easy] / [Medium] / [Hard]`, options (or True/False, or one-word), the correct answer, and a one-line justification. Assumes modern Java (8–21) unless stated.

---

## Section A — Easy (Q1–Q35)

**Q1. [Easy]** Which method is the entry point of a Java application?

- A) `start()`
- B) `public static void main(String[] args)`
- C) `run()`
- D) `init()`

**Answer:** B — The JVM invokes `public static void main(String[])` to start execution.

**Q2. [Easy]** Java source files compile to:

- A) Machine code
- B) Bytecode (.class)
- C) Assembly
- D) HTML

**Answer:** B — `javac` produces platform-independent bytecode run by the JVM.

**Q3. [Easy] (One word)** The component that executes Java bytecode is the **\_\_**.

**Answer:** JVM — The Java Virtual Machine executes bytecode.

**Q4. [Easy]** Which is NOT a primitive type in Java?

- A) int
- B) boolean
- C) String
- D) char

**Answer:** C — `String` is a class (reference type), not a primitive.

**Q5. [Easy]** The default value of an uninitialized `int` instance field is:

- A) null
- B) 0
- C) undefined
- D) -1

**Answer:** B — Numeric instance fields default to 0.

**Q6. [Easy] (True/False)** Java supports multiple inheritance of classes.

**Answer:** False — Java allows single class inheritance but multiple interfaces.

**Q7. [Easy]** Which keyword is used to inherit a class?

- A) implements
- B) extends
- C) inherits
- D) uses

**Answer:** B — `extends` is used for class inheritance.

**Q8. [Easy]** Which keyword implements an interface?

- A) extends
- B) implements
- C) override
- D) instanceof

**Answer:** B — Classes use `implements` to realize interfaces.

**Q9. [Easy]** What is the size of an `int` in Java?

- A) 16 bits
- B) 32 bits
- C) 64 bits
- D) Platform-dependent

**Answer:** B — Java `int` is always 32 bits.

**Q10. [Easy] (True/False)** Strings in Java are immutable.

**Answer:** True — Once created, a `String` object's content cannot change.

**Q11. [Easy]** Which class is preferred for mutable string building in single-threaded code?

- A) String
- B) StringBuffer
- C) StringBuilder
- D) CharSequence

**Answer:** C — `StringBuilder` is mutable and unsynchronized (faster).

**Q12. [Easy]** `==` on two String objects compares:

- A) Content
- B) References (identity)
- C) Length
- D) Hash codes

**Answer:** B — `==` compares references; use `.equals()` for content.

**Q13. [Easy] (One word)** The keyword to prevent method overriding is **\_\_**.

**Answer:** final — A `final` method cannot be overridden.

**Q14. [Easy]** Which package is imported by default?

- A) java.util
- B) java.io
- C) java.lang
- D) java.net

**Answer:** C — `java.lang` is implicitly imported.

**Q15. [Easy]** What does JVM stand for?

- A) Java Verified Machine
- B) Java Virtual Machine
- C) Java Variable Method
- D) Just Virtual Memory

**Answer:** B — Java Virtual Machine.

**Q16. [Easy] (True/False)** `main` must be `static`.

**Answer:** True — The JVM calls `main` without creating an instance.

**Q17. [Easy]** Which collection does NOT allow duplicate elements?

- A) List
- B) Set
- C) ArrayList
- D) LinkedList

**Answer:** B — A `Set` stores unique elements only.

**Q18. [Easy]** Which is a checked exception?

- A) NullPointerException
- B) IOException
- C) ArrayIndexOutOfBoundsException
- D) ArithmeticException

**Answer:** B — `IOException` is checked and must be handled/declared.

**Q19. [Easy]** The `finally` block is used to:

- A) Catch exceptions
- B) Always execute cleanup code
- C) Throw exceptions
- D) Skip code

**Answer:** B — `finally` runs whether or not an exception occurs.

**Q20. [Easy] (One word)** The process of converting an object to a byte stream is called **\_\_**.

**Answer:** Serialization — Converting object state to bytes.

**Q21. [Easy]** Which operator checks an object's type?

- A) typeof
- B) instanceof
- C) is
- D) cast

**Answer:** B — `instanceof` tests runtime type compatibility.

**Q22. [Easy] (True/False)** An interface can contain `static` methods since Java 8.

**Answer:** True — Java 8 added static and default methods to interfaces.

**Q23. [Easy]** What is autoboxing?

- A) Converting object to primitive
- B) Automatic conversion of primitive to wrapper object
- C) Casting between numbers
- D) Garbage collection

**Answer:** B — E.g., `int` to `Integer` automatically.

**Q24. [Easy]** Which keyword refers to the parent class?

- A) this
- B) super
- C) base
- D) parent

**Answer:** B — `super` accesses superclass members/constructors.

**Q25. [Easy] (True/False)** Java has pointers accessible to the programmer.

**Answer:** False — Java uses references and hides raw pointers.

**Q26. [Easy]** Which access modifier is the most restrictive?

- A) public
- B) protected
- C) default (package)
- D) private

**Answer:** D — `private` limits access to the declaring class.

**Q27. [Easy]** Which loop is guaranteed to execute at least once?

- A) for
- B) while
- C) do-while
- D) enhanced for

**Answer:** C — `do-while` checks the condition after the body.

**Q28. [Easy] (One word)** The keyword used to define a constant is often combined with `static` and **\_\_**.

**Answer:** final — `static final` denotes a constant.

**Q29. [Easy]** The default value of a `boolean` field is:

- A) true
- B) false
- C) 0
- D) null

**Answer:** B — Boolean fields default to `false`.

**Q30. [Easy]** Which is used to handle exceptions?

- A) try-catch
- B) if-else
- C) switch
- D) loop

**Answer:** A — `try-catch` handles exceptions.

**Q31. [Easy] (True/False)** `ArrayList` provides O(1) average-time random access by index.

**Answer:** True — Backed by an array, index access is constant time.

**Q32. [Easy]** Which interface must a class implement to be used in a for-each loop?

- A) Comparable
- B) Iterable
- C) Serializable
- D) Cloneable

**Answer:** B — `Iterable` provides an iterator for enhanced-for.

**Q33. [Easy]** What does `null` represent?

- A) Zero
- B) An empty string
- C) Absence of a reference/object
- D) A boolean

**Answer:** C — `null` means the reference points to no object.

**Q34. [Easy] (One word)** The tool used to compile Java source is **\_\_**.

**Answer:** javac — The Java compiler.

**Q35. [Easy]** Which keyword creates a new object?

- A) create
- B) new
- C) alloc
- D) make

**Answer:** B — `new` allocates and initializes an object.

---

## Section B — Medium (Q36–Q75)

**Q36. [Medium]** Output?

```java
Integer a = 127, b = 127;
Integer c = 128, d = 128;
System.out.println((a==b) + " " + (c==d));
```

- A) true true
- B) true false
- C) false false
- D) false true

**Answer:** B — Integer cache holds -128..127, so `a==b` is true but `c==d` compares distinct objects.

**Q37. [Medium]** Which collection maintains insertion order and allows duplicates?

- A) HashSet
- B) TreeSet
- C) ArrayList
- D) HashMap

**Answer:** C — `ArrayList` preserves insertion order and allows duplicates.

**Q38. [Medium]** `HashMap` allows how many null keys?

- A) 0
- B) 1
- C) Many
- D) 2

**Answer:** B — At most one null key is permitted.

**Q39. [Medium] (True/False)** `HashMap` is thread-safe.

**Answer:** False — Use `ConcurrentHashMap` or synchronization for thread safety.

**Q40. [Medium]** Which guarantees sorted key order?

- A) HashMap
- B) LinkedHashMap
- C) TreeMap
- D) Hashtable

**Answer:** C — `TreeMap` keeps keys sorted (natural/comparator order).

**Q41. [Medium]** The `transient` keyword indicates a field:

- A) Is constant
- B) Should not be serialized
- C) Is thread-local
- D) Is volatile

**Answer:** B — `transient` fields are skipped during serialization.

**Q42. [Medium]** The `volatile` keyword ensures:

- A) Atomic compound operations
- B) Visibility of writes across threads
- C) Locking
- D) Immutability

**Answer:** B — It guarantees visibility (and ordering), not atomicity of compound ops.

**Q43. [Medium]** Which creates a thread correctly?

- A) Extend `Thread` or implement `Runnable`
- B) Extend `Executor`
- C) Implement `Callable` only
- D) Extend `Object`

**Answer:** A — Both extending `Thread` and implementing `Runnable` work.

**Q44. [Medium]** `Callable` differs from `Runnable` because it:

- A) Cannot be submitted to executors
- B) Returns a result and can throw checked exceptions
- C) Is not generic
- D) Runs synchronously

**Answer:** B — `Callable<V>` returns a value and may throw checked exceptions.

**Q45. [Medium] (True/False)** A `final` variable can be assigned exactly once.

**Answer:** True — After its single assignment it cannot be reassigned.

**Q46. [Medium]** Output?

```java
String s = "a";
s.concat("b");
System.out.println(s);
```

- A) ab
- B) a
- C) b
- D) Compile error

**Answer:** B — Strings are immutable; `concat` returns a new string that is discarded.

**Q47. [Medium]** Which is TRUE about `equals()` and `hashCode()`?

- A) Overriding equals requires overriding hashCode for correct hashing
- B) They are unrelated
- C) hashCode must be unique
- D) equals must use ==

**Answer:** A — The contract requires consistent `hashCode` when `equals` is overridden.

**Q48. [Medium]** A checked exception must be:

- A) Ignored
- B) Caught or declared with `throws`
- C) Only logged
- D) Converted to error

**Answer:** B — The compiler enforces handling or declaration.

**Q49. [Medium]** Which is an unchecked exception's superclass?

- A) Exception
- B) RuntimeException
- C) Throwable only
- D) IOException

**Answer:** B — Unchecked exceptions extend `RuntimeException`.

**Q50. [Medium] (One word)** The functional interface with a single abstract method used by lambdas is annotated with @**\_\_**.

**Answer:** FunctionalInterface — Marks a single-abstract-method interface.

**Q51. [Medium]** A lambda `(x) -> x * 2` can target which functional interface?

- A) Runnable
- B) Function<Integer,Integer> / UnaryOperator<Integer>
- C) Comparator only
- D) Callable

**Answer:** B — It matches a one-arg function returning a value.

**Q52. [Medium]** `Stream.filter` is:

- A) A terminal operation
- B) An intermediate (lazy) operation
- C) A collector
- D) A reduction

**Answer:** B — `filter` is intermediate and lazily evaluated.

**Q53. [Medium]** Which is a terminal stream operation?

- A) map
- B) filter
- C) collect
- D) peek

**Answer:** C — `collect` triggers stream evaluation and produces a result.

**Q54. [Medium] (True/False)** Streams can be reused after a terminal operation.

**Answer:** False — A stream is consumed and cannot be reused.

**Q55. [Medium]** `Optional` is designed to:

- A) Replace all nulls at runtime
- B) Represent presence/absence of a value explicitly
- C) Improve performance
- D) Handle threads

**Answer:** B — It models optional values to reduce NPEs.

**Q56. [Medium]** The `default` method in an interface:

- A) Must be abstract
- B) Provides a concrete implementation
- C) Is static
- D) Cannot be overridden

**Answer:** B — Default methods have a body and can be overridden.

**Q57. [Medium]** Which fixes the interface "diamond" for conflicting defaults?

- A) It compiles fine automatically
- B) The class must override and can call `Interface.super.method()`
- C) Java forbids default methods
- D) Use reflection

**Answer:** B — The implementing class resolves the conflict explicitly.

**Q58. [Medium]** Output?

```java
int[] a = {1,2,3};
System.out.println(a.length);
System.out.println("abc".length());
```

- A) 3 and 3
- B) 3 and 4
- C) length() and length
- D) Compile error

**Answer:** A — Arrays use `length` (field); Strings use `length()` (method); both are 3.

**Q59. [Medium] (True/False)** Java passes object references by value.

**Answer:** True — The reference value is copied; the object is shared.

**Q60. [Medium]** `try-with-resources` requires the resource to implement:

- A) Closeable/AutoCloseable
- B) Serializable
- C) Runnable
- D) Comparable

**Answer:** A — `AutoCloseable` enables automatic resource closing.

**Q61. [Medium]** Which garbage collector region holds long-lived objects (HotSpot)?

- A) Eden
- B) Survivor
- C) Old/Tenured generation
- D) Metaspace

**Answer:** C — Long-lived objects are promoted to the old generation.

**Q62. [Medium]** `String` interning refers to:

- A) Copying strings
- B) Storing unique string literals in a pool
- C) Encrypting strings
- D) Splitting strings

**Answer:** B — Interned strings share a single pooled instance.

**Q63. [Medium] (One word)** The concurrency utility for a thread-safe counter with atomic operations is **\_\_**Integer.

**Answer:** Atomic — `AtomicInteger` provides lock-free atomic operations.

**Q64. [Medium]** Which map is best for high-concurrency reads/writes?

- A) HashMap
- B) TreeMap
- C) ConcurrentHashMap
- D) LinkedHashMap

**Answer:** C — `ConcurrentHashMap` supports concurrent access with fine-grained locking.

**Q65. [Medium]** `Comparable` vs `Comparator`: which allows multiple sort orders externally?

- A) Comparable
- B) Comparator
- C) Both identical
- D) Neither

**Answer:** B — `Comparator` defines external, interchangeable orderings.

**Q66. [Medium] (True/False)** Overloaded methods must differ in parameter list.

**Answer:** True — Overloading distinguishes methods by parameter types/count.

**Q67. [Medium]** What does `System.arraycopy` provide?

- A) Deep clone of objects
- B) Fast native array copy
- C) Sorting
- D) Serialization

**Answer:** B — It performs an efficient native shallow copy.

**Q68. [Medium]** Which statement about `finalize()` is correct?

- A) Guaranteed to run promptly
- B) Deprecated and unreliable for cleanup
- C) Called by every method
- D) Replaces try-with-resources

**Answer:** B — `finalize()` is deprecated; prefer `AutoCloseable`/`Cleaner`.

**Q69. [Medium]** Output?

```java
System.out.println(0.1 + 0.2 == 0.3);
```

- A) true
- B) false
- C) Compile error
- D) Runtime error

**Answer:** B — Floating-point rounding makes the sum not exactly 0.3.

**Q70. [Medium] (One word)** A `record` (Java 16+) automatically generates equals, hashCode, and **\_\_**.

**Answer:** toString — Records auto-generate accessors, equals, hashCode, toString.

**Q71. [Medium]** Which keyword allows sealing a class hierarchy (Java 17)?

- A) final
- B) sealed
- C) closed
- D) private

**Answer:** B — `sealed` restricts which classes may extend/implement.

**Q72. [Medium]** The diamond operator `<>`:

- A) Creates lambdas
- B) Infers generic type arguments
- C) Declares arrays
- D) Casts types

**Answer:** B — It lets the compiler infer generic parameters.

**Q73. [Medium] (True/False)** `switch` expressions (Java 14+) can return a value.

**Answer:** True — Arrow `switch` expressions yield values via `yield`/arrow.

**Q74. [Medium]** Which stream method removes duplicates?

- A) filter
- B) distinct
- C) map
- D) limit

**Answer:** B — `distinct()` removes duplicate elements using `equals`.

**Q75. [Medium]** `ExecutorService.submit()` returns:

- A) void
- B) A Future
- C) A Thread
- D) A Runnable

**Answer:** B — It returns a `Future` for the task's result.

---

## Section C — Hard (Q76–Q100)

**Q76. [Hard]** What causes the classic "double-checked locking" bug pre-Java 5?

- A) Too many locks
- B) Instruction reordering without `volatile` on the instance
- C) Missing synchronized
- D) Static import

**Answer:** B — Without `volatile`, a partially constructed object could be visible; `volatile` fixes it since Java 5.

**Q77. [Hard]** Type erasure means:

```java
List<String> a = new ArrayList<>();
List<Integer> b = new ArrayList<>();
System.out.println(a.getClass() == b.getClass());
```

- A) false
- B) true
- C) Compile error
- D) Runtime error

**Answer:** B — Generic types are erased; both share the raw `ArrayList` class.

**Q78. [Hard]** Which is TRUE about `ConcurrentModificationException`?

- A) Always thrown in multithreading only
- B) Can occur when modifying a collection during iteration (fail-fast)
- C) Only for arrays
- D) Guaranteed detection

**Answer:** B — Fail-fast iterators detect structural modification best-effort.

**Q79. [Hard] (True/False)** `HashMap` in Java 8+ converts a bucket to a balanced tree when it exceeds a threshold.

**Answer:** True — Buckets with ≥8 entries (and capacity ≥64) treeify to O(log n).

**Q80. [Hard]** The `happens-before` relationship in the JMM guarantees:

- A) Thread priority
- B) Visibility and ordering of memory operations
- C) Deadlock freedom
- D) GC timing

**Answer:** B — It defines when one thread's writes are visible to another.

**Q81. [Hard]** Output?

```java
List<Integer> list = new ArrayList<>(List.of(1,2,3));
list.removeIf(x -> x == 2);
System.out.println(list);
```

- A) [1, 3]
- B) [1, 2, 3]
- C) ConcurrentModificationException
- D) [2]

**Answer:** A — `removeIf` safely removes matching elements → `[1, 3]`.

**Q82. [Hard]** Which is a valid reason `String` is immutable?

- A) To allow subclassing
- B) Security, caching (interning), and thread-safety
- C) To reduce heap usage always
- D) To support pointers

**Answer:** B — Immutability enables safe sharing, caching, and hashing.

**Q83. [Hard] (One word)** The default method conflict where two interfaces provide the same default forces the implementer to **\_\_** the method.

**Answer:** Override — The class must override to resolve ambiguity.

**Q84. [Hard]** `CompletableFuture.thenApply` vs `thenCompose`: `thenCompose` is used when the function returns:

- A) A plain value
- B) Another CompletionStage/CompletableFuture (flattening)
- C) void
- D) A Thread

**Answer:** B — `thenCompose` flattens nested futures (like flatMap).

**Q85. [Hard]** Which statement about `finally` and `return` is correct?

```java
int f(){ try { return 1; } finally { return 2; } }
```

- A) Returns 1
- B) Returns 2
- C) Compile error
- D) Throws

**Answer:** B — A `return` in `finally` overrides the try's return value.

**Q86. [Hard] (True/False)** A generic method can declare its own type parameter independent of the class.

**Answer:** True — E.g., `<T> T identity(T x)` introduces a method-level type variable.

**Q87. [Hard]** PECS ("Producer Extends, Consumer Super") guides usage of:

- A) Threads
- B) Bounded wildcards in generics
- C) Streams
- D) Annotations

**Answer:** B — It dictates `? extends`/`? super` for producers/consumers.

**Q88. [Hard]** Why can't you create `new T[10]` directly with a generic `T`?

- A) Syntax error only
- B) Arrays are reified but generics are erased (type-unsafe)
- C) T is final
- D) JVM limitation on heap

**Answer:** B — Reifiable arrays clash with erased generics, so it's disallowed.

**Q89. [Hard]** `ThreadLocal` provides:

- A) Shared global state
- B) Per-thread isolated variable copies
- C) Locking
- D) Atomic operations

**Answer:** B — Each thread has its own independent value.

**Q90. [Hard] (True/False)** Escaping `this` from a constructor can expose a partially constructed object to other threads.

**Answer:** True — Publishing `this` during construction is unsafe.

**Q91. [Hard]** Which GC is designed for low pause times on large heaps (Java 11+)?

- A) Serial GC
- B) ZGC / Shenandoah
- C) Parallel GC
- D) Reference counting

**Answer:** B — ZGC and Shenandoah target very low, scalable pause times.

**Q92. [Hard]** Output?

```java
System.out.println(Integer.MAX_VALUE + 1);
```

- A) 2147483648
- B) -2147483648
- C) Overflow error
- D) 0

**Answer:** B — Integer overflow wraps around to `Integer.MIN_VALUE`.

**Q93. [Hard]** A `WeakReference` allows the referent to be:

- A) Never collected
- B) Garbage-collected when only weakly reachable
- C) Pinned in memory
- D) Serialized

**Answer:** B — Weakly reachable objects can be reclaimed by GC.

**Q94. [Hard] (One word)** The pattern used by `Collections.unmodifiableList` to wrap and restrict is the **\_\_** pattern.

**Answer:** Decorator — It wraps a list to add read-only behavior.

**Q95. [Hard]** Which best explains `synchronized` on an instance method?

- A) Locks the class object
- B) Locks on `this` (the instance monitor)
- C) No locking
- D) Locks all threads globally

**Answer:** B — It acquires the intrinsic lock of the current instance.

**Q96. [Hard]** `static synchronized` methods lock on:

- A) The instance
- B) The Class object
- C) A new lock each call
- D) Nothing

**Answer:** B — Class-level lock is the `Class` object's monitor.

**Q97. [Hard] (True/False)** Two threads can deadlock by acquiring two locks in opposite orders.

**Answer:** True — Circular wait on lock ordering causes deadlock.

**Q98. [Hard]** `varargs` (`String... args`) is compiled to:

- A) A List
- B) An array parameter
- C) A Set
- D) A Stream

**Answer:** B — Varargs are syntactic sugar over an array.

**Q99. [Hard]** Which statement about `Stream.parallel()` is TRUE?

- A) Always faster
- B) Uses the common ForkJoinPool and may harm performance for small/ordered tasks
- C) Guarantees ordering improvements
- D) Is single-threaded

**Answer:** B — Parallel streams share the common pool and aren't always beneficial.

**Q100. [Hard]** Output?

```java
Object o = "hello";
if (o instanceof String s) System.out.println(s.length());
```

- A) Compile error
- B) 5
- C) hello
- D) 0

**Answer:** B — Pattern matching for `instanceof` binds `s`, and `"hello".length()` is 5.
