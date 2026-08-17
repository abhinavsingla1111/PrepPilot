# 🧰 Built-in Functions

> Ready-made tools to transform strings, numbers, dates, and handle NULLs.

## 🧠 What & Why

MySQL ships with dozens of **scalar functions** — functions that take a value (or a few) and return a single value, row by row. They let you reshape data right inside a query: format a name, round a price, extract a year, or provide a fallback when something is NULL.

Unlike aggregate functions (which collapse many rows into one), scalar functions run **once per row** and leave the row count unchanged. We'll group them into four families: string, numeric, date/time, and conditional.

Schema: `employees(id, name, department_id, salary, hire_date, manager_id)`, `departments(id, name, location)`, `orders(id, customer_id, amount, order_date)`.

## 🔑 Key Concepts

- **String functions** — manipulate text.
- **Numeric functions** — round and compute numbers.
- **Date/time functions** — extract and shift dates.
- **Conditional functions** — branch logic and handle NULLs.

## 💻 String functions

```sql
SELECT
  CONCAT(name, ' (', department_id, ')') AS labelled,  -- glue strings
  LENGTH(name)                           AS name_len,  -- length in bytes
  UPPER(name)                            AS shout,      -- uppercase
  LOWER(name)                            AS quiet,      -- lowercase
  SUBSTRING(name, 1, 3)                  AS first_three,-- chars 1..3
  TRIM('  spaced  ')                     AS trimmed,    -- strip spaces
  REPLACE(name, 'a', '@')                AS leetspeak   -- swap substrings
FROM employees;
```

| Function | Purpose | Example → Result |
|---|---|---|
| `CONCAT(a, b)` | Join strings | `CONCAT('Ada',' L')` → `Ada L` |
| `LENGTH(s)` | Byte length | `LENGTH('cat')` → `3` |
| `UPPER/LOWER(s)` | Change case | `UPPER('hi')` → `HI` |
| `SUBSTRING(s,p,n)` | Extract part | `SUBSTRING('hello',1,3)` → `hel` |
| `TRIM(s)` | Remove edges spaces | `TRIM(' x ')` → `x` |
| `REPLACE(s,a,b)` | Swap text | `REPLACE('a-b','-','_')` → `a_b` |

## 💻 Numeric functions

```sql
SELECT
  ROUND(salary / 12, 2) AS monthly_pay,  -- round to 2 decimals
  CEIL(salary / 1000)   AS k_ceiling,    -- round up
  FLOOR(salary / 1000)  AS k_floor,      -- round down
  ABS(-42)              AS absolute,     -- magnitude
  MOD(id, 2)            AS even_or_odd,  -- remainder (id % 2)
  POWER(2, 10)          AS two_pow_ten   -- exponent
FROM employees;
```

| Function | Purpose | Example → Result |
|---|---|---|
| `ROUND(x, d)` | Round to d decimals | `ROUND(3.146, 2)` → `3.15` |
| `CEIL(x)` | Round up | `CEIL(4.1)` → `5` |
| `FLOOR(x)` | Round down | `FLOOR(4.9)` → `4` |
| `ABS(x)` | Absolute value | `ABS(-7)` → `7` |
| `MOD(a, b)` | Remainder | `MOD(10, 3)` → `1` |
| `POWER(a, b)` | a to the b | `POWER(3, 2)` → `9` |

## 💻 Date/time functions

```sql
SELECT
  NOW()                              AS timestamp_now, -- date + time
  CURDATE()                          AS today,         -- date only
  DATE(order_date)                   AS just_date,     -- strip time
  YEAR(order_date)                   AS yr,
  MONTH(order_date)                  AS mo,
  DAY(order_date)                    AS dy,
  DATEDIFF(CURDATE(), order_date)    AS days_ago,      -- day difference
  DATE_ADD(order_date, INTERVAL 30 DAY) AS due_date,   -- add time
  DATE_SUB(CURDATE(), INTERVAL 1 MONTH) AS last_month,
  DATE_FORMAT(order_date, '%Y-%m')   AS ym             -- custom format
FROM orders;
```

