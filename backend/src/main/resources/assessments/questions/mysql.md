# MySQL — 100 Interview MCQs

> Format: Each question has a difficulty tag `[Easy] / [Medium] / [Hard]`, options (or True/False, or one-word), the correct answer, and a one-line justification. Assumes MySQL 8.x where relevant.

---

## Section A — Easy (Q1–Q35)

**Q1. [Easy]** Which SQL command retrieves data from a table?

- A) GET
- B) SELECT
- C) FETCH
- D) READ

**Answer:** B — `SELECT` retrieves rows from tables.

**Q2. [Easy]** Which clause filters rows in a query?

- A) ORDER BY
- B) WHERE
- C) GROUP BY
- D) HAVING

**Answer:** B — `WHERE` filters rows before grouping.

**Q3. [Easy] (One word)** The command to remove all rows quickly without logging each deletion is **\_\_**.

**Answer:** TRUNCATE — `TRUNCATE` fast-empties a table.
  
**Q4. [Easy]** Which statement adds a new row?

- A) ADD
- B) INSERT
- C) UPDATE
- D) CREATE

**Answer:** B — `INSERT` adds new rows.

**Q5. [Easy] (True/False)** SQL keywords are case-insensitive in MySQL.

**Answer:** True — `SELECT` and `select` are equivalent.

**Q6. [Easy]** Which clause sorts the result set?

- A) SORT
- B) ORDER BY
- C) GROUP BY
- D) ARRANGE

**Answer:** B — `ORDER BY` sorts rows.

**Q7. [Easy]** The default sort direction of `ORDER BY` is:

- A) DESC
- B) ASC
- C) Random
- D) None

**Answer:** B — Ascending (`ASC`) is the default.

**Q8. [Easy] (One word)** The keyword to eliminate duplicate rows in a SELECT is **\_\_**.

**Answer:** DISTINCT — It returns unique rows.

**Q9. [Easy]** Which data type stores variable-length strings?

- A) CHAR
- B) VARCHAR
- C) INT
- D) BLOB

**Answer:** B — `VARCHAR` stores variable-length text.

**Q10. [Easy]** Which command modifies existing rows?

- A) INSERT
- B) UPDATE
- C) ALTER
- D) MODIFY

**Answer:** B — `UPDATE` changes existing data.

**Q11. [Easy] (True/False)** A PRIMARY KEY uniquely identifies each row.

**Answer:** True — It enforces uniqueness and non-null.

**Q12. [Easy]** A PRIMARY KEY column:

- A) Allows NULLs
- B) Cannot contain NULLs and must be unique
- C) Allows duplicates
- D) Is always text

**Answer:** B — Primary keys are unique and NOT NULL.

**Q13. [Easy]** Which clause groups rows for aggregation?

- A) WHERE
- B) GROUP BY
- C) ORDER BY
- D) JOIN

**Answer:** B — `GROUP BY` groups rows for aggregate functions.

**Q14. [Easy] (One word)** The aggregate function that counts rows is **\_\_**.

**Answer:** COUNT — `COUNT()` returns row counts.

**Q15. [Easy]** Which removes a table entirely (structure + data)?

- A) DELETE
- B) DROP TABLE
- C) TRUNCATE
- D) REMOVE

**Answer:** B — `DROP TABLE` deletes the table and its definition.

**Q16. [Easy]** Which operator checks a range?

- A) IN
- B) BETWEEN
- C) LIKE
- D) EXISTS

**Answer:** B — `BETWEEN a AND b` checks an inclusive range.

**Q17. [Easy] (True/False)** `LIKE '%abc%'` matches any string containing "abc".

**Answer:** True — `%` is a wildcard for any sequence of characters.

**Q18. [Easy]** Which wildcard matches exactly one character in `LIKE`?

- A) %
- B) \_
- C) \*
- D) ?

**Answer:** B — Underscore `_` matches a single character.

**Q19. [Easy]** Which clause filters groups after aggregation?

- A) WHERE
- B) HAVING
- C) GROUP BY
- D) ON

