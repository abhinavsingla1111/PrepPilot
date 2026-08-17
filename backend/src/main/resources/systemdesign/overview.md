# 🧭 System Design — From Beginner to Pro

> Your one-stop place to go from "what even is system design?" to confidently designing large systems in interviews and at work.

---

## 🎯 What is System Design?

**System design** is the process of defining the **architecture, components, data, and interactions** of a software system so it meets its goals — being fast, reliable, and able to grow.

If coding is about making *one program* work, system design is about making *many programs work together* to serve millions of users without falling over.

A good design answers questions like:

- How does data flow from the user to the database and back?
- What happens when traffic suddenly grows 100×?
- Where could this system fail, and how does it recover?
- What trade-offs are we making, and why?

---

## 🤔 Why does it matter?

- **Interviews** — System design rounds are standard for mid/senior roles. They test how you *think*, not whether you memorized an answer.
- **Real work** — Every feature you ship lives inside a larger system. Understanding the whole picture makes you a stronger engineer.
- **Trade-offs** — There is rarely one "right" answer. Great engineers reason about options and pick the best fit for the constraints.

> There are no perfect designs — only designs that fit (or don't fit) a set of requirements. Your job is to make the trade-offs **explicit**.

---

## 🗺️ The two levels of design

This course splits system design into the two levels interviewers actually ask about:

| | High-Level Design (HLD) | Low-Level Design (LLD) |
| --- | --- | --- |
| **Focus** | The big picture — services, databases, queues | The details — classes, methods, objects |
| **Question** | "Design Twitter's feed" | "Design a parking lot in code" |
| **Output** | Architecture diagram + data flow | Class diagram + working code |
| **Skills** | Scaling, storage, networking | OOP, design patterns, clean code |

Start with the **Overview** in each section, then work through the 10 hands-on examples. Each example includes diagrams, data models or class designs, code, and FAQs.

---

## 🧩 The building blocks you'll keep seeing

You'll meet these ideas again and again — think of them as your toolbox:

- **Load Balancer** — spreads traffic across many servers.
- **Cache** (Redis) — keeps hot data in memory for fast reads.
- **Database** — SQL for structured/consistent data, NoSQL for scale/flexibility.
- **Message Queue** (Kafka) — decouples producers from consumers, absorbs spikes.
- **CDN** — serves static content (images, video) from near the user.
- **Replication & Sharding** — copy data for availability, split it for scale.

---

## 🚀 How to use this course

1. **Read both overviews** (HLD and LLD) to build the mental model.
2. **Work through the examples** — don't just read; try to sketch the design yourself first, then compare.
3. **Study the FAQs** — they mirror the follow-up questions interviewers love to ask.
4. **Focus on trade-offs** — for every choice, be able to say *why*.

Ready? Open **High-Level Design → Overview** to begin.
