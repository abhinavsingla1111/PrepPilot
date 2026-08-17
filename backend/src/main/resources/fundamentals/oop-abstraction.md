# 🎭 Abstraction

> Show what something does, hide how it does it.

## 🧠 What & Why

Abstraction is the art of exposing only the **essential features** of something while hiding the complicated inner workings. You interact with a simple surface; the messy details stay out of sight.

When you drive a car, you press the accelerator to go faster. You don't think about fuel injection, spark timing, or torque curves. The pedal is an **abstraction** over a very complex machine. Software works the same way: a `List` lets you `add()` and `remove()` items without knowing whether it's backed by an array or a linked structure.

Abstraction reduces the mental load on whoever uses your code. It also decouples the "what" from the "how," so you can swap implementations freely. This leads to the classic advice: **program to an interface, not an implementation** — depend on the abstract capability, not a specific concrete class.

## 🔑 Key Concepts

- **Interface** — A pure contract listing method signatures with no implementation. A class `implements` it and promises to provide the behavior.
- **Abstract class** — A partially implemented class that can't be instantiated on its own; it may mix concrete methods with `abstract` ones subclasses must fill in.
- **Program to an interface** — Declare variables and parameters by their abstract type so any implementation fits.
- **Contract** — The set of promises an abstraction makes about behavior, independent of how it's fulfilled.

## 💻 Example

```java
// The abstraction: a contract for anything that can send a message.
interface Notifier {
    void send(String message); // WHAT it does — not HOW.
}

// One hidden implementation.
class EmailNotifier implements Notifier {
    public void send(String message) {
        // Complex details (SMTP, retries) are hidden behind this method.
        System.out.println("Email sent: " + message);
    }
}

// Another implementation with completely different internals.
class SmsNotifier implements Notifier {
    public void send(String message) {
        System.out.println("SMS sent: " + message);
    }
}

public class Main {
    // We depend on the abstraction (Notifier), not a concrete class.
    static void alertUser(Notifier notifier, String msg) {
        notifier.send(msg); // We don't care how it sends.
    }

    public static void main(String[] args) {
        alertUser(new EmailNotifier(), "Interview at 10am"); // swap freely
        alertUser(new SmsNotifier(), "Interview at 10am");
    }
}
```

## 🎯 Abstract Class vs Interface

| Feature | Abstract Class | Interface |
| --- | --- | --- |
| Purpose | Share code + partial implementation | Pure contract of capabilities |
| Instantiable? | No | No |
| Fields | Can hold state (instance fields) | Only constants (`static final`) |
| Methods | Concrete + abstract mix | Abstract (+ `default`/`static` in modern Java) |
| Multiple inheritance | One superclass only | A class can implement many |
| Best when | Types are closely related | Unrelated types share a capability |

## 🚗 Real-World Analogy

A car's pedals and steering wheel are an abstraction over the engine and drivetrain. Two different cars — one gasoline, one electric — expose the *same* controls (accelerate, brake, steer) even though what happens underneath is completely different. As a driver, you "program to the interface": your driving skills transfer because you depend on the pedals, not the engine internals.

## ⚠️ Common Pitfalls

- **Leaky abstractions.** If callers must understand internal details to use your class correctly, the abstraction is failing its job.
- **Choosing abstract class when an interface fits.** Because Java allows only one superclass, an unnecessary abstract class can block a type from extending something else.
- **Over-abstracting too early.** Adding interfaces "just in case" for code with a single implementation adds indirection without benefit. Abstract when you actually see variation.

## ❓ FAQs

### What is the difference between abstraction and encapsulation?
Abstraction is about hiding *complexity* by exposing only essential behavior, answering "what does this do?", while encapsulation is about hiding *data* and protecting state, answering "who can touch this?". Abstraction is a design-level idea about the interface you expose; encapsulation is an implementation technique using access modifiers.

### When should I use an interface versus an abstract class?
Use an interface when unrelated classes share a capability (like "can be serialized" or "can be sent"), and use an abstract class when closely related types need to share common code and state. A quick rule: interfaces define *what* a type can do, abstract classes provide a partial *how* for a family of related types.

### What does "program to an interface, not an implementation" mean?
It means you should declare variables, parameters, and return types using an abstract type (an interface or abstract class) rather than a specific concrete class. This lets you swap in any implementation without changing the calling code, making your system flexible and easier to test with mocks.

### Can an interface have method bodies in Java?
Yes — since Java 8, interfaces can have `default` and `static` methods with actual implementations, and since Java 9 they can also have `private` helper methods. However, the core idea of an interface remains a contract, so default methods are best used to add convenience or evolve APIs without breaking existing implementers.

### Why is abstraction useful in large systems?
Abstraction decouples users of a component from its internal details, so teams can work in parallel and implementations can change without rippling out. It also makes testing easier, because you can substitute lightweight fakes for heavy real components as long as they honor the same contract.