**Answer:** B — `HAVING` filters aggregated groups.

**Q20. [Easy] (One word)** The default storage engine in MySQL 8 is **\_\_**.

**Answer:** InnoDB — InnoDB is the default engine.

**Q21. [Easy]** Which JOIN returns only matching rows in both tables?

- A) LEFT JOIN
- B) INNER JOIN
- C) RIGHT JOIN
- D) FULL JOIN

**Answer:** B — `INNER JOIN` returns matches from both sides.

**Q22. [Easy]** Which returns all rows from the left table plus matches from the right?

- A) INNER JOIN
- B) LEFT JOIN
- C) RIGHT JOIN
- D) CROSS JOIN

**Answer:** B — `LEFT JOIN` keeps all left rows.

**Q23. [Easy] (True/False)** `NULL` represents an unknown or missing value.

**Answer:** True — NULL means absence of a value.

**Q24. [Easy]** How do you test for NULL?

- A) = NULL
- B) IS NULL
- C) == NULL
- D) EQUALS NULL

**Answer:** B — Use `IS NULL` (comparisons with `=` fail on NULL).

**Q25. [Easy]** Which command adds a column to an existing table?

- A) UPDATE TABLE
- B) ALTER TABLE ... ADD
- C) MODIFY TABLE
- D) INSERT COLUMN

**Answer:** B — `ALTER TABLE ... ADD COLUMN` modifies schema.

**Q26. [Easy] (One word)** The clause to limit the number of returned rows in MySQL is **\_\_**.

**Answer:** LIMIT — `LIMIT n` caps result rows.

**Q27. [Easy]** Which returns the number of rows: `SELECT COUNT(*) FROM t`?

- A) Sum of a column
- B) Total row count
- C) Distinct rows
- D) NULL count

**Answer:** B — `COUNT(*)` counts all rows.

**Q28. [Easy]** Which aggregate returns the largest value?

- A) MIN
- B) MAX
- C) SUM
- D) AVG

**Answer:** B — `MAX()` returns the maximum.

**Q29. [Easy] (True/False)** `AUTO_INCREMENT` generates sequential numeric values automatically.

**Answer:** True — It auto-generates increasing IDs.

**Q30. [Easy]** Which keyword combines two result sets and removes duplicates?

- A) UNION ALL
- B) UNION
- C) JOIN
- D) INTERSECT

**Answer:** B — `UNION` merges and deduplicates.

**Q31. [Easy]** Which keeps duplicates when combining result sets?

- A) UNION
- B) UNION ALL
- C) DISTINCT
- D) EXCEPT

**Answer:** B — `UNION ALL` retains duplicates and is faster.

**Q32. [Easy] (One word)** A column that references the primary key of another table is a **\_\_** key.

**Answer:** Foreign — Foreign keys enforce referential integrity.

**Q33. [Easy]** Which data type is best for whole numbers?

- A) VARCHAR
- B) INT
- C) DATE
- D) TEXT

**Answer:** B — `INT` stores integers.

**Q34. [Easy]** Which comment syntax is valid in MySQL?

- A) // comment
- B) -- comment (with space) or # comment
- C) <!-- comment -->
- D) /comment/

**Answer:** B — MySQL supports `-- `, `#`, and `/* */`.

**Q35. [Easy] (True/False)** `WHERE` is evaluated before `GROUP BY`.

**Answer:** True — Row filtering happens before grouping.

---

## Section B — Medium (Q36–Q75)

**Q36. [Medium]** What is the logical execution order of these clauses?

- A) SELECT → FROM → WHERE → GROUP BY
- B) FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY
- C) WHERE → FROM → SELECT
- D) ORDER BY → SELECT → FROM

**Answer:** B — MySQL processes FROM/WHERE/GROUP BY/HAVING before SELECT/ORDER BY.

**Q37. [Medium]** Output logic: `SELECT COUNT(col) FROM t` where col has NULLs:

- A) Counts all rows including NULLs
- B) Counts only non-NULL values of col
- C) Errors
- D) Counts NULLs only

**Answer:** B — `COUNT(col)` ignores NULLs (unlike `COUNT(*)`).

