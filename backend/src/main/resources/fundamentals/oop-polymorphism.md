# 🎭 Polymorphism

> One interface, many forms — write code once that works with many types.

## 🧠 What & Why

Polymorphism (Greek for "many shapes") lets a single piece of code work with objects of different types through a shared interface or parent class. You call the same method name, and each object responds in its own way.

Imagine a list of `Shape` objects — circles, squares, triangles. You loop over them and call `area()` on each. You don't write an `if` for every shape type; each shape knows how to compute its own area. Adding a new shape later requires zero changes to the loop. That's the power of polymorphism: **extensibility without rewriting existing code**.

There are two flavors. **Compile-time polymorphism** (method *overloading*) picks the method by its parameter list when the code is compiled. **Run-time polymorphism** (method *overriding*) picks the method based on the object's actual type while the program runs — this is the deeper, more powerful form, powered by **dynamic dispatch**.

## 🔑 Key Concepts

- **Overloading (compile-time)** — Same method name, different parameter lists, resolved by the compiler.
- **Overriding (run-time)** — A subclass redefines a parent method; the version that runs depends on the real object type.
- **Dynamic dispatch** — The runtime mechanism that routes a method call to the correct overridden implementation.
- **Upcasting** — Treating a subclass object as its parent type (e.g., `Shape s = new Circle()`), which is safe and implicit.
- **Downcasting** — Converting a parent reference back to a subclass type; must be explicit and can fail at runtime.

## 💻 Example

```java
// Common abstraction: everything that has an area.
abstract class Shape {
    abstract double area(); // each shape must define this
}

class Circle extends Shape {
    double radius;
    Circle(double radius) { this.radius = radius; }
    @Override
    double area() { return Math.PI * radius * radius; }
}

class Rectangle extends Shape {
    double width, height;
    Rectangle(double width, double height) { this.width = width; this.height = height; }
    @Override
    double area() { return width * height; }
}

public class Main {
    // Compile-time polymorphism: overloaded 'describe' methods.
    static void describe(int count) { System.out.println("There are " + count + " items."); }
    static void describe(String label) { System.out.println("Label: " + label); }

    public static void main(String[] args) {
        // Upcasting: Circle and Rectangle stored as Shape.
        Shape[] shapes = { new Circle(2), new Rectangle(3, 4) };

        // Run-time polymorphism: the correct area() is chosen per object.
        for (Shape s : shapes) {
            System.out.printf("Area = %.2f%n", s.area()); // dynamic dispatch
        }

        describe(5);         // compiler picks describe(int)
        describe("Shapes");  // compiler picks describe(String)
    }
}
```

## 🎯 Overloading vs Overriding

| Aspect | Overloading (compile-time) | Overriding (run-time) |
| --- | --- | --- |
| Where | Same class | Subclass replaces parent method |
| Signature | Different parameter lists | Identical signature |
| Resolved | At compile time | At run time (dynamic dispatch) |
| Return type | Can differ | Same or covariant |
| Annotation | None | `@Override` |

## ⚠️ Common Pitfalls

- **Confusing overloading with overriding.** Same name is where the similarity ends — one is compile-time selection by arguments, the other is run-time selection by object type.
- **Unsafe downcasting.** Casting a parent reference to the wrong subclass throws `ClassCastException`. Check with `instanceof` first.
- **Forgetting `@Override`.** Without it, a typo in the method name creates a new method instead of overriding, and the bug is silent. The annotation makes the compiler verify your intent.
- **Overusing overloading.** Too many similar overloads can confuse both the compiler and readers about which one gets called.

## ❓ FAQs

### What is polymorphism in simple terms?
Polymorphism lets you use a single interface or method name to work with many different types, where each type provides its own behavior. This means you can write general code — like a loop that calls `area()` — that automatically does the right thing for every specific object it encounters.

### What is the difference between compile-time and run-time polymorphism?
Compile-time polymorphism is achieved through method overloading, where the compiler picks the method based on the argument types before the program runs. Run-time polymorphism is achieved through method overriding, where the JVM decides which subclass implementation to call at run time based on the object's actual type, using dynamic dispatch.

### What is dynamic dispatch?
Dynamic dispatch is the mechanism that, at run time, looks at the actual (not declared) type of an object and calls the correct overridden method for that type. It's what makes a `Shape` reference pointing to a `Circle` invoke `Circle.area()` rather than some generic version, enabling true run-time polymorphism.

### What is upcasting and why is it safe?
Upcasting is treating a subclass object as an instance of its parent type, such as assigning a `Circle` to a `Shape` variable. It's always safe and implicit because every subclass instance genuinely *is* an instance of its parent, so no information is lost and no runtime check is needed.

### How does polymorphism help with maintainability?
Polymorphism lets you add new types that plug into existing code without modifying it — you just create a new subclass that honors the shared interface. This aligns with the Open/Closed Principle, so systems become easier to extend and less risky to change as requirements grow.
