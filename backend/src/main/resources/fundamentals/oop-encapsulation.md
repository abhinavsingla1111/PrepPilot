# 🔒 Encapsulation

> Keep an object's data safe by bundling it with the code that's allowed to touch it.

## 🧠 What & Why

Encapsulation means packaging an object's **data** and the **methods** that operate on that data together, then controlling who can access what. The data becomes private, and the outside world interacts only through a controlled set of public methods.

Think of a capsule: the medicine is sealed inside, and you interact with it through a clean outer shell. In code, this means marking fields as `private` so no one can reach in and set them to invalid values. Instead, callers go through methods that can validate and protect the object's rules.

Why do this? Because uncontrolled access leads to bugs. If any code can set `balance = -1000`, your bank account is broken. Encapsulation lets you enforce **invariants** — rules that must always hold true — in one place, so the rest of the program can't accidentally violate them.

## 🔑 Key Concepts

- **Access modifiers** — Keywords that control visibility. `private` (only this class), `protected` (this class and subclasses), `public` (everyone), and package-private (the default, same package).
- **Getter** — A method that reads a private field, e.g., `getBalance()`.
- **Setter** — A method that updates a private field, often with validation, e.g., `setAge(int age)`.
- **Invariant** — A condition that must always be true for the object (e.g., balance never negative).
- **Information hiding** — Deliberately concealing internal representation so it can change without breaking callers.

## 💻 Example

```java
// BAD: fields are public, so anyone can set an invalid state.
class BankAccountBad {
    public double balance; // no protection at all
}

// Somewhere else:
// BankAccountBad a = new BankAccountBad();
// a.balance = -5000; // Oops — impossible balance, and nothing stops it.

// GOOD: fields are private and guarded by methods that enforce the rules.
class BankAccount {
    private double balance; // hidden from outside code

    public BankAccount(double initial) {
        if (initial < 0) throw new IllegalArgumentException("Initial balance cannot be negative");
        this.balance = initial;
    }

    // Getter: read-only access to the balance.
    public double getBalance() {
        return balance;
    }

    // Controlled mutation: the invariant (balance >= 0) is protected here.
    public void withdraw(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (amount > balance) throw new IllegalStateException("Insufficient funds");
        balance -= amount;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        balance += amount;
    }
}
```

## 🎯 Access Modifiers at a Glance

| Modifier | Same Class | Same Package | Subclass | Everywhere |
| --- | --- | --- | --- | --- |
| `private` | ✅ | ❌ | ❌ | ❌ |
| (default) | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

## ⚠️ Common Pitfalls

- **Making everything public.** This defeats encapsulation entirely — any code can corrupt your object's state.
- **Getters/setters with no logic for everything.** Blindly adding a getter and setter for every field just makes public fields with extra steps. Only expose what callers truly need.
- **Returning references to mutable internals.** A getter that returns your internal `List` lets callers modify it directly, breaking your invariants. Return a copy or an unmodifiable view.

## ❓ FAQs

### What is encapsulation in one sentence?
Encapsulation is the practice of bundling an object's data with the methods that operate on it and restricting direct access to that data, so the object controls its own state through a well-defined interface.

### What is the difference between encapsulation and abstraction?
Encapsulation is about *hiding data and protecting state* using access modifiers and methods, while abstraction is about *hiding complexity* by exposing only the essential behavior behind a clean interface. They often work together, but encapsulation focuses on "how do I protect this data" and abstraction focuses on "what should the outside world see."

### Why should fields be private instead of public?
Private fields prevent outside code from putting the object into an invalid state, because every change must go through methods that can validate input and enforce invariants. This also lets you change the internal representation later without breaking any callers, since they only depend on your public methods.

### Do getters and setters break encapsulation?
They can, if you mechanically add them for every field with no validation — that's basically public access in disguise. Good encapsulation exposes only the operations callers genuinely need and puts meaningful rules (validation, derived values, immutability) behind those methods.

### How does encapsulation help with maintenance?
Because callers depend only on your public interface, you're free to refactor the hidden internals — change a data structure, add caching, or fix a bug — without touching any external code. This isolation dramatically reduces the ripple effect of changes in large systems.
