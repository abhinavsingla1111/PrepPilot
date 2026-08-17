# 🐬 SQL vs MySQL

> They sound the same, but one is a language and the other is a product.

This is one of the most common "gotcha" interview questions, and the answer is simple once you see it: **SQL is a language; MySQL is a database that speaks that language.** Confusing them is like confusing *English* with *a specific book written in English*.

## 🧠 What & Why

**SQL (Structured Query Language)** is the **standard language** for defining and querying relational data. It's a specification — `SELECT`, `INSERT`, `JOIN`, `GROUP BY` all come from the SQL standard.

**MySQL** is a specific **Relational Database Management System (RDBMS)** — an actual piece of software (originally by MySQL AB, now owned by Oracle) that **stores** your data and **understands** SQL commands. PostgreSQL, Oracle, SQL Server, and SQLite are other RDBMSs that also speak SQL.

So when you "write MySQL," you're really writing **SQL** that the **MySQL** engine executes.

## 🔑 Key Concepts

- **SQL** — the query language / standard (ANSI/ISO). It rarely changes.
- **RDBMS** — software that stores relational data and runs SQL (MySQL, PostgreSQL, etc.).
- **Dialect** — each RDBMS adds its own extra syntax on top of standard SQL (e.g. MySQL's `LIMIT` vs SQL Server's `TOP`).
- **Engine** — MySQL's pluggable storage layer (InnoDB is the default; supports transactions & foreign keys).

## 📊 Side by side

| | **SQL** | **MySQL** |
|---|---|---|
| **What it is** | A language / standard | A database product (RDBMS) |
| **Purpose** | Express queries & schema | Store data and run SQL |
| **Changes** | Evolves slowly (standard) | Releases versions (5.7, 8.0, …) |
| **Made by** | ANSI/ISO committee | Oracle (originally MySQL AB) |
| **Examples** | `SELECT * FROM t` | The server running on port 3306 |
| **Alternatives** | — | PostgreSQL, Oracle, SQL Server |

## 💻 Example

The **same** standard SQL runs on MySQL, PostgreSQL, and others:

```sql
-- Standard SQL — works on virtually every relational database
SELECT city, COUNT(*) AS people
FROM users
GROUP BY city
HAVING COUNT(*) > 10;
```

But **dialects** differ for some features:

```sql
-- MySQL / PostgreSQL: page results
SELECT * FROM users ORDER BY id LIMIT 10 OFFSET 20;

-- SQL Server does the same thing differently:
SELECT TOP 10 * FROM users ORDER BY id;   -- (older style)
```

## ⚠️ Common Pitfalls

- Saying "MySQL is a language" — it isn't; **SQL** is the language.
- Assuming SQL is 100% portable — core queries are, but functions, `LIMIT`/`TOP`, auto-increment syntax, and data types vary by database.
- Confusing **MySQL** with **Microsoft SQL Server (MSSQL)** — different products from different companies.

## ❓ FAQs

### Is MySQL the same as SQL Server?
No. **MySQL** is an open-source database owned by Oracle. **SQL Server (MSSQL)** is a separate commercial database from Microsoft. They both use SQL, but they are different products with different tooling, pricing, and dialect quirks.

### If I learn MySQL, can I use PostgreSQL too?
Mostly yes. The core SQL you learn — `SELECT`, joins, `GROUP BY`, subqueries — transfers directly. You'll just adjust some vendor-specific bits like functions, `AUTO_INCREMENT` vs `SERIAL`, and certain data types. The concepts are identical.

### What is the difference between MySQL and PostgreSQL?
Both are open-source relational databases. PostgreSQL is known for strong standards-compliance and advanced features (rich types, window functions, extensibility); MySQL is known for speed, simplicity, and huge web adoption. For interviews, knowing that both are SQL-speaking RDBMSs with different strengths is usually enough.

### Why do people say "SQL database" instead of "relational database"?
It's a casual shorthand. Because relational databases are queried with SQL, people call them "SQL databases" — especially to contrast them with "NoSQL" databases. Technically the precise term is *relational database*, but "SQL database" is widely understood.
