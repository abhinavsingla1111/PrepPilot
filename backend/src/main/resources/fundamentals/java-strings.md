# 🧵 Java Strings

> Text in Java is immutable — and that one fact explains almost everything about it.

## 🧠 What & Why

A `String` in Java represents a sequence of characters, like `"hello"`. Strings are everywhere — user input, file names, JSON, log messages — so understanding how they behave is essential for both writing correct code and answering interview questions.

The single most important thing to know is that **Strings are immutable**: once created, a `String` object's contents can never change. Any method that looks like it "modifies" a string actually returns a brand-new string. This design brings safety, easy sharing, and reliable hashing — but it also means naive string-building in a loop can be slow, which is where `StringBuilder` comes in.

## 🔑 Key Concepts

- **Immutability** — A `String`'s characters can't change after creation; "modifying" methods return new strings.
- **String pool** — A special area in memory where string *literals* are cached and reused to save space.
- **`StringBuilder`** — A mutable, fast text builder for assembling strings; not thread-safe.
- **`StringBuffer`** — Like `StringBuilder` but thread-safe (synchronized) and slightly slower.
- **`==` vs `equals()`** — `==` compares references (same object?), `equals()` compares actual characters.
- **`.intern()`** — Forces a string into the pool so equal literals share one instance.

## 💻 Example

```java
public class StringsDemo {
    public static void main(String[] args) {
        // Immutability: concat returns a NEW string
        String a = "Prep";
        String b = a.concat("Pilot"); // a is still "Prep"
        System.out.println(a);        // -> Prep
        System.out.println(b);        // -> PrepPilot

        // String pool: literals are shared
        String x = "hi";
        String y = "hi";
        System.out.println(x == y);       // -> true (same pooled object)

        String z = new String("hi");      // forces a new heap object
        System.out.println(x == z);       // -> false (different reference)
        System.out.println(x.equals(z));  // -> true (same characters)

        // Building strings efficiently with StringBuilder
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            sb.append("ab");             // mutates in place, no new objects
        }
        System.out.println(sb.toString()); // -> ababab

        // Common methods
        String s = "  Hello, World  ";
        System.out.println(s.trim());              // "Hello, World"
        System.out.println(s.toUpperCase());       // "  HELLO, WORLD  "
        System.out.println(s.indexOf("World"));    // 9
        System.out.println(s.substring(2, 7));     // "Hello"
        System.out.println("a,b,c".split(",").length); // 3
    }
}
```

## ⚖️ StringBuilder vs StringBuffer vs String

| Feature | `String` | `StringBuilder` | `StringBuffer` |
|---|---|---|---|
| Mutable? | No | Yes | Yes |
| Thread-safe? | Yes (immutable) | No | Yes (synchronized) |
| Speed | Slow for concatenation | Fastest | Slower than builder |
| Use when | Fixed text | Single-threaded building | Multi-threaded building |

## 🧠 How immutability + the pool work

When you write a string literal like `"hi"`, the JVM checks the **string pool**. If `"hi"` already exists there, it reuses that same object; otherwise it adds it. Because strings can never change, sharing them is completely safe — no one can mutate a value out from under someone else. Using `new String("hi")` skips the pool and always creates a fresh heap object, which is why `==` then returns `false`.

## ⚠️ Common Pitfalls

- Comparing strings with `==` instead of `.equals()` — `==` checks identity, not content.
- Building strings with `+=` inside a loop — each iteration creates a new `String` (O(n²) overall). Use `StringBuilder`.
- Assuming a "modifying" method changes the original — it returns a new string you must capture.
- Forgetting `null` checks — call `"literal".equals(userInput)` to avoid a `NullPointerException` on `userInput`.

## ❓ FAQs

### Why are Java Strings immutable?
Immutability makes strings safe to share across threads and data structures without copying or locking, since no one can alter them. It also lets the JVM cache their hash code (great for `HashMap` keys) and pool literals to save memory. The trade-off is that repeated modification creates many objects, which `StringBuilder` solves.

### What's the difference between == and equals() for strings?
`==` compares references — it's `true` only when both variables point to the exact same object. `.equals()` compares the actual character contents. Because of the string pool, two identical literals may share an object and pass `==`, but you should always use `.equals()` for content comparison to avoid subtle bugs.

### When should I use StringBuilder instead of String concatenation?
Use `StringBuilder` whenever you build a string through many steps, especially inside loops, because String's `+` creates a new object each time and can become O(n²). For a handful of concatenations the compiler often optimizes `+` into a `StringBuilder` anyway, so it mainly matters in loops or performance-sensitive code.

### What is the String pool?
The string pool is a special region of memory where the JVM stores unique string literals. When you use a literal like `"hi"`, Java reuses the pooled instance instead of allocating a new one, which saves memory. You can force a runtime string into the pool with `.intern()`, and `new String("hi")` deliberately bypasses it.

### StringBuilder vs StringBuffer — which should I use?
Prefer `StringBuilder` in almost all cases; it's the same API but faster because it isn't synchronized. Use `StringBuffer` only when multiple threads share and mutate the same builder, which is rare — usually it's cleaner to give each thread its own `StringBuilder` or synchronize at a higher level.
