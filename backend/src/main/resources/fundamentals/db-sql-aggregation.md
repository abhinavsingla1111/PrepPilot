# 📊 Aggregation & GROUP BY

> Turn many rows into meaningful summaries — counts, totals, and averages.

## 🧠 What & Why

Sometimes you don't want individual rows — you want a **summary**. *"How many employees per department?"* *"What's the average salary?"* *"What's the total order revenue per customer?"* Those are **aggregate** questions, answered with aggregate functions and `GROUP BY`.

An aggregate function collapses a set of rows into a **single value**. `GROUP BY` splits the table into buckets first, then the aggregate runs **once per bucket**. Get this mental model — *bucket, then summarize* — and grouping stops being mysterious.

Schema: `employees(id, name, department_id, salary, hire_date, manager_id)`, `departments(id, name, location)`, `orders(id, customer_id, amount, order_date)`.

## 🔑 Key Concepts

- **COUNT** — how many rows (or non-NULL values).
- **SUM** — total of a numeric column.
- **AVG** — average value.
- **MIN / MAX** — smallest / largest value.
- **GROUP BY** — split rows into groups before aggregating.
- **HAVING** — filter **groups** after aggregation (vs `WHERE`, which filters **rows** before).

## 💻 Aggregates without grouping

```sql
-- One summary row for the whole table
SELECT COUNT(*)      AS total_employees,
       AVG(salary)   AS average_salary,
       MAX(salary)   AS highest_salary,
       MIN(salary)   AS lowest_salary,
       SUM(salary)   AS total_payroll
FROM employees;
```

## 💻 GROUP BY one column

```sql
-- Employee count and average salary per department
SELECT department_id,
       COUNT(*)    AS headcount,
       AVG(salary) AS avg_salary
FROM employees
GROUP BY department_id;
```

## 💻 GROUP BY multiple columns

```sql
-- Break down by department AND manager
SELECT department_id, manager_id, COUNT(*) AS team_size
FROM employees
GROUP BY department_id, manager_id;
```

## 💻 WHERE vs HAVING (the crucial interview point)

`WHERE` filters **individual rows before** they are grouped. `HAVING` filters **whole groups after** aggregation. Because aggregates like `COUNT(*)` only exist *after* grouping, you can't use them in `WHERE` — you must use `HAVING`.

```sql
-- WHERE first (drop low earners), THEN group, THEN HAVING (keep big teams)
SELECT department_id,
       COUNT(*)    AS headcount,
       AVG(salary) AS avg_salary
FROM employees
WHERE salary > 40000            -- row-level filter, runs first
GROUP BY department_id
HAVING COUNT(*) >= 3            -- group-level filter, runs after
ORDER BY avg_salary DESC;
```

| | **WHERE** | **HAVING** |
|---|---|---|
| Filters | Individual rows | Groups |
| Runs | Before `GROUP BY` | After `GROUP BY` |
| Can use aggregates? | No | Yes |
| Can use plain columns? | Yes | Yes (if grouped) |
| Typical use | `salary > 40000` | `COUNT(*) > 3` |

## 💻 COUNT variants

```sql
-- COUNT(*)          : counts ALL rows, including NULLs
-- COUNT(col)        : counts rows where col IS NOT NULL
-- COUNT(DISTINCT c) : counts distinct non-NULL values
SELECT COUNT(*)                    AS all_rows,
       COUNT(manager_id)           AS rows_with_manager,
       COUNT(DISTINCT department_id) AS distinct_departments
FROM employees;
```

This matters: `COUNT(*)` and `COUNT(manager_id)` differ whenever some `manager_id` values are `NULL`.

## 💻 Grouping with joins

```sql
-- Total revenue per department by joining orders through employees
SELECT d.name AS department,
       COUNT(o.id)   AS order_count,
       SUM(o.amount) AS revenue
FROM departments AS d
JOIN employees AS e ON e.department_id = d.id
JOIN orders    AS o ON o.customer_id = e.id
GROUP BY d.name
HAVING SUM(o.amount) > 10000
ORDER BY revenue DESC;
```

## ⚠️ Common Pitfalls

- **Selecting an ungrouped column** — every non-aggregated column in `SELECT` should appear in `GROUP BY` (MySQL may allow it, but the value is arbitrary and misleading).
- **Putting an aggregate in `WHERE`** — `WHERE COUNT(*) > 3` is an error; use `HAVING`.
- **`COUNT(col)` ignores NULLs** — if you want *all* rows, use `COUNT(*)`.
- **`AVG` ignores NULLs too** — the average is over non-NULL values only, which may not be what you expect.
- **Forgetting `GROUP BY`** while mixing aggregates and plain columns produces confusing single-row output.

## ❓ FAQs

### What's the difference between WHERE and HAVING?
`WHERE` filters raw rows **before** grouping and cannot see aggregate results, while `HAVING` filters the grouped results **after** aggregation and *can* reference aggregates like `COUNT(*)` — so use `WHERE salary > 40000` to drop rows and `HAVING COUNT(*) > 3` to drop small groups.

### What is the difference between COUNT(*) and COUNT(column)?
`COUNT(*)` counts every row regardless of NULLs, whereas `COUNT(column)` counts only rows where that column is **not NULL** — so if 5 of 20 employees have a NULL `manager_id`, `COUNT(*)` is 20 but `COUNT(manager_id)` is 15.

### Can I use an aggregate function in the WHERE clause?
No, because `WHERE` runs before rows are grouped and aggregates don't exist yet; put aggregate conditions in `HAVING`, which is evaluated after `GROUP BY` — e.g. `HAVING SUM(amount) > 10000`.

### Do I have to list every selected column in GROUP BY?
As a rule yes — any column in `SELECT` that isn't wrapped in an aggregate should appear in `GROUP BY`; MySQL's older default sometimes allowed omitting them, but the returned value is arbitrary, so enabling `ONLY_FULL_GROUP_BY` (the modern default) enforces the safe behavior.

### How do NULLs affect AVG and SUM?
Both `AVG` and `SUM` simply **ignore** NULL values, so `AVG(salary)` divides the total by the count of non-NULL salaries only; if you want NULLs treated as zero, wrap the column with `COALESCE(salary, 0)` first.