**Q38. [Medium] (One word)** The JOIN that produces a Cartesian product is a **\_\_** join.

**Answer:** CROSS — `CROSS JOIN` pairs every row with every other.

**Q39. [Medium]** Which is TRUE about `WHERE` vs `HAVING`?

- A) Both filter rows before grouping
- B) WHERE filters rows; HAVING filters groups (can use aggregates)
- C) HAVING filters raw rows
- D) They are identical

**Answer:** B — `HAVING` applies after aggregation and can reference aggregates.

**Q40. [Medium]** A subquery in the `FROM` clause is called a:

- A) Correlated subquery
- B) Derived table (inline view)
- C) CTE
- D) View

**Answer:** B — A subquery in FROM is a derived table.

**Q41. [Medium]** A correlated subquery:

- A) Runs once independently
- B) References the outer query and re-evaluates per outer row
- C) Is always faster
- D) Cannot use WHERE

**Answer:** B — It depends on outer-row values, running per row.

**Q42. [Medium] (True/False)** A CTE is defined using the `WITH` keyword.

**Answer:** True — Common Table Expressions start with `WITH`.

**Q43. [Medium]** The difference between CHAR and VARCHAR is:

- A) CHAR is variable-length
- B) CHAR is fixed-length (padded); VARCHAR is variable-length
- C) They are identical
- D) VARCHAR is numeric

**Answer:** B — CHAR pads to fixed size; VARCHAR stores actual length.

**Q44. [Medium]** Which index type does InnoDB use for the primary key?

- A) Hash index
- B) Clustered B+ tree index
- C) Bitmap index
- D) Full-text index

**Answer:** B — InnoDB stores rows in a clustered B+ tree keyed by the primary key.

**Q45. [Medium]** A secondary (non-clustered) index in InnoDB stores:

- A) The full row
- B) The indexed columns plus the primary key value
- C) Only row offsets
- D) Nothing

**Answer:** B — Secondary indexes reference the primary key for lookups.

**Q46. [Medium] (One word)** A lookup from a secondary index back to the clustered index to fetch other columns is called a **\_\_** lookup.

**Answer:** back — A "index back" / bookmark lookup (row lookup via PK).

**Q47. [Medium]** Which improves query performance for `WHERE col = ?`?

- A) Dropping indexes
- B) Adding an index on col
- C) Using SELECT \*
- D) More NULLs

**Answer:** B — An index on the filtered column speeds lookups.

**Q48. [Medium]** A covering index is one that:

- A) Covers all tables
- B) Contains all columns a query needs (no table lookup)
- C) Is always unique
- D) Covers NULLs

**Answer:** B — The query is satisfied entirely from the index.

**Q49. [Medium]** What does `EXPLAIN` do?

- A) Executes and returns rows
- B) Shows the query execution plan
- C) Repairs a table
- D) Deletes rows

**Answer:** B — `EXPLAIN` reveals how MySQL plans to run a query.

**Q50. [Medium] (True/False)** Wrapping an indexed column in a function (e.g., `WHERE YEAR(col)=2020`) can prevent index use.

**Answer:** True — Function-wrapped columns are usually non-sargable.

**Q51. [Medium]** Which isolation level is InnoDB's default?

- A) READ UNCOMMITTED
- B) READ COMMITTED
- C) REPEATABLE READ
- D) SERIALIZABLE

**Answer:** C — InnoDB defaults to REPEATABLE READ.

**Q52. [Medium]** A "dirty read" occurs at which isolation level?

- A) READ UNCOMMITTED
- B) REPEATABLE READ
- C) SERIALIZABLE
- D) READ COMMITTED

**Answer:** A — READ UNCOMMITTED allows reading uncommitted changes.

**Q53. [Medium] (One word)** The transaction property ensuring all-or-nothing execution is **\_\_**.

**Answer:** Atomicity — The "A" in ACID.

**Q54. [Medium]** The ACID properties are:

