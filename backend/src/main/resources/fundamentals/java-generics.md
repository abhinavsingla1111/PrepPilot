# 🧬 Java Generics

> Write code once that works safely with any type — no casting, no surprises.

## 🧠 What & Why

**Generics** let you write classes and methods that work with a *type parameter* instead of a fixed type. That's why `List<String>` and `List<Integer>` share the same `List` code but stay type-safe: the compiler guarantees you only put the right kind of object in and get it out.

Before generics (Java 5), collections held raw `Object`s, forcing ugly casts and risking runtime `ClassCastException`s. Generics move those errors to *compile time*, where they're cheap to fix. In interviews, generics show you understand type safety, reusability, and the subtleties of wildcards and erasure.

## 🔑 Key Concepts

- **Type parameter** — A placeholder type like `T`, `E`, `K`, `V` used in a generic declaration.
- **Generic class** — A class parameterized by a type, e.g. `Box<T>`.
- **Generic method** — A method with its own type parameter, e.g. `<T> T first(List<T> list)`.
- **Bounded type** — Restricts a parameter, e.g. `<T extends Number>` means "T must be a Number or subtype."
- **Wildcard `?`** — An unknown type; `? extends X` (upper bound) or `? super X` (lower bound).
- **Type erasure** — Generics exist only at compile time; the JVM erases them to raw types at runtime.

## 💻 Example

```java
import java.util.*;

public class GenericsDemo {

    // A generic class: Box can hold any type T
    static class Box<T> {
        private T value;
        void set(T value) { this.value = value; }
        T get() { return value; }
    }

    // A generic method with a bounded type: T must be a Number
    static <T extends Number> double sum(List<T> nums) {
        double total = 0;
        for (T n : nums) {
            total += n.doubleValue(); // allowed because T is a Number
        }
        return total;
    }

    // Upper-bounded wildcard: read from any list of Number or subtype
    static double total(List<? extends Number> nums) {
        double t = 0;
        for (Number n : nums) t += n.doubleValue();
        return t;
    }

    public static void main(String[] args) {
        Box<String> box = new Box<>();
        box.set("PrepPilot");
        System.out.println(box.get().length()); // no cast needed -> 9

        System.out.println(sum(List.of(1, 2, 3)));       // 6.0
        System.out.println(total(List.of(1.5, 2.5)));    // 4.0
    }
}
```

## 🔀 PECS: `extends` vs `super`

The rule of thumb is **PECS — Producer Extends, Consumer Super**:

| Wildcard | Meaning | Use when you want to... |
|---|---|---|
| `? extends T` | T or any subtype | **Read** items *from* the structure (it produces T) |
| `? super T` | T or any supertype | **Write** items *into* the structure (it consumes T) |
| `?` (unbounded) | Any type | Only use `Object` methods / don't care about the type |

So a `List<? extends Number>` is safe to read `Number`s from, and a `List<? super Integer>` is safe to add `Integer`s to.

## 🧯 Type erasure

At compile time, Java uses generics to check your types, then **erases** them — replacing `T` with its bound (or `Object`) in the bytecode. That's why you can't write `new T()`, use `T` in a `static` context, or check `list instanceof List<String>` at runtime. It's the price of backward compatibility with pre-generics code.

## ⚠️ Common Pitfalls

- Using raw types like `List` instead of `List<String>` — you lose type safety and get warnings.
- Trying to create arrays of generics (`new T[]`) — not allowed due to erasure.
- Expecting `List<Integer>` to be a subtype of `List<Number>` — it isn't; that's what wildcards are for.
- Forgetting PECS and getting compile errors when adding to a `? extends` list (you can't).
- Assuming generic type info survives at runtime — erasure removes it.

## ❓ FAQs

### Why were generics added to Java?
Generics add compile-time type safety and remove the need for casts when using collections and other container types. Before them, a `List` held `Object`s, so mistakes surfaced as runtime `ClassCastException`s. With `List<String>`, the compiler rejects wrong types up front, making code safer and more self-documenting.

### What is type erasure and why does it matter?
Type erasure means the compiler uses generic type information only for checking, then strips it out, replacing type parameters with their bounds (or `Object`) in the bytecode. This keeps generics backward-compatible with old code but has consequences: you can't instantiate `T`, create generic arrays, or inspect a type argument at runtime. Understanding it explains many "why can't I do this?" errors.

### What does the PECS rule mean?
PECS stands for "Producer Extends, Consumer Super." Use `? extends T` when a structure *produces* values you'll read (you can safely treat them as `T`), and `? super T` when it *consumes* values you'll write (you can safely add `T`). It tells you which wildcard keeps your generic code both flexible and type-safe.

### Why isn't List<Integer> a subtype of List<Number>?
Because generics are *invariant*: even though `Integer` is a `Number`, `List<Integer>` and `List<Number>` are unrelated types. If they were interchangeable, you could add a `Double` to a `List<Number>` that's actually a `List<Integer>`, breaking type safety. Wildcards like `List<? extends Number>` exist to safely bridge this gap when needed.

### What's the difference between a bounded type and a wildcard?
A bounded type parameter like `<T extends Number>` names a type you can reference throughout the method or class, letting you both use and return `T`. A wildcard like `? extends Number` represents an unknown, unnamed type and is used in a single parameter position, mainly for flexibility when reading or writing. Use bounded parameters when you need to refer to the type by name, wildcards when you don't.
