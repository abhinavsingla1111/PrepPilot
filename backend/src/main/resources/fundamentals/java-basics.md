# 🧱 Java Basics

> The everyday building blocks: variables, types, operators, control flow, and methods.

## 🧠 What & Why

Before you can solve interview problems in Java, you need the basics that every program is built from: how to store data in **variables**, how to make decisions with **control flow**, and how to package logic into **methods**. These are the nuts and bolts you'll use in literally every solution.

Java is **statically typed**, meaning every variable has a fixed type known at compile time. That feels stricter than languages like Python, but it catches mistakes early and makes your intent clear. Get comfortable here and everything else — collections, streams, concurrency — becomes much easier.

## 🔑 Key Concepts

- **Variable** — A named box that holds a value, e.g. `int age = 30;`.
- **Primitive type** — A raw value stored directly (`int`, `double`, `boolean`, `char`, etc.). Fast and can't be `null`.
- **Object / reference type** — A variable that points to an object on the heap (`String`, arrays, `Integer`, your own classes). Can be `null`.
- **Operator** — Symbols that compute values: arithmetic (`+ - * / %`), comparison (`== != < >`), logical (`&& || !`), assignment (`= += -=`).
- **Control flow** — Statements that decide *what runs next*: `if`, `for`, `while`, `switch`.
- **Method** — A reusable block of code with a name, inputs (parameters), and an optional return value.
- **`var`** — Lets the compiler infer a local variable's type from its initializer (Java 10+).
- **Autoboxing** — Automatic conversion between a primitive (`int`) and its wrapper object (`Integer`).

## 💻 Example

```java
public class Basics {
    public static void main(String[] args) {
        // --- Variables & primitive types ---
        int count = 5;              // whole number
        double price = 19.99;       // decimal number
        boolean isReady = true;     // true/false
        char grade = 'A';           // single character

        // --- var: compiler infers the type (still String) ---
        var name = "PrepPilot";     // same as String name = "PrepPilot";

        // --- Operators ---
        int total = count * 2;      // arithmetic -> 10
        boolean cheap = price < 20; // comparison -> true

        // --- Control flow: if / else ---
        if (isReady && cheap) {
            System.out.println(name + " is ready and cheap!");
        } else {
            System.out.println("Not yet.");
        }

        // --- for loop ---
        for (int i = 0; i < 3; i++) {
            System.out.println("Loop i = " + i);
        }

        // --- while loop ---
        int n = total;
        while (n > 8) {
            n--;                    // decrement until condition fails
        }

        // --- switch ---
        switch (grade) {
            case 'A' -> System.out.println("Excellent");
            case 'B' -> System.out.println("Good");
            default  -> System.out.println("Keep going");
        }

        // --- Arrays ---
        int[] nums = {10, 20, 30};
        System.out.println("First number: " + nums[0]);

        // --- Method call ---
        System.out.println("Sum: " + add(count, total));
    }

    // A method: takes two ints, returns their sum.
    static int add(int a, int b) {
        return a + b;
    }
}
```

## 🔢 Primitive types at a glance

| Type | Size | Example | Notes |
|---|---|---|---|
| `byte` | 8-bit | `byte b = 100;` | Small integers |
| `int` | 32-bit | `int i = 42;` | Default whole-number type |
| `long` | 64-bit | `long big = 9_000_000_000L;` | Needs `L` suffix |
| `float` | 32-bit | `float f = 3.14f;` | Needs `f` suffix |
| `double` | 64-bit | `double d = 3.14;` | Default decimal type |
| `boolean` | ~1-bit | `boolean ok = true;` | `true` or `false` |
| `char` | 16-bit | `char c = 'x';` | Single Unicode character |

## ⚠️ Common Pitfalls

- **Integer division**: `5 / 2` is `2`, not `2.5`. Use a `double` if you want decimals.
- **`==` on objects** compares references, not contents — use `.equals()` for `String` and other objects.
- **Uninitialized locals**: local variables have no default value; you must assign before use.
- **Autoboxing surprises**: comparing two `Integer` objects with `==` can be false for values outside the cached range (`-128..127`).
- **Array index out of bounds**: valid indices are `0` to `length - 1`.

## ❓ FAQs

### What's the difference between primitive types and objects?
Primitives (`int`, `double`, `boolean`, ...) store the raw value directly, are fast, and can never be `null`. Objects (like `String` or `Integer`) are stored on the heap and accessed through a reference, so they can be `null` and carry methods. Each primitive has a matching wrapper class for when you need object behavior.

### What is autoboxing and when does it happen?
Autoboxing is Java automatically converting a primitive to its wrapper object (e.g. `int` → `Integer`), and unboxing does the reverse. It happens when you mix primitives and objects, such as putting an `int` into a `List<Integer>`. It's convenient, but be careful: unboxing a `null` wrapper throws a `NullPointerException`.

### When should I use the `var` keyword?
Use `var` for local variables where the type is obvious from the right-hand side, like `var list = new ArrayList<String>();`, to cut down on repetition. Avoid it when the inferred type isn't clear from context, since readability matters more than brevity. Note `var` only works for local variables, not fields, parameters, or return types.

### Why does `5 / 2` give `2` instead of `2.5`?
Because both operands are integers, Java performs integer division and discards the fractional part. To get `2.5`, make at least one operand a floating-point number, e.g. `5.0 / 2` or `(double) 5 / 2`. This is a classic off-by-decimals bug in interview code.

### What's the difference between `while` and `for` loops?
Functionally they're interchangeable, but `for` loops are ideal when you know the number of iterations or need a counter, since the init/condition/update sit together. `while` loops shine when you loop *until some condition changes* and don't have a fixed count. Pick whichever makes the intent clearest.
