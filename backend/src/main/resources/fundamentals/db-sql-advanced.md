# 🚀 Advanced & Complex Queries

> Indexes, views, transactions, CTEs, and window functions — the tools that separate juniors from seniors.

## 🧠 What & Why

Once you're comfortable with `SELECT`, joins, and grouping, the next level is about **performance, correctness, and expressiveness**. Indexes make queries fast. Transactions keep data consistent. CTEs and window functions let you write powerful analytics — running totals, rankings, "top N per group" — that would be painful or impossible otherwise.

This is the flagship doc: we'll cover each concept briefly, then combine everything into realistic "complex query" examples that interviewers actually ask about.

Schema: `employees(id, name, department_id, salary, hire_date, manager_id)`, `departments(id, name, location)`, `orders(id, customer_id, amount, order_date)`.

## 🔑 Key Concepts

- **Index** — a lookup structure that speeds up reads (at the cost of slower writes).
- **View** — a saved query you can treat like a table.
- **Transaction** — a group of changes that all succeed or all fail (ACID).
- **Normalization** — organizing tables to reduce redundancy (1NF/2NF/3NF).
- **UNION** — stack the results of two queries.
- **CTE (WITH)** — a named temporary result for readability.
- **Window function** — compute across a set of rows *without* collapsing them.

## 💻 Indexes

```sql
-- Without an index, MySQL scans every row to find matches.
-- An index on department_id makes lookups and joins on it fast.
CREATE INDEX idx_emp_department ON employees(department_id);

-- Composite index helps queries filtering by department AND sorting by salary
CREATE INDEX idx_emp_dept_salary ON employees(department_id, salary);

-- See how MySQL plans a query (look for "ref"/"range" vs "ALL" scan)
EXPLAIN SELECT * FROM employees WHERE department_id = 2;
```

Indexes speed up `WHERE`, `JOIN`, and `ORDER BY` on the indexed columns, but they **slow down** `INSERT`/`UPDATE`/`DELETE` (the index must be maintained) and use extra storage — so index the columns you filter/join on, not every column.

## 💻 Views

```sql
-- A view is a named, reusable query. It stores no data itself.
CREATE VIEW high_earners AS
SELECT e.name, e.salary, d.name AS department
FROM employees AS e
JOIN departments AS d ON e.department_id = d.id
WHERE e.salary > 90000;

-- Query it like a table
SELECT * FROM high_earners ORDER BY salary DESC;
```

## 💻 Transactions & ACID

```sql
-- Transfer logic: both updates succeed, or neither does
START TRANSACTION;

UPDATE employees SET salary = salary - 1000 WHERE id = 1;
UPDATE employees SET salary = salary + 1000 WHERE id = 2;

COMMIT;    -- make changes permanent
-- ROLLBACK;  -- ...or undo everything if something went wrong
```