| Function | Purpose | Example → Result |
|---|---|---|
| `NOW()` | Current date+time | `2024-06-01 09:30:00` |
| `CURDATE()` | Current date | `2024-06-01` |
| `YEAR/MONTH/DAY(d)` | Extract part | `YEAR('2024-06-01')` → `2024` |
| `DATEDIFF(a, b)` | Days between | `DATEDIFF('2024-06-10','2024-06-01')` → `9` |
| `DATE_ADD(d, INTERVAL n UNIT)` | Shift forward | `+30 DAY` |
| `DATE_FORMAT(d, fmt)` | Format string | `%Y-%m` → `2024-06` |

## 💻 Conditional functions

```sql
SELECT name, salary,
  -- IF(condition, then, else)
  IF(salary > 80000, 'senior', 'junior') AS tier,

  -- CASE for multiple branches
  CASE
    WHEN salary >= 100000 THEN 'A'
    WHEN salary >= 60000  THEN 'B'
    ELSE 'C'
  END AS pay_band,

  -- COALESCE: first non-NULL value
  COALESCE(manager_id, 0)   AS manager_or_zero,

  -- IFNULL: two-argument NULL fallback
  IFNULL(manager_id, -1)    AS mgr_fallback,

  -- NULLIF: returns NULL if the two args are equal
  NULLIF(salary, 0)         AS salary_or_null
FROM employees;
```

| Function | Purpose | Behavior |
|---|---|---|
| `IF(c, a, b)` | Inline branch | `a` if `c` true, else `b` |
| `CASE WHEN` | Multi-branch | First matching `THEN`, else `ELSE` |
| `COALESCE(a, b, …)` | First non-NULL | Scans args left→right |
| `IFNULL(a, b)` | NULL fallback | `a` unless NULL, then `b` |
| `NULLIF(a, b)` | Guard equality | `NULL` if `a = b`, else `a` |

## ⚠️ Common Pitfalls

- **`LENGTH` vs `CHAR_LENGTH`** — `LENGTH` counts **bytes**, so multi-byte characters inflate it; use `CHAR_LENGTH` for character count.
- **`CONCAT` with NULL** — if any argument is NULL, the whole result is NULL; wrap with `COALESCE`.
- **`SUBSTRING` is 1-indexed** — the first character is position `1`, not `0`.
- **Date string formats** — MySQL expects `'YYYY-MM-DD'`; other formats may parse unexpectedly.
- **`NULLIF` misuse** — great for avoiding divide-by-zero (`amount / NULLIF(count, 0)`), but easy to forget.

## ❓ FAQs

### What's the difference between COALESCE and IFNULL?
Both return a fallback for NULL, but `IFNULL(a, b)` takes exactly **two** arguments while `COALESCE(a, b, c, …)` accepts **many** and returns the first non-NULL — `COALESCE` is standard SQL and more flexible, whereas `IFNULL` is a MySQL-specific two-argument shortcut.

### When should I use CASE instead of IF?
Use `IF` for a single true/false branch and `CASE WHEN` when you have **multiple** conditions or want portable, standard SQL — `CASE` reads like a mini if/elseif/else chain and works across all databases, while `IF` is a MySQL convenience.

### How do I get just the year or month from a date?
Use the extraction functions `YEAR(order_date)`, `MONTH(order_date)`, and `DAY(order_date)`, or format flexibly with `DATE_FORMAT(order_date, '%Y-%m')` when you want a combined string like `2024-06` for grouping monthly reports.

### Why is CONCAT returning NULL?
Because `CONCAT` propagates NULL — if any argument is NULL the entire result becomes NULL; either wrap each nullable piece in `COALESCE(col, '')` or use `CONCAT_WS(separator, …)`, which skips NULL arguments instead of nullifying the whole string.

### How do I avoid a divide-by-zero error?
Wrap the divisor in `NULLIF(divisor, 0)` so that a zero becomes NULL and the division yields NULL instead of an error — e.g. `total / NULLIF(order_count, 0)` — then optionally `COALESCE` the result to a default.
