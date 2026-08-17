# 🏗️ High-Level Design (HLD) — Overview

> Learn to design the architecture of large systems: the services, data stores, and connections that let an app serve millions.

---

## 🎯 What is High-Level Design?

**High-Level Design** describes a system from a **bird's-eye view**. Instead of individual classes, you think about **boxes and arrows**: which services exist, how they talk, where data lives, and how the whole thing scales.

It answers: *"If a user taps a button, what journey does that request take through our system, and how do we keep it fast and reliable at scale?"*

---

## 🧠 A framework for any HLD question

Interviewers care about your **approach**. Follow these steps every time:

#### 1. Clarify requirements
Split them into two kinds:

- **Functional** — what the system does (e.g., "shorten a URL", "post a tweet").
- **Non-functional** — how well it does it (latency, availability, consistency, durability).

#### 2. Estimate the scale
Do quick back-of-the-envelope math: users, requests/sec, storage/year. This drives every later decision.

#### 3. Define the APIs
A few key endpoints (`POST /tweet`, `GET /feed`) pin down the contract.

#### 4. Design the data model
What are the main entities and how are they stored (SQL vs NoSQL)?

#### 5. Draw the high-level architecture
Client → Load Balancer → Services → Cache/DB/Queue. This is the core diagram.

#### 6. Deep-dive & discuss trade-offs
Scale the bottlenecks: caching, sharding, replication, async processing.

---

## ⚖️ The trade-offs that define HLD

Almost every decision is a trade-off between these forces:

| Force | Question it answers |
| --- | --- |
| **Latency vs throughput** | Fast for one user, or high volume overall? |
| **Consistency vs availability** | Always correct, or always up? (CAP theorem) |
| **Read vs write optimization** | Is the system read-heavy or write-heavy? |
| **Cost vs performance** | How much are we willing to spend? |

> **CAP theorem:** in a distributed system, during a network partition you can have **Consistency** or **Availability**, but not both. Most web systems favor availability + *eventual* consistency.

---

## 🧩 Core components cheat sheet

- **Load Balancer** — distributes requests; enables horizontal scaling.
- **Application Servers** — stateless business logic (easy to scale out).
- **Cache** (Redis/Memcached) — fast in-memory reads; reduces DB load.
- **Database** — SQL (ACID, joins) or NoSQL (scale, flexible schema).
- **Message Queue** (Kafka/RabbitMQ) — async, decoupled, spike-absorbing.
- **CDN** — caches static assets close to users.
- **Object Storage** (S3) — cheap, durable storage for files/media.

---

## 📈 Scaling techniques you'll reuse

- **Horizontal scaling** — add more machines (preferred) vs vertical (bigger machine).
- **Caching** — store hot data in memory; the single biggest read win.
- **Replication** — copy data to replicas for availability and read scale.
- **Sharding** — split data across nodes so no single DB is a bottleneck.
- **Async processing** — push slow work (emails, video encoding) onto queues.

---

## ✅ What to do next

Work through the 10 examples in order — they progress from the classic **URL Shortener** to complex systems like **Payment** and **Ride Sharing**. For each, try to sketch the architecture yourself before reading the solution.
