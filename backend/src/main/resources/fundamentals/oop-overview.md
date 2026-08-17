# 🧭 Object-Oriented Programming: Overview

> Model your code the way you think about the real world — as objects that hold data and do things.

## 🧠 What & Why

Object-Oriented Programming (OOP) is a way of structuring code around **objects** rather than around functions and logic alone. An object bundles together some **data** (its state) and the **behavior** that operates on that data. Instead of one giant script, you build many small, self-contained objects that talk to each other.

Why bother? As programs grow, procedural code becomes hard to change without breaking something. OOP helps you manage complexity by grouping related things together, hiding messy details, and reusing code. It maps naturally to how we describe the world: a `Car` has a color and can `drive()`; a `BankAccount` has a balance and can `deposit()`.

OOP rests on four big ideas — **encapsulation, abstraction, inheritance, and polymorphism** — often called the "four pillars." You don't need to master them all at once. Think of this page as the map, and the pillars as the territory.

## 🔑 Key Concepts

- **Class** — A blueprint or template that defines what data and behavior objects will have. It's the cookie cutter, not the cookie.
- **Object** — A concrete instance created from a class. Each object has its own copy of the data. It's the actual cookie.
- **State** — The data an object holds at a given moment (e.g., a car's current speed).
- **Behavior** — The actions an object can perform, defined as methods (e.g., `accelerate()`).
- **Encapsulation** — Bundling data with the methods that use it, and hiding internal details.
- **Abstraction** — Exposing only what matters and hiding the how.
- **Inheritance** — Building new classes on top of existing ones to reuse code.
- **Polymorphism** — Letting one interface work with many different underlying types.

## 💻 Example

```java
// A class is a blueprint. It describes state (fields) and behavior (methods).
class Dog {
    // State: every Dog object gets its own name and age.
    String name;
    int age;

    // Constructor: runs when we create a new Dog object.
    Dog(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Behavior: something a Dog can do.
    void bark() {
        System.out.println(name + " says: Woof!");
    }
}

public class Main {
    public static void main(String[] args) {
        // Objects are concrete instances of the Dog class.
        Dog rex = new Dog("Rex", 3);   // one object
        Dog bella = new Dog("Bella", 5); // a separate object with its own state

        rex.bark();   // Rex says: Woof!
        bella.bark(); // Bella says: Woof!
    }
}
```

## 🎯 Class vs Object

| Aspect | Class | Object |
| --- | --- | --- |
| What it is | A blueprint/definition | A concrete instance |
| When it exists | At compile time (design) | At run time (in memory) |
| How many | One definition | Many instances possible |
| Analogy | Architect's house plan | An actual built house |
| Java keyword | `class Dog { ... }` | `new Dog(...)` |

## ⚠️ Common Pitfalls

- **Confusing the class with the object.** The class is the recipe; the object is the meal. You call `new` to turn a class into an object.
- **Putting everything in one class.** A class that does too much is hard to maintain. Aim for one clear responsibility per class.
- **Thinking OOP means "more code is better."** Over-engineering with deep hierarchies and endless abstractions can hurt more than help.

## ❓ FAQs

### What is the difference between a class and an object?
A class is a blueprint that defines the structure (fields) and behavior (methods) that its instances will have, while an object is a specific instance created from that class using `new`. You can create many independent objects from a single class, and each object keeps its own copy of the state.

### What are the four pillars of OOP?
The four pillars are encapsulation (bundling and hiding data), abstraction (exposing only the essentials), inheritance (reusing behavior from a parent class), and polymorphism (one interface serving many types). Together they let you build code that is modular, reusable, and easy to extend.

### Why choose OOP over procedural programming?
OOP shines when a system grows in size and complexity, because grouping data with behavior makes code easier to reason about, maintain, and extend. Procedural code can be simpler for small scripts, but OOP's encapsulation and polymorphism reduce coupling and make large codebases more manageable.

### Is OOP always the best approach?
No. OOP is a great fit for domains with rich, interacting entities, but functional or procedural styles can be cleaner for data pipelines, simple scripts, or math-heavy code. Good engineers pick the paradigm that fits the problem rather than forcing everything into objects.

## What's Next

Now that you have the map, explore each pillar in depth: **encapsulation**, **abstraction**, **inheritance**, and **polymorphism**. Once those click, level up your design skills with the **SOLID** principles and common **design patterns**, which build directly on these four ideas.