- A) Atomicity, Consistency, Isolation, Durability
- B) Accuracy, Concurrency, Integrity, Data
- C) Atomicity, Concurrency, Isolation, Data
- D) Availability, Consistency, Integrity, Durability

**Answer:** A — ACID = Atomicity, Consistency, Isolation, Durability.

**Q55. [Medium]** Which statement commits a transaction?

- A) SAVE
- B) COMMIT
- C) END
- D) FLUSH

**Answer:** B — `COMMIT` makes changes permanent.

**Q56. [Medium] (True/False)** `ROLLBACK` undoes changes since the last commit/savepoint.

**Answer:** True — It reverts uncommitted work.

**Q57. [Medium]** Which join returns rows even when there's no match on the right side?

- A) INNER JOIN
- B) LEFT JOIN
- C) CROSS JOIN
- D) EQUI JOIN

**Answer:** B — `LEFT JOIN` fills unmatched right columns with NULL.

**Q58. [Medium]** Output logic: `SELECT * FROM a LEFT JOIN b ON a.id=b.id WHERE b.id IS NULL` returns:

- A) Matching rows
- B) Rows in a with no match in b (anti-join)
- C) All rows in b
- D) Cartesian product

**Answer:** B — It finds left rows without a right match.

**Q59. [Medium] (One word)** A named, stored SELECT query you can reuse like a table is a **\_\_**.

**Answer:** View — Views encapsulate queries as virtual tables.

**Q60. [Medium]** Which is TRUE about `TRUNCATE` vs `DELETE`?

- A) TRUNCATE can use WHERE
- B) TRUNCATE resets AUTO_INCREMENT and is faster; DELETE is row-by-row and can use WHERE
- C) DELETE is always faster
- D) They are identical

**Answer:** B — TRUNCATE is a fast DDL-like reset; DELETE is logged DML.

**Q61. [Medium]** Which function returns the current date and time?

- A) GETDATE()
- B) NOW()
- C) SYSTIME()
- D) TODAY()

**Answer:** B — `NOW()` returns the current datetime in MySQL.

**Q62. [Medium] (True/False)** A UNIQUE constraint allows one NULL (or multiple NULLs in MySQL) but no duplicate non-null values.

**Answer:** True — MySQL permits multiple NULLs in a UNIQUE index but forbids duplicate values.

**Q63. [Medium]** `GROUP_CONCAT` is used to:

- A) Join tables
- B) Concatenate values from a group into one string
- C) Count groups
- D) Sort groups

**Answer:** B — It aggregates group values into a delimited string.

**Q64. [Medium]** Which returns the second-highest salary safely?

- A) MAX(salary)
- B) SELECT MAX(salary) FROM emp WHERE salary < (SELECT MAX(salary) FROM emp)
- C) MIN(salary)
- D) COUNT(salary)

**Answer:** B — It finds the max below the overall max (second highest).

**Q65. [Medium] (One word)** The clause to page results by skipping rows in MySQL is `LIMIT ... ______`.

**Answer:** OFFSET — `LIMIT n OFFSET m` paginates results.

**Q66. [Medium]** Which is TRUE about `ONLY_FULL_GROUP_BY` (MySQL 8 default)?

- A) It's disabled by default
- B) Non-aggregated SELECT columns must appear in GROUP BY
- C) It allows any columns
- D) It disables GROUP BY

**Answer:** B — Selected non-aggregated columns must be grouped or functionally dependent.

**Q67. [Medium]** A composite index on `(a, b)` can efficiently serve a query filtering on:

- A) Only b
- B) a alone, or a and b (leftmost prefix)
- C) Only c
- D) Any column order

**Answer:** B — The leftmost-prefix rule requires leading column `a`.

**Q68. [Medium] (True/False)** `SELECT *` can hurt performance and prevent covering-index use.

**Answer:** True — It fetches unneeded columns and may force row lookups.

**Q69. [Medium]** The `IN` subquery `WHERE id IN (SELECT ...)` vs `EXISTS`:

- A) Always identical performance
- B) EXISTS can be more efficient for correlated existence checks
- C) IN is always faster
- D) EXISTS returns rows

