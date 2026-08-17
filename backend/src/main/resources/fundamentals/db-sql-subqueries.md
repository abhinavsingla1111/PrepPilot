# 🪆 Subqueries

> A query inside a query — use one result to drive another.

## 🧠 What & Why

A **subquery** (or inner query) is a `SELECT` nested inside another statement. It lets you answer questions in steps: *"Who earns more than the company average?"* first needs the average, then compares each person to it. The subquery computes the average; the outer query does the comparison.

Subqueries appear in three main spots: in `SELECT` (a computed value), in `WHERE` (a filter), and in `FROM` (a temporary table, called a **derived table**). Knowing when a subquery is clean versus when a `JOIN` is faster is a favorite interview theme.

Schema: `employees(id, name, department_id, salary, hire_date, manager_id)`, `departments(id, name, location)`, `orders(id, customer_id, amount, order_date)`.

## 🔑 Key Concepts

- **Scalar subquery** — returns a single value (one row, one column).
- **Subquery in WHERE** — feeds `IN`, `=`, `>`, etc.
- **Correlated subquery** — references the outer row; runs once per outer row.
- **EXISTS** — tests whether the subquery returns any row at all.
- **Derived table** — a subquery in `FROM` used like a table.

## 💻 Scalar subquery

```sql
-- Compare each employee to the overall average salary
SELECT name, salary,
       (SELECT AVG(salary) FROM employees) AS company_avg
FROM employees
WHERE salary > (SELECT AVG(salary) FROM employees);
```

## 💻 Subquery in WHERE (IN and comparison)

```sql
-- Employees in departments located in New York
SELECT name
FROM employees
WHERE department_id IN (
  SELECT id FROM departments WHERE location = 'New York'
);

-- Employees earning more than the max salary in department 3
SELECT name, salary
FROM employees
WHERE salary > (
  SELECT MAX(salary) FROM employees WHERE department_id = 3
);
```

## 💻 Correlated subquery

A correlated subquery depends on the **outer** row, so it re-runs for each row the outer query considers. Here, for every employee we compute *their department's* average.

```sql
-- Employees paid above their OWN department's average
SELECT e.name, e.salary, e.department_id
FROM employees AS e
WHERE e.salary > (
  SELECT AVG(inner_e.salary)
  FROM employees AS inner_e
  WHERE inner_e.department_id = e.department_id   -- references outer e
);
```

## 💻 EXISTS vs IN

```sql
-- EXISTS: true as soon as one matching row is found (great with correlation)
SELECT d.name
FROM departments AS d
WHERE EXISTS (
  SELECT 1 FROM employees AS e WHERE e.department_id = d.id
);

-- IN: compares a value against a list of returned values
SELECT d.name
FROM departments AS d
WHERE d.id IN (SELECT department_id FROM employees);
```

Prefer `EXISTS` when the inner list could contain `NULL` (where `NOT IN` misbehaves) or when you just need "does at least one match exist?"

## 💻 ANY and ALL

```sql
-- > ANY : greater than at least one value (i.e. > the minimum)
SELECT name, salary FROM employees
WHERE salary > ANY (SELECT salary FROM employees WHERE department_id = 2);

-- > ALL : greater than every value (i.e. > the maximum)
SELECT name, salary FROM employees
WHERE salary > ALL (SELECT salary FROM employees WHERE department_id = 2);
```

## 💻 Derived table (subquery in FROM)

```sql
-- Treat a grouped summary as a table, then filter it
SELECT dept_stats.department_id, dept_stats.avg_salary
FROM (
  SELECT department_id, AVG(salary) AS avg_salary
  FROM employees
  GROUP BY department_id
) AS dept_stats
WHERE dept_stats.avg_salary > 70000;
```

## 💻 When a JOIN is better

```sql
-- Subquery version (fine, but re-evaluated logic)
SELECT name FROM employees
WHERE department_id IN (SELECT id FROM departments WHERE location = 'New York');

-- JOIN version — usually faster and lets you SELECT columns from BOTH tables
SELECT e.name, d.location
FROM employees AS e
JOIN departments AS d ON e.department_id = d.id
WHERE d.location = 'New York';
```

Use a **JOIN** when you need columns from both tables or when performance matters; use a **subquery** when the logic is naturally "compute this, then filter by it" and you don't need the inner table's columns.

## ⚠️ Common Pitfalls

- **Scalar subquery returning >1 row** — using `=` with a subquery that returns many rows errors; use `IN` or an aggregate.
- **Correlated subqueries can be slow** — they run per outer row; a JOIN or window function is often faster.
- **`NOT IN` with NULLs** returns no rows — switch to `NOT EXISTS`.
- **Derived tables need an alias** — MySQL requires `AS name` after a `FROM` subquery.
- **Over-nesting** — deeply nested subqueries are hard to read; CTEs (`WITH`) or joins are clearer.

## ❓ FAQs

### What is a correlated subquery and how is it different from a regular one?
A regular subquery runs **once** and its result is reused, while a **correlated** subquery references a column from the outer query and therefore re-executes **once per outer row** — like computing each employee's own department average — which is powerful but can be slow on large tables.

### When should I use EXISTS instead of IN?
Use `EXISTS` when you only need to know whether **any** matching row exists (it can stop at the first match and pairs naturally with correlated conditions) and especially when the inner query might produce NULLs, because `NOT IN` with a NULL silently returns no rows whereas `NOT EXISTS` behaves correctly.

### Is a JOIN faster than a subquery?
Often yes — the optimizer can usually execute a JOIN more efficiently than an equivalent correlated subquery, and a JOIN also lets you select columns from both tables; however, for simple "filter by a computed value" cases a subquery is perfectly fine and sometimes clearer, so measure rather than assume.

### What's the difference between ANY and ALL?
`> ANY (subquery)` is true if the value beats **at least one** returned value (equivalent to greater-than-the-minimum), while `> ALL (subquery)` is true only if it beats **every** returned value (greater-than-the-maximum) — flip the operator and the same logic applies to `<`, `=`, etc.

### Why must a subquery in FROM have an alias?
Because MySQL treats a `FROM` subquery as a **derived table** and every table in a query needs a name so its columns can be referenced; omitting the alias (`) AS dept_stats`) is a syntax error, even if you never use the alias elsewhere.
