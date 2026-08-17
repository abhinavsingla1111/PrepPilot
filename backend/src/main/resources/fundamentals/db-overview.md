# 🗄️ Databases: SQL vs NoSQL

> Where your data lives — and the two big philosophies for storing it.

Almost every app needs to **remember things**: users, orders, messages, scores. A **database** is the organized place that data lives in, and a **DBMS** (Database Management System) is the software that stores, protects, and lets you query it.

There are two big families. **SQL (relational)** databases store data in neat **tables** with a fixed structure. **NoSQL** databases store data in flexible shapes — documents, key-value pairs, wide columns, or graphs. Neither is "better"; they solve different problems.

## 🧠 What is SQL?

**SQL** stands for **Structured Query Language** — the standard language for talking to **relational databases** (MySQL, PostgreSQL, Oracle, SQL Server). Data lives in **tables** (rows and columns) with a **fixed schema** you define up front.

```sql
-- A relational "users" table: every row has the same columns
CREATE TABLE users (
    id      INT PRIMARY KEY,
    name    VARCHAR(100) NOT NULL,
    email   VARCHAR(255) UNIQUE,
    city    VARCHAR(100)
);

SELECT name, email
FROM users
WHERE city = 'London'
ORDER BY name;
```

Tables can be **linked** by keys (relationships), which is why they're called *relational*. A user has many orders; an order belongs to one user.

## 🧠 What is NoSQL?

**NoSQL** ("Not Only SQL") is an umbrella for databases that **don't** use rigid tables. The most common is the **document** model (MongoDB), where each record is a flexible JSON-like document — records in the same collection can even have different fields.

```json
// A MongoDB "users" document — nested and schema-flexible
{
  "_id": "u_123",
  "name": "Ada",
  "email": "ada@example.com",
  "address": { "city": "London", "zip": "EC1" },
  "hobbies": ["chess", "cycling"]
}
```

## ⚖️ SQL vs NoSQL

| Aspect | SQL (Relational) | NoSQL (e.g. Document) |
|---|---|---|
| **Data model** | Tables (rows & columns) | Documents, key-value, wide-column, graph |
| **Schema** | Fixed, defined up front | Flexible / dynamic |
| **Relationships** | Joins across tables | Nested/embedded data or references |
| **Query language** | SQL (standardized) | Per-database APIs (e.g. MongoDB Query) |
| **Scaling** | Usually **vertical** (bigger server) | Usually **horizontal** (more servers) |
| **Consistency** | Strong, **ACID** transactions | Often **BASE** / eventual consistency |
| **Best when** | Structured data & complex queries | Huge scale, evolving or varied data |
| **Examples** | MySQL, PostgreSQL, Oracle | MongoDB, Redis, Cassandra, Neo4j |

**ACID** (SQL): Atomicity, Consistency, Isolation, Durability — rock-solid guarantees, great for money and orders.
**BASE** (many NoSQL): Basically Available, Soft state, Eventual consistency — trades strictness for scale and speed.

## 🧭 When to use which

- **Reach for SQL** when data is **structured and related** (users, orders, payments), you need **complex queries and joins**, and correctness matters (banking, e-commerce).
- **Reach for NoSQL** when data is **huge, fast-changing, or loosely structured** (event logs, product catalogs, real-time feeds, caching), or you need to **scale horizontally** across many machines.
- Many real systems use **both** — SQL for core transactions, NoSQL for caching, search, or analytics. This is called *polyglot persistence*.

## 🐬 Wait — what about "MySQL"?

People often mix up **SQL** and **MySQL**. Short version:

- **SQL** = the **language** you write queries in.
- **MySQL** = a specific **relational database product** that *uses* SQL.

There's a whole page on this in **SQL vs MySQL** — but the one-liner is: *SQL is the language; MySQL, PostgreSQL, and Oracle are databases that speak it.*

## 🗺️ What's next

The rest of this track goes deep on **SQL** — the language you'll be tested on most:

1. **SQL Basics** — `SELECT`, `FROM`, `WHERE`, `ORDER BY`
2. **Filtering & Operators** — `LIKE`, `IN`, `BETWEEN`, `NULL`
3. **Joins** — combining tables
4. **Grouping & Aggregation** — `GROUP BY`, `COUNT`, `SUM`
5. **Built-in Functions** — string, number, and date helpers
6. **Tables & Constraints** — `CREATE TABLE`, keys, `CHECK`
7. **Insert / Update / Delete** — changing data
8. **Subqueries** — queries inside queries
9. **Advanced & Complex Queries** — indexes, views, transactions, window functions, CTEs

…and a dedicated **NoSQL Explained** page plus **SQL vs MySQL**.

## ❓ FAQs

### Is NoSQL faster than SQL?
Not inherently. NoSQL can be faster for simple lookups at massive scale because it avoids joins and can spread data across many servers. But for complex, related queries, a well-indexed SQL database is often faster and simpler. "Faster" always depends on the access pattern.

### Can NoSQL databases do transactions?
Increasingly, yes. Classic NoSQL favored speed and availability over strict transactions, but modern engines like MongoDB now support multi-document ACID transactions. Still, if transactions across many entities are central to your app, a relational database is usually the more natural fit.

### What does "schema-less" really mean?
It means the database doesn't force every record to have the same fields, so you can evolve your data shape without a migration. It does **not** mean "no structure" — your application still expects certain fields, so you effectively manage the schema in code instead of in the database.

### Which should I learn first for interviews?
Learn **SQL** first — it's asked in almost every backend and full-stack interview, and the concepts (tables, joins, indexing) transfer everywhere. Add NoSQL (especially MongoDB) once you're comfortable, since many modern stacks use both.

### What is horizontal vs vertical scaling?
**Vertical scaling** means making one server more powerful (more CPU/RAM) — simple but has a ceiling. **Horizontal scaling** means adding more servers and splitting data across them (sharding) — harder to coordinate but nearly limitless. SQL traditionally scales vertically; many NoSQL systems are built to scale horizontally.
