# ✏️ Changing Data (DML)

> INSERT, UPDATE, and DELETE — how data gets in, changes, and goes away.

## 🧠 What & Why

**DML (Data Manipulation Language)** covers the commands that *change* what's stored: `INSERT` adds rows, `UPDATE` modifies them, and `DELETE` removes them. Where `SELECT` is read-only, DML actually writes to the database — so it deserves respect.

The single most important habit in DML is the `WHERE` clause on `UPDATE` and `DELETE`. Forget it, and you don't change one row — you change **every** row. Interviewers love asking about this because it's a real, career-defining mistake.

Schema: `employees(id, name, department_id, salary, hire_date, manager_id)`, `departments(id, name, location)`, `orders(id, customer_id, amount, order_date)`.

## 🔑 Key Concepts

- **INSERT** — add one or many new rows.
- **INSERT ... SELECT** — copy rows from a query.
- **UPDATE** — change values in existing rows.
- **DELETE** — remove rows (filtered).
- **TRUNCATE** — remove all rows fast.
- **UPSERT** — insert, or update if it already exists.

## 💻 INSERT (single row)

```sql
-- Column list + matching values (recommended: explicit and safe)
INSERT INTO departments (name, location)
VALUES ('Engineering', 'New York');
```

## 💻 INSERT (multiple rows)

```sql
-- One statement, many rows — faster than many separate inserts
INSERT INTO employees (name, department_id, salary, hire_date)
VALUES
  ('Ada Lovelace',  1, 95000, '2023-01-15'),
  ('Alan Turing',   1, 98000, '2022-11-01'),
  ('Grace Hopper',  2, 88000, '2024-03-20');
```

## 💻 INSERT ... SELECT

```sql
-- Populate an archive table from a query result
INSERT INTO orders_archive (id, customer_id, amount, order_date)
SELECT id, customer_id, amount, order_date
FROM orders
WHERE order_date < '2023-01-01';
```

## 💻 UPDATE (always with WHERE)

```sql
-- Give one employee a raise — the WHERE targets exactly one row
UPDATE employees
SET salary = salary * 1.10
WHERE id = 42;

-- Update multiple columns at once
UPDATE employees
SET department_id = 3,
    manager_id    = 7
WHERE id = 42;

-- Conditional bulk raise for a whole department
UPDATE employees
SET salary = salary + 5000
WHERE department_id = 1 AND salary < 90000;
```

## 💻 DELETE (always with WHERE)

```sql
-- Remove a specific stale order
DELETE FROM orders
WHERE id = 1001;

-- Remove all orders older than a cutoff
DELETE FROM orders
WHERE order_date < '2020-01-01';
```

## 💻 TRUNCATE

```sql
-- Wipe an entire table instantly (no WHERE, resets AUTO_INCREMENT)
TRUNCATE TABLE orders_archive;
```

## 💻 UPSERT (INSERT ... ON DUPLICATE KEY UPDATE)

```sql
-- If the PK/UNIQUE key already exists, update instead of erroring.
-- Requires a UNIQUE or PRIMARY KEY constraint to detect the "duplicate".
INSERT INTO departments (id, name, location)
VALUES (1, 'Engineering', 'Boston')
ON DUPLICATE KEY UPDATE
  location = VALUES(location);
```

## ⚠️ The danger of a missing WHERE

```sql
-- 😱 DISASTER: no WHERE means EVERY employee's salary is set to 1
UPDATE employees SET salary = 1;

-- 😱 DISASTER: this deletes the ENTIRE table's rows
DELETE FROM employees;
```

Habits that save you:
- **Write the `WHERE` first**, then the `SET`.
- **Preview with a `SELECT`** using the same `WHERE` before running the `UPDATE`/`DELETE`.
- **Wrap risky changes in a transaction** so you can `ROLLBACK` a mistake.

```sql
-- Safety pattern: preview, then act
SELECT * FROM employees WHERE id = 42;   -- verify it's the right row
UPDATE employees SET salary = 99000 WHERE id = 42;
```

## ⚠️ Common Pitfalls

- **`UPDATE`/`DELETE` without `WHERE`** — the flagship catastrophe; affects all rows.
- **Assuming rows come back** — DML returns a count of affected rows, not the rows themselves.
- **`INSERT` column/value mismatch** — the count and order of columns must match the values.
- **`ON DUPLICATE KEY UPDATE` needs a key** — without a UNIQUE/PRIMARY constraint it just inserts and can create duplicates.
- **`TRUNCATE` bypasses `ON DELETE` triggers/foreign-key row checks** and can't be easily undone.

## ❓ FAQs

### What happens if I run UPDATE or DELETE without a WHERE clause?
The statement applies to **every row** in the table — `UPDATE employees SET salary = 1` sets all salaries to 1 and `DELETE FROM employees` empties the table — which is why you should preview with a matching `SELECT` and wrap risky changes in a transaction you can `ROLLBACK`.

### What is an UPSERT and how do I do it in MySQL?
An UPSERT inserts a row, or updates it if a row with the same primary/unique key already exists; in MySQL you write `INSERT ... ON DUPLICATE KEY UPDATE ...`, which relies on a `UNIQUE` or `PRIMARY KEY` constraint to detect the conflict and then applies the `UPDATE` part instead of erroring.

### What's the difference between DELETE and TRUNCATE?
`DELETE` removes rows one at a time, honors a `WHERE` filter, fires triggers, and can be rolled back; `TRUNCATE` deallocates the whole table's data at once, ignores `WHERE`, resets `AUTO_INCREMENT`, and is much faster but effectively irreversible.

### Can I insert data selected from another table?
Yes — `INSERT INTO target (cols) SELECT cols FROM source WHERE ...` copies query results directly into another table without round-tripping through your application, which is ideal for archiving, backfilling, or duplicating filtered data.

### How do I update one column based on another table's value?
Use a multi-table `UPDATE` with a join: `UPDATE employees e JOIN departments d ON e.department_id = d.id SET e.location = d.location WHERE ...`, which lets each row take its new value from the matched row in the other table.