**ACID** = **A**tomicity (all-or-nothing), **C**onsistency (rules stay valid), **I**solation (concurrent transactions don't corrupt each other), **D**urability (committed data survives crashes).

## 💻 Normalization (briefly)

```sql
-- 1NF: atomic columns, no repeating groups (don't store "phone1, phone2, phone3")
-- 2NF: 1NF + every non-key column depends on the WHOLE primary key
-- 3NF: 2NF + no non-key column depends on another non-key column
--   e.g. storing department_name on employees violates 3NF —
--   it depends on department_id, so keep it in departments instead.
```

## 💻 UNION vs UNION ALL

```sql
-- UNION removes duplicate rows (does extra work to de-dupe)
SELECT name FROM employees WHERE department_id = 1
UNION
SELECT name FROM employees WHERE salary > 90000;

-- UNION ALL keeps duplicates — faster when you know there are none
SELECT name FROM employees WHERE department_id = 1
UNION ALL
SELECT name FROM employees WHERE department_id = 2;
```

## 💻 CTEs (WITH)

```sql
-- A CTE names a subquery up front, making complex queries readable
WITH dept_avg AS (
  SELECT department_id, AVG(salary) AS avg_salary
  FROM employees
  GROUP BY department_id
)
SELECT e.name, e.salary, dept_avg.avg_salary
FROM employees AS e
JOIN dept_avg ON e.department_id = dept_avg.department_id
WHERE e.salary > dept_avg.avg_salary;
```

## 💻 Window functions

Window functions compute a value **across a set of related rows** (a "window") while **keeping every row** — unlike `GROUP BY`, which collapses them.

```sql
-- Rank employees by salary WITHIN each department, keeping all rows
SELECT name, department_id, salary,
  ROW_NUMBER() OVER (PARTITION BY department_id ORDER BY salary DESC) AS rn,
  RANK()       OVER (PARTITION BY department_id ORDER BY salary DESC) AS rnk,
  DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) AS dense_rnk
FROM employees;

-- LAG/LEAD peek at neighboring rows (previous/next salary)
SELECT name, salary,
  LAG(salary)  OVER (ORDER BY hire_date) AS prev_hire_salary,
  LEAD(salary) OVER (ORDER BY hire_date) AS next_hire_salary
FROM employees;
```

`ROW_NUMBER` always gives unique numbers; `RANK` leaves gaps after ties (1,1,3); `DENSE_RANK` doesn't (1,1,2).

## 💻 Complex example 1 — second highest salary per department

```sql
-- Rank within each department, then keep the #2 in each
WITH ranked AS (
  SELECT name, department_id, salary,
         DENSE_RANK() OVER (PARTITION BY department_id ORDER BY salary DESC) AS rnk
  FROM employees
)
SELECT department_id, name, salary
FROM ranked
WHERE rnk = 2;
```

## 💻 Complex example 2 — top 3 orders per customer

```sql
-- Number each customer's orders from largest amount, keep the top 3
WITH ranked_orders AS (
  SELECT customer_id, id AS order_id, amount,
         ROW_NUMBER() OVER (PARTITION BY customer_id ORDER BY amount DESC) AS rn
  FROM orders
)
SELECT customer_id, order_id, amount
FROM ranked_orders
WHERE rn <= 3
ORDER BY customer_id, amount DESC;
```

## 💻 Complex example 3 — running monthly revenue

```sql
-- Roll orders up to monthly totals, then a cumulative running total over time
WITH monthly AS (
  SELECT DATE_FORMAT(order_date, '%Y-%m') AS month,
         SUM(amount)                       AS revenue
  FROM orders
  GROUP BY DATE_FORMAT(order_date, '%Y-%m')
)
SELECT month,
       revenue,
       SUM(revenue) OVER (ORDER BY month) AS running_total
FROM monthly
ORDER BY month;
```

## ⚠️ Common Pitfalls

- **Over-indexing** — every index slows writes and costs storage; index deliberately.
- **Forgetting to COMMIT** — an open transaction can hold locks and block other queries.
- **`UNION` when you meant `UNION ALL`** — the de-dupe step is wasted work if duplicates are impossible.
- **Mismatched `UNION` columns** — both queries must have the same number and compatible types of columns.
- **Confusing `RANK` and `ROW_NUMBER`** — ties behave differently; pick the one your problem needs.
- **Window functions can't go in `WHERE`** — filter them in an outer query or CTE (as shown above).

## ❓ FAQs

### What is an index and when does it help?
An index is an auxiliary data structure (usually a B-tree) that lets MySQL find rows without scanning the whole table, dramatically speeding up `WHERE`, `JOIN`, and `ORDER BY` on the indexed columns; the trade-off is slower writes and extra storage, so you index columns you frequently search or join on rather than every column.

### What's the difference between UNION and UNION ALL?
`UNION` combines two result sets **and removes duplicate rows**, which requires an extra sorting/hashing step, while `UNION ALL` simply concatenates them and keeps duplicates — so use `UNION ALL` when you know the sets don't overlap (or duplicates are acceptable) because it's noticeably faster.

### How do RANK, DENSE_RANK, and ROW_NUMBER differ?
`ROW_NUMBER` assigns a unique sequential number to every row regardless of ties; `RANK` gives tied rows the same number but then **skips** the next values (1, 1, 3); and `DENSE_RANK` gives tied rows the same number **without gaps** (1, 1, 2) — pick `DENSE_RANK` for "Nth highest" problems where ties should share a place.

### What does ACID mean and why do transactions matter?
ACID stands for **Atomicity** (all statements in a transaction succeed or none do), **Consistency** (constraints stay valid), **Isolation** (concurrent transactions don't see each other's half-done work), and **Durability** (committed changes survive a crash); transactions matter because multi-step operations like transfers must not leave data half-updated.

### When should I use a CTE versus a subquery?
Functionally a non-recursive CTE (`WITH`) and a derived-table subquery are similar, but a CTE is defined once at the top with a name, so it's far more **readable**, can be **referenced multiple times** in the same query, and supports **recursion** for hierarchical data — reach for CTEs when a query has several stacked steps or repeated subqueries.
