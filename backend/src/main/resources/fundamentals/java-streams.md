# 🌊 Java Streams & Lambdas

> Describe *what* you want done with data, and let Java figure out the loop.

## 🧠 What & Why

The **Stream API** (Java 8+) lets you process collections in a declarative, pipeline style — filtering, transforming, and aggregating data without writing manual loops. Paired with **lambdas** (compact anonymous functions), streams make data-processing code short and readable.

Instead of "loop over the list, check a condition, build a new list," you say "filter these, map them, collect the result." This reads almost like a sentence and is a favorite in modern Java interviews. The key mental shift: streams are about *describing a computation*, not stepping through it.

## 🔑 Key Concepts

- **Lambda** — A short anonymous function, e.g. `x -> x * 2` or `(a, b) -> a + b`.
- **Functional interface** — An interface with exactly one abstract method (e.g. `Predicate`, `Function`), which a lambda can implement.
- **Stream** — A pipeline of operations over a data source; it doesn't store data itself.
- **Intermediate op** — Returns another stream and is *lazy* (`filter`, `map`, `sorted`).
- **Terminal op** — Triggers execution and produces a result (`collect`, `reduce`, `forEach`, `count`).
- **Method reference** — Shorthand for a lambda that just calls a method, e.g. `String::toUpperCase`.
- **`Optional`** — A container that may or may not hold a value, avoiding `null`.

## 💻 Example

```java
import java.util.*;
import java.util.stream.*;

public class StreamsDemo {
    public static void main(String[] args) {
        List<String> names = List.of("ann", "bob", "carol", "dave", "eve");

        // Filter -> map -> collect
        List<String> result = names.stream()
            .filter(n -> n.length() <= 3)     // keep short names
            .map(String::toUpperCase)         // method reference
            .sorted()                         // intermediate, lazy
            .collect(Collectors.toList());    // terminal, triggers work
        System.out.println(result);           // [ANN, BOB, EVE]

        // reduce: combine into a single value
        int totalLength = names.stream()
            .mapToInt(String::length)
            .sum();                           // 3+3+5+4+3 = 18
        System.out.println(totalLength);

        // Optional: safely handle "maybe no value"
        Optional<String> first = names.stream()
            .filter(n -> n.startsWith("z"))
            .findFirst();
        System.out.println(first.orElse("none found")); // none found

        // Grouping with collectors
        Map<Integer, List<String>> byLength = names.stream()
            .collect(Collectors.groupingBy(String::length));
        System.out.println(byLength); // {3=[ann, bob, eve], 4=[dave], 5=[carol]}
    }
}
```

## 🧩 Common functional interfaces

| Interface | Abstract method | Meaning | Example lambda |
|---|---|---|---|
| `Predicate<T>` | `boolean test(T)` | A condition | `x -> x > 0` |
| `Function<T,R>` | `R apply(T)` | Transform T into R | `s -> s.length()` |
| `Consumer<T>` | `void accept(T)` | Do something, no return | `System.out::println` |
| `Supplier<T>` | `T get()` | Produce a value | `() -> new Random().nextInt()` |
| `BinaryOperator<T>` | `T apply(T,T)` | Combine two of same type | `(a, b) -> a + b` |

## 🐢 Lazy evaluation

Stream pipelines are **lazy**: intermediate operations like `filter` and `map` do nothing until a *terminal* operation runs. When it does, elements flow through the whole pipeline one at a time. This enables optimizations — for example, `findFirst()` can stop early without processing the entire source, and short-circuiting operations skip needless work.

## ⚠️ Common Pitfalls

- **Reusing a stream** — a stream can be consumed only once; calling a terminal op twice throws `IllegalStateException`.
- **Side effects in lambdas** — modifying external state inside `map`/`filter` breaks the declarative model and misbehaves in parallel.
- **Forgetting the terminal op** — without one, nothing runs (the pipeline stays lazy).
- **Overusing `parallelStream()`** — it can be slower for small data and unsafe with shared mutable state.
- **Unwrapping `Optional` with `get()`** without checking — throws if empty; prefer `orElse`/`map`.

## ❓ FAQs

### What is a functional interface and how does it relate to lambdas?
A functional interface has exactly one abstract method, which makes it a valid target for a lambda or method reference. When you write `Predicate<Integer> p = x -> x > 0;`, the lambda supplies the implementation of that single method. Examples include `Runnable`, `Comparator`, `Function`, and `Predicate`, often marked with `@FunctionalInterface`.

### What's the difference between intermediate and terminal operations?
Intermediate operations (`filter`, `map`, `sorted`) return a new stream and are lazy — they just build up the pipeline. Terminal operations (`collect`, `reduce`, `forEach`, `count`) trigger the actual processing and produce a result or side effect. A stream does no work until a terminal operation is invoked, and it can only be used once.

### What does "lazy evaluation" mean for streams?
It means intermediate steps aren't executed as you chain them; they only run when a terminal operation pulls data through. This lets the JVM optimize — for instance, short-circuiting with `findFirst` or `anyMatch` stops as soon as the answer is known, avoiding processing the whole collection. Laziness is what makes streams both expressive and efficient.

### When should I use a method reference instead of a lambda?
Use a method reference when your lambda does nothing but call an existing method, like `s -> s.toUpperCase()` becoming `String::toUpperCase`. It's more concise and signals intent clearly. Stick with a full lambda when you need extra logic, multiple statements, or arguments arranged differently than the method expects.

### Why use Optional instead of returning null?
`Optional` makes "there might be no value" explicit in the type, forcing callers to consider the empty case instead of forgetting a null check. Methods like `orElse`, `map`, and `ifPresent` let you handle absence cleanly without scattered `if (x != null)` guards. It's mainly intended for return values, not fields or parameters.