**Answer:** B — `EXISTS` short-circuits on the first match for correlated checks.

**Q70. [Medium]** Which normal form removes partial dependencies on a composite key?

- A) 1NF
- B) 2NF
- C) 3NF
- D) BCNF

**Answer:** B — 2NF eliminates partial key dependencies.

**Q71. [Medium] (One word)** 3NF removes **\_\_** dependencies (non-key attributes depending on other non-key attributes).

**Answer:** transitive — 3NF removes transitive dependencies.

**Q72. [Medium]** Which is a benefit of denormalization?

- A) Less redundancy
- B) Faster reads at the cost of redundancy/write complexity
- C) Perfect integrity
- D) Smaller storage always

**Answer:** B — It trades redundancy for read performance.

**Q73. [Medium]** A stored procedure differs from a function in that:

- A) A function must return a value and can be used in expressions
- B) They are identical
- C) A procedure always returns a value
- D) Functions cannot take parameters

**Answer:** A — Functions return values usable in SQL; procedures perform actions.

**Q74. [Medium] (True/False)** A trigger executes automatically in response to INSERT/UPDATE/DELETE events.

**Answer:** True — Triggers fire on DML events.

**Q75. [Medium]** Which clause in an INSERT handles duplicate-key conflicts by updating?

- A) INSERT IGNORE
- B) INSERT ... ON DUPLICATE KEY UPDATE
- C) REPLACE only
- D) MERGE

**Answer:** B — It updates the existing row on a unique/primary key conflict.

---

## Section C — Hard (Q76–Q100)

**Q76. [Hard]** In InnoDB REPEATABLE READ, non-locking SELECTs use:

- A) Table locks
- B) Consistent snapshots via MVCC (multi-version concurrency control)
- C) Dirty reads
- D) No concurrency

**Answer:** B — MVCC provides a consistent read view without locking.

**Q77. [Hard]** A "phantom read" is prevented in InnoDB REPEATABLE READ largely by:

- A) Row locks only
- B) Next-key (gap) locks
- C) Table locks
- D) Nothing

**Answer:** B — Next-key locks lock ranges to block phantom inserts.

**Q78. [Hard] (One word)** The InnoDB lock that combines a record lock and a gap lock is the **\_\_**-key lock.

**Answer:** next — Next-key locking prevents phantoms.

**Q79. [Hard]** A deadlock in InnoDB is resolved by:

- A) Hanging forever
- B) Detecting the cycle and rolling back one transaction (victim)
- C) Committing both
- D) Ignoring it

**Answer:** B — InnoDB detects deadlocks and rolls back the smaller transaction.

**Q80. [Hard]** Output logic: `SELECT 1 UNION SELECT 1` returns how many rows?

- A) 2
- B) 1
- C) 0
- D) Error

**Answer:** B — `UNION` removes duplicates, yielding a single row.

**Q81. [Hard]** Why might `WHERE indexed_col = '5'` (col is INT) still work but risk issues?

- A) It errors
- B) Implicit type conversion may allow index use, but string/number mismatches can cause full scans or wrong results
- C) It always uses the index
- D) It ignores the column

**Answer:** B — Implicit conversions can defeat indexes or cause surprising comparisons.

**Q82. [Hard] (True/False)** The InnoDB clustered index means table data is physically ordered by the primary key.

**Answer:** True — Rows are stored in primary-key order within the B+ tree.

**Q83. [Hard]** Choosing a random UUID as a primary key in InnoDB can hurt performance because:

- A) UUIDs are too short
- B) Random inserts cause page splits and fragmentation in the clustered index
- C) They can't be indexed
- D) They are strings only

**Answer:** B — Non-sequential keys scatter inserts, hurting locality.

**Q84. [Hard]** Window function `ROW_NUMBER() OVER (PARTITION BY dept ORDER BY salary DESC)`:

- A) Sums salaries
- B) Assigns a unique sequential rank within each department
- C) Groups rows
- D) Deletes duplicates

**Answer:** B — It numbers rows per partition by the ordering.

