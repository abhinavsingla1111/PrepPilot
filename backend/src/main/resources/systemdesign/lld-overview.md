# 🧱 Low-Level Design (LLD) — Overview

> Learn to turn requirements into clean, extensible **object-oriented code**: the classes, relationships, and design patterns behind real features.

---

## 🎯 What is Low-Level Design?

**Low-Level Design** zooms in from architecture to **code structure**. Given a feature — say, a parking lot or an elevator — how do you model it with **classes and objects** so the code is clean, testable, and easy to extend?

It answers: *"What classes exist, what does each own, how do they interact, and which design patterns keep it flexible?"*

---

## 🧭 A framework for any LLD question

#### 1. Clarify requirements & scope
List the core use cases. Ask what's in and out of scope (e.g., "do we handle payments?").

#### 2. Identify the entities (nouns)
The nouns in the problem usually become **classes**: `Vehicle`, `ParkingSpot`, `Ticket`.

#### 3. Define behaviors (verbs)
The verbs become **methods**: `park()`, `unpark()`, `calculateFee()`.

#### 4. Establish relationships
How do classes relate — *has-a* (composition), *is-a* (inheritance), *uses-a* (association)?

#### 5. Apply design patterns
Reach for a pattern only when it removes real duplication or if/else sprawl.

#### 6. Write clean, working code
Encapsulate state, favor interfaces, keep classes focused.

---

## 🏛️ The four pillars of OOP (your foundation)

| Pillar | Meaning | Example |
| --- | --- | --- |
| **Encapsulation** | Hide internal state behind methods | `private balance; deposit()` |
| **Abstraction** | Expose *what*, hide *how* | `Shape.area()` |
| **Inheritance** | Reuse via *is-a* relationships | `Car extends Vehicle` |
| **Polymorphism** | One interface, many behaviors | `notify()` on Email/SMS |

---

## 🎨 Design patterns you'll use constantly

Patterns are proven solutions to recurring problems. The ones that show up most in interviews:

- **Strategy** — swap algorithms at runtime (pricing, split logic). *Removes if/else on "type".*
- **State** — behavior changes with internal state (vending machine, elevator).
- **Factory** — centralize object creation (`createVehicle(type)`).
- **Observer** — notify many subscribers of an event (pub/sub, notifications).
- **Singleton** — exactly one instance (config, connection pool). *Use sparingly.*
- **Decorator** — add behavior without subclass explosion (coffee + toppings).

---

## 🧩 SOLID principles (write code that lasts)

- **S**ingle Responsibility — a class does one thing.
- **O**pen/Closed — open to extension, closed to modification.
- **L**iskov Substitution — subtypes must be usable as their base type.
- **I**nterface Segregation — many small interfaces beat one fat one.
- **D**ependency Inversion — depend on abstractions, not concretions.

> A quick smell test: if adding a new "type" forces you to edit a big `switch`, you probably need **Strategy** or **polymorphism** instead.

---

## ✅ What to do next

The 10 examples move from the classic **Parking Lot** to **Movie Booking**. Each one shows the requirements, the design approach, a class diagram, working **Java** code, and FAQs. Try designing the classes yourself before reading the solution.
