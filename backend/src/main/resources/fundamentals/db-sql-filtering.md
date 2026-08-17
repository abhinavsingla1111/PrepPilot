# 🎯 Filtering with WHERE

> Keep the rows you want, drop the rest — precisely.

## 🧠 What & Why

`WHERE` is how you narrow a query down to just the rows you care about. Without it, `SELECT` returns the whole table. With it, you can ask sharp questions: *"employees earning over 80k,"* *"orders placed last month,"* *"managers in the New York office."*

`WHERE` evaluates a **condition** for every row and keeps the row only when the condition is **TRUE**. Rows where the condition is `FALSE` — *or* `NULL` (unknown) — are dropped. That `NULL` behavior trips up a lot of people, so we'll dig into it.

We keep using `employees(id, name, department_id, salary, hire_date, manager_id)`, `departments(id, name, location)`, and `orders(id, customer_id, amount, order_date)`.

## 🔑 Key Concepts

- **Comparison operators** — `=`, `<>` (not equal), `>`, `<`, `>=`, `<=`.
- **Logical operators** — `AND`, `OR`, `NOT`, and their precedence.
- **IN / NOT IN** — match against a list of values.
- **BETWEEN** — an inclusive range shortcut.
- **LIKE** — pattern matching with `%` and `_` wildcards.
- **IS NULL / IS NOT NULL** — the *only* correct way to test for NULL.

## 💻 Comparison operators

```sql
-- Employees earning exactly 50000
SELECT name FROM employees WHERE salary = 50000;

-- Employees NOT in department 3 (<> means "not equal"; != also works)
SELECT name FROM employees WHERE department_id <> 3;

-- Everyone earning at least 60000
SELECT name, salary FROM employees WHERE salary >= 60000;
```

## 💻 AND / OR / NOT and precedence

```sql
-- Both conditions must be true
SELECT name FROM employees
WHERE salary > 50000 AND department_id = 2;

-- Either condition can be true
SELECT name FROM employees
WHERE department_id = 1 OR department_id = 2;

-- AND binds tighter than OR — parentheses make intent explicit
SELECT name FROM employees
WHERE (department_id = 1 OR department_id = 2)
  AND salary > 70000;

-- NOT flips a condition
SELECT name FROM employees
WHERE NOT department_id = 3;
```

`AND` is evaluated before `OR`, just like `×` before `+` in math. When mixing them, **always use parentheses** to say exactly what you mean.

## 💻 IN and NOT IN

```sql
-- Cleaner than a chain of ORs
SELECT name FROM employees
WHERE department_id IN (1, 2, 5);

-- Exclude several values at once
SELECT name FROM employees
WHERE department_id NOT IN (3, 4);
```

## 💻 BETWEEN (inclusive)

```sql
-- Salaries from 50000 to 80000, endpoints included
SELECT name, salary FROM employees
WHERE salary BETWEEN 50000 AND 80000;

-- Works for dates too
SELECT id, order_date FROM orders
WHERE order_date BETWEEN '2024-01-01' AND '2024-03-31';
```

## 💻 LIKE with wildcards

```sql
-- % matches any number of characters
SELECT name FROM employees WHERE name LIKE 'A%';   -- starts with A
SELECT name FROM employees WHERE name LIKE '%son'; -- ends with "son"
SELECT name FROM employees WHERE name LIKE '%an%'; -- contains "an"

-- _ matches exactly one character
SELECT name FROM employees WHERE name LIKE 'J_n';  -- Jan, Jon, Jen...
```

| Pattern | Matches |
|---|---|
| `'A%'` | Anything starting with `A` |
| `'%z'` | Anything ending with `z` |
| `'%mid%'` | Contains `mid` anywhere |
| `'_a%'` | Second letter is `a` |

## 💻 NULL handling

```sql
-- Top-level employees have no manager (NULL manager_id)
SELECT name FROM employees WHERE manager_id IS NULL;

-- Everyone who DOES report to someone
SELECT name FROM employees WHERE manager_id IS NOT NULL;
```

## ⚠️ The NULL gotcha

`NULL` means *"unknown,"* not *"zero"* or *"empty."* Comparing anything to an unknown yields **unknown**, never TRUE — so these return **no rows**, even for rows that clearly have a NULL:

```sql
-- WRONG: never matches, because NULL = NULL is UNKNOWN, not TRUE
SELECT name FROM employees WHERE manager_id = NULL;

-- RIGHT: use IS NULL
SELECT name FROM employees WHERE manager_id IS NULL;
```

A subtler trap: `NOT IN` with a NULL in the list. If any listed value is NULL, `NOT IN` can silently return **nothing**, because the comparison becomes unknown for every row.

## ⚠️ Common Pitfalls

- **`= NULL` instead of `IS NULL`** — the classic; always use `IS NULL` / `IS NOT NULL`.
- **Forgetting parentheses** when mixing `AND` and `OR` changes the meaning entirely.
- **`NOT IN (... NULL ...)`** can return zero rows — prefer `NOT EXISTS` when NULLs are possible.
- **`LIKE` without wildcards** is just `=`; `LIKE 'Amy'` matches only the exact string.
- **Case sensitivity** depends on the column's collation; MySQL's default is usually case-insensitive.

## ❓ FAQs

### Why does `WHERE column = NULL` return no rows?
Because in SQL's three-valued logic any comparison with `NULL` evaluates to `UNKNOWN` (not TRUE), and `WHERE` keeps only rows that are TRUE, so `= NULL` filters everything out; the correct test is `IS NULL`, which is specifically designed to detect the unknown value.

### What's the difference between BETWEEN and using two comparisons?
`salary BETWEEN 50000 AND 80000` is exactly equivalent to `salary >= 50000 AND salary <= 80000` — it's **inclusive** on both ends and just shorter to read; the only gotcha is remembering that the lower bound must come first.

### When should I use IN versus OR?
Use `IN (1, 2, 5)` when checking one column against several values — it's shorter and clearer than `department_id = 1 OR department_id = 2 OR department_id = 5`; reach for `OR` only when the conditions involve *different* columns.

### What do `%` and `_` mean in LIKE?
`%` matches any sequence of zero or more characters and `_` matches exactly one character, so `'J_n%'` matches "Jane", "Jon Smith", etc.; to match a literal `%` or `_`, escape it (`LIKE '50\%'`).

### How is NOT different from `<>`?
`<>` (or `!=`) compares two values for inequality on a single expression, while `NOT` negates an entire boolean condition — so `NOT (salary > 100 AND bonus > 10)` flips the whole compound test, something `<>` can't do.
