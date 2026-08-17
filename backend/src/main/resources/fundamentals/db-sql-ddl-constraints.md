# 🏗️ DDL & Constraints

> Design the shape of your data and enforce the rules that keep it clean.

## 🧠 What & Why

So far we've *queried* data. **DDL (Data Definition Language)** is how you *define* it — creating tables, choosing data types, and altering structure. Alongside DDL come **constraints**: rules the database enforces automatically so bad data can never sneak in.

Constraints are your safety net. A `NOT NULL` guarantees a value exists; a `FOREIGN KEY` guarantees `employees.department_id` always points at a real department; a `UNIQUE` prevents duplicate emails. Let the database enforce correctness so your application doesn't have to.

Schema: `employees(id, name, department_id, salary, hire_date, manager_id)`, `departments(id, name, location)`, `orders(id, customer_id, amount, order_date)`.

## 🔑 Key Concepts

- **Data type** — what kind of value a column holds (number, text, date…).
- **CREATE TABLE** — define a new table.
- **ALTER TABLE** — change an existing table's structure.
- **DROP / TRUNCATE / DELETE** — remove a table, all rows, or some rows.
- **Constraint** — an enforced rule (PRIMARY KEY, FOREIGN KEY, UNIQUE, etc.).

## 💻 Common data types

```sql
-- INT       : whole numbers (~±2.1 billion)
-- BIGINT    : very large whole numbers
-- DECIMAL(p,s): exact decimals — use for money!
-- VARCHAR(n): variable text up to n chars
-- TEXT      : long text (no length limit needed)
-- DATE      : calendar date (YYYY-MM-DD)
-- DATETIME  : date + time
-- BOOLEAN   : true/false (stored as TINYINT in MySQL)
```

## 💻 CREATE TABLE

```sql
CREATE TABLE departments (
  id       INT AUTO_INCREMENT PRIMARY KEY,   -- surrogate key, auto-numbered
  name     VARCHAR(100) NOT NULL UNIQUE,      -- required and unique
  location VARCHAR(100) DEFAULT 'Remote'      -- default when omitted
);

CREATE TABLE employees (
  id            INT AUTO_INCREMENT PRIMARY KEY,
  name          VARCHAR(100) NOT NULL,
  department_id INT,
  salary        DECIMAL(10, 2) NOT NULL CHECK (salary >= 0),
  hire_date     DATE NOT NULL DEFAULT (CURRENT_DATE),
  manager_id    INT,
  FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL,
  FOREIGN KEY (manager_id)    REFERENCES employees(id)   ON DELETE SET NULL
);
```

## 💻 ALTER TABLE

```sql
-- Add a new column
ALTER TABLE employees ADD COLUMN email VARCHAR(255);

-- Change a column's type or attributes
ALTER TABLE employees MODIFY COLUMN salary DECIMAL(12, 2) NOT NULL;

-- Remove a column
ALTER TABLE employees DROP COLUMN email;

-- Add a constraint after the fact
ALTER TABLE employees ADD CONSTRAINT uq_name_dept UNIQUE (name, department_id);
```

## 💻 DROP vs TRUNCATE vs DELETE

```sql
-- DELETE: removes chosen rows; can be filtered and rolled back
DELETE FROM orders WHERE order_date < '2020-01-01';

-- TRUNCATE: instantly empties the table, resets AUTO_INCREMENT, no WHERE
TRUNCATE TABLE orders;

-- DROP: removes the entire table, structure and all
DROP TABLE orders;
```

| Command | Removes | WHERE? | Resets auto-increment | Structure kept |
|---|---|---|---|---|
| `DELETE` | Chosen rows | Yes | No | Yes |
| `TRUNCATE` | All rows | No | Yes | Yes |
| `DROP` | Whole table | No | n/a | No |

## 💻 Constraints in action

```sql
CREATE TABLE orders (
  id          INT AUTO_INCREMENT PRIMARY KEY,          -- unique row identity
  customer_id INT NOT NULL,                            -- must have a customer
  amount      DECIMAL(10, 2) NOT NULL CHECK (amount > 0), -- must be positive
  order_date  DATE NOT NULL DEFAULT (CURRENT_DATE),    -- defaults to today
  UNIQUE (customer_id, order_date),                    -- one order/customer/day
  FOREIGN KEY (customer_id) REFERENCES employees(id) ON DELETE CASCADE
);
```

## 📊 Constraints summary

| Constraint | Guarantees | Example |
|---|---|---|
| `PRIMARY KEY` | Unique + not NULL row identity | `id INT PRIMARY KEY` |
| `FOREIGN KEY` | Value points to another table's key | `REFERENCES departments(id)` |
| `UNIQUE` | No duplicate values | `email VARCHAR(255) UNIQUE` |
| `NOT NULL` | Value must be present | `name VARCHAR(100) NOT NULL` |
| `DEFAULT` | Fallback value if omitted | `location DEFAULT 'Remote'` |
| `CHECK` | Value passes a condition | `CHECK (salary >= 0)` |
| `AUTO_INCREMENT` | Auto-generated increasing number | `id INT AUTO_INCREMENT` |

**`ON DELETE` options** for foreign keys: `CASCADE` (delete children too), `SET NULL` (null out the reference), `RESTRICT`/`NO ACTION` (block the delete).

## ⚠️ Common Pitfalls

- **Using `FLOAT`/`DOUBLE` for money** — they're approximate; always use `DECIMAL` for currency.
- **`TRUNCATE` can't be rolled back** easily and ignores `WHERE` — it wipes everything.
- **Forgetting `ON DELETE`** — deleting a parent row may fail or orphan children depending on the rule.
- **`VARCHAR` too small** — inserts silently truncate (or error in strict mode); size for the real data.
- **Adding a `NOT NULL` column without a default** to a populated table will fail; supply a `DEFAULT`.

## ❓ FAQs

### What's the difference between DELETE, TRUNCATE, and DROP?
`DELETE` removes selected rows and can be filtered with `WHERE` and rolled back in a transaction; `TRUNCATE` instantly empties the whole table, resets `AUTO_INCREMENT`, and can't be filtered; `DROP` removes the entire table definition along with its data.

### When should I use a PRIMARY KEY versus a UNIQUE constraint?
A table has exactly **one** `PRIMARY KEY` that identifies each row and implicitly enforces both uniqueness and `NOT NULL`, while you can have **many** `UNIQUE` constraints and a UNIQUE column is allowed to contain a single NULL — use PRIMARY KEY for identity and UNIQUE for other "no duplicates" rules like email.

### What does ON DELETE CASCADE do?
It tells a foreign key to automatically delete the child rows when their parent row is deleted — so removing an employee also removes their orders; alternatives are `SET NULL` (blank the reference) and `RESTRICT` (block the parent delete while children exist).

### Why should I use DECIMAL instead of FLOAT for money?
Because `FLOAT`/`DOUBLE` store values in binary floating point and can't represent many decimal fractions exactly, causing rounding errors like `0.1 + 0.2 ≠ 0.3`; `DECIMAL(p, s)` stores exact base-10 values, which is essential for salaries and order amounts.

### What is AUTO_INCREMENT and can I reuse deleted IDs?
`AUTO_INCREMENT` makes MySQL assign the next increasing integer automatically to a column (typically the primary key) on each insert; by default deleted IDs are **not** reused because the counter only moves forward, and `TRUNCATE` is what resets it back to the start.