**Q85. [Hard] (One word)** The window function that leaves gaps in ranking after ties is **\_\_**().

**Answer:** RANK — `RANK()` skips numbers after ties (unlike `DENSE_RANK`).

**Q86. [Hard]** Difference between `RANK()` and `DENSE_RANK()`:

- A) None
- B) RANK skips ranks after ties; DENSE_RANK does not
- C) DENSE_RANK skips
- D) Both skip

**Answer:** B — DENSE_RANK produces consecutive ranks without gaps.

**Q87. [Hard]** Output logic: `SELECT NULL = NULL;` returns:

- A) 1 (true)
- B) NULL
- C) 0 (false)
- D) Error

**Answer:** B — Comparing NULL with `=` yields NULL, not true.

**Q88. [Hard]** To safely compare possibly-NULL values for equality, use:

- A) =
- B) <=> (NULL-safe equal)
- C) ==
- D) IS

**Answer:** B — The `<=>` operator treats NULL = NULL as true.

**Q89. [Hard] (True/False)** `COUNT(*)` and `COUNT(1)` generally perform the same in modern MySQL.

**Answer:** True — The optimizer treats them equivalently.

**Q90. [Hard]** The query cache in MySQL 8 is:

- A) Enabled by default
- B) Removed entirely
- C) Only for InnoDB
- D) Faster than indexes

**Answer:** B — MySQL 8 removed the query cache.

**Q91. [Hard]** A "loose index scan" is used when:

- A) No index exists
- B) MySQL can skip large parts of an index for GROUP BY/DISTINCT on leading columns
- C) Only for full scans
- D) For text search

**Answer:** B — It jumps through the index efficiently for grouped/distinct queries.

**Q92. [Hard] (One word)** The isolation anomaly where a row read twice returns different values within one transaction is a non-**\_\_** read.

**Answer:** repeatable — Non-repeatable read (prevented by REPEATABLE READ).

**Q93. [Hard]** Why can a large `OFFSET` (e.g., `LIMIT 100000, 20`) be slow?

- A) It uses no index
- B) MySQL still scans and discards all skipped rows
- C) OFFSET is invalid
- D) It locks the table

**Answer:** B — Rows before the offset are read then thrown away (use keyset pagination).

**Q94. [Hard]** Keyset (seek) pagination improves on OFFSET by:

- A) Using OFFSET twice
- B) Filtering with `WHERE id > last_seen_id ORDER BY id LIMIT n`
- C) Scanning all rows
- D) Disabling indexes

**Answer:** B — It seeks directly using the last key, avoiding scans.

**Q95. [Hard] (True/False)** In MySQL, a transaction that only reads still gets a consistent snapshot under REPEATABLE READ.

**Answer:** True — The first consistent read establishes the snapshot.

**Q96. [Hard]** The InnoDB redo log ensures:

- A) Query caching
- B) Durability by replaying committed changes after a crash
- C) Faster reads
- D) Deduplication

**Answer:** B — Redo logs provide crash recovery (the "D" in ACID).

**Q97. [Hard]** The InnoDB undo log is used for:

- A) Durability only
- B) Rollback and MVCC consistent reads
- C) Indexing
- D) Replication only

**Answer:** B — Undo logs support transaction rollback and snapshot reads.

**Q98. [Hard] (One word)** The InnoDB in-memory structure that caches data and index pages is the buffer **\_\_**.

**Answer:** pool — The InnoDB buffer pool caches pages.

**Q99. [Hard]** Output logic: aggregating `SELECT dept, COUNT(*) FROM emp GROUP BY dept HAVING COUNT(*) > 5` returns:

- A) All departments
- B) Only departments with more than 5 employees
- C) Employees over 5 years
- D) Error

**Answer:** B — `HAVING` filters groups by the aggregate count.

**Q100. [Hard]** Which best explains a "covering index" advantage in a JOIN-heavy query?

- A) It joins faster by locking
- B) The index contains all needed columns, avoiding clustered-index (row) lookups
- C) It removes indexes
- D) It caches queries

**Answer:** B — Reading only the index avoids extra row fetches, boosting performance.
