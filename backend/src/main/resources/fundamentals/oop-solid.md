# 🏛️ SOLID Principles

> Five guidelines that keep object-oriented code flexible, testable, and easy to change.

## 🧠 What & Why

SOLID is an acronym for five design principles that help you write software that's easy to maintain and extend. Coined by Robert C. Martin ("Uncle Bob"), they aren't rigid laws but reliable heuristics for reducing tangled, brittle code.

Why memorize them? Because they show up constantly in interviews and, more importantly, in real work. Code that violates SOLID tends to be hard to test, scary to change, and full of ripple effects where fixing one thing breaks another. Following SOLID leads to small, focused, loosely coupled pieces that snap together cleanly.

Don't treat SOLID as a checklist to apply everywhere at maximum strength — that leads to over-engineering. Treat them as a lens: when code feels painful to change, one of these principles is usually being violated.

## 🔑 Key Concepts

- **SRP — Single Responsibility Principle** — A class should have one reason to change.
- **OCP — Open/Closed Principle** — Open for extension, closed for modification.
- **LSP — Liskov Substitution Principle** — Subtypes must be usable wherever their base type is expected.
- **ISP — Interface Segregation Principle** — Prefer many small interfaces over one fat one.
- **DIP — Dependency Inversion Principle** — Depend on abstractions, not concrete details.

## 📋 Summary Table

| Letter | Principle | One-line summary |
| --- | --- | --- |
| **S** | Single Responsibility | A class should do one thing and have one reason to change. |
| **O** | Open/Closed | Add new behavior by extending, not by editing existing code. |
| **L** | Liskov Substitution | A subclass should work anywhere its parent works, no surprises. |
| **I** | Interface Segregation | Don't force classes to implement methods they don't use. |
| **D** | Dependency Inversion | High-level code should depend on interfaces, not implementations. |

## 💻 Example

```java
// ❌ BAD: violates SRP — this class both models a report AND saves it.
class ReportBad {
    String content;
    void generate() { /* build report text */ }
    void saveToDisk() { /* file I/O logic here too */ } // second responsibility!
}

// ✅ GOOD (SRP): one class per responsibility.
class Report {
    String content;
    void generate() { /* build report text */ }
}
interface ReportSaver {                 // DIP + ISP: small abstraction
    void save(Report report);
}
class FileReportSaver implements ReportSaver {
    public void save(Report report) { /* file I/O */ }
}

// ✅ OCP + DIP: high-level code depends on the abstraction, and we can add
// new savers (e.g., CloudReportSaver) WITHOUT modifying this method.
class ReportService {
    private final ReportSaver saver; // depend on abstraction, not a concrete class
    ReportService(ReportSaver saver) { this.saver = saver; }

    void export(Report report) {
        report.generate();
        saver.save(report);
    }
}
```

## 🧪 LSP in Action

```java
// ❌ BAD (LSP violation): a Square "is-a" Rectangle by inheritance,
// but overriding setters breaks callers that expect independent width/height.
class Rectangle { int width, height;
    void setWidth(int w) { width = w; }
    void setHeight(int h) { height = h; }
}
class Square extends Rectangle {
    void setWidth(int w) { width = height = w; }  // surprising side effect
    void setHeight(int h) { width = height = h; } // breaks Rectangle's contract
}
// A method expecting a Rectangle (set width=5, height=4, expect area 20)
// gets 16 when handed a Square — the subtype is NOT safely substitutable.
```

## ⚠️ Common Pitfalls

- **Splitting classes too aggressively for SRP.** Ending up with dozens of one-line classes is its own kind of unmaintainable. "One responsibility" means one cohesive purpose, not one method.
- **Faking OCP with configuration flags.** Adding `if (type == ...)` branches is modification, not extension. Real OCP uses polymorphism.
- **Ignoring LSP with `instanceof` checks.** If callers must special-case certain subtypes, the substitution contract is broken.
- **Over-applying DIP.** Not every class needs an interface. Introduce abstractions where variation or testing actually demands them.

## ❓ FAQs

### What does SOLID stand for?
SOLID stands for Single Responsibility, Open/Closed, Liskov Substitution, Interface Segregation, and Dependency Inversion. Together they form a set of principles for writing object-oriented code that is loosely coupled, easy to test, and safe to extend over time.

### Can you explain the Single Responsibility Principle simply?
SRP says a class should have exactly one reason to change, meaning it should focus on a single responsibility rather than mixing unrelated concerns like business logic and persistence. When a class does one thing well, it's easier to understand, test, and modify without accidentally affecting unrelated behavior.

### What is the difference between the Open/Closed Principle and Dependency Inversion?
OCP says you should be able to add new behavior by extending code (typically via new subclasses or implementations) without editing existing, tested code. DIP says high-level modules should depend on abstractions rather than concrete classes; the two often work together, because depending on abstractions is what makes extension without modification possible.

### How does the Liskov Substitution Principle relate to inheritance?
LSP states that objects of a subclass must be substitutable for objects of the parent class without breaking the program's correctness. It's a discipline on inheritance: if a subclass changes expected behavior — like a `Square` that alters how `setWidth` works on a `Rectangle` — it violates LSP even though the code compiles fine.

### Do I have to follow all five principles all the time?
No — SOLID principles are heuristics, not absolute rules, and blindly maximizing them leads to over-engineering with needless abstractions. The skill is recognizing when code is becoming hard to change or test, then applying the relevant principle to relieve that specific pain.
