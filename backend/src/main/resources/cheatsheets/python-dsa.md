# 🐍 Python DSA Cheat Sheet

> Quick reference for coding interviews. Skim the tables, copy the snippets.

---

## 🔢 Built-in Data Types

| Type    | Example         | Notes                                  |
| ------- | --------------- | -------------------------------------- |
| `int`   | `42`            | Arbitrary precision (no overflow)      |
| `float` | `3.14`          | 64-bit double                          |
| `bool`  | `True` / `False`| Subclass of `int` (`True == 1`)        |
| `str`   | `"abc"`         | Immutable, Unicode                     |
| `None`  | `None`          | The null value                         |
| `list`  | `[1, 2, 3]`     | Dynamic array, mutable                 |
| `tuple` | `(1, 2)`        | Immutable sequence                     |
| `dict`  | `{"a": 1}`      | Hash map, insertion-ordered            |
| `set`   | `{1, 2, 3}`     | Unordered unique elements              |

```python
big = 10 ** 100        # ints never overflow
pi  = 3.14
flag = True
INF = float('inf')     # +infinity
NINF = float('-inf')   # -infinity
```

---

## 🔄 Type Conversion & Casting

```python
int("42")        # 42
int(3.99)        # 3   (truncates toward zero)
float("3.14")    # 3.14
str(42)          # "42"
list("abc")      # ['a', 'b', 'c']
"".join(['a','b'])   # "abc"

# char <-> code point
ord('a')         # 97
chr(97)          # 'a'
ord('7') - ord('0')   # 7  (digit value)

# base conversions
bin(5)           # '0b101'
int('101', 2)    # 5
```

> ⚠️ `int()` truncates toward zero; `5 // 2 == 2` but `-5 // 2 == -3` (floor division).

---

## 🔡 Strings (immutable)

```python
s = "abcd"
len(s)           # 4
s[0]             # 'a'
s[-1]            # 'd'
s[1:3]           # 'bc'   (slice)
s[::-1]          # 'dcba' (reverse)

s.upper()  s.lower()
s.strip()  s.split(",")
",".join(["a", "b"])     # "a,b"
s.find("bc")     # index or -1
s.replace("a", "x")
s.isdigit()  s.isalpha()  s.isalnum()

# build strings efficiently
parts = []
parts.append("x")
result = "".join(parts)   # O(n), not += in a loop
```

---

## 📋 Lists (dynamic array)

```python
a = [1, 2, 3]
a.append(4)          # add to end        O(1)
a.pop()              # remove last       O(1)
a.pop(0)             # remove first      O(n)
a.insert(0, 9)       # insert at index   O(n)
a.remove(2)          # remove by value   O(n)
a[1] = 10            # set
len(a); a[-1]

a[1:3]               # slice
a[::-1]              # reversed copy
a.sort()             # in place
b = sorted(a)        # new sorted list
a.reverse()
sum(a); max(a); min(a)

# 2D list (grid) — avoid the aliasing trap
grid = [[0] * cols for _ in range(rows)]   # correct
# grid = [[0] * cols] * rows               # WRONG: shared rows

# handy
nums = [0] * n                  # fixed-size
idx = a.index(10)               # first index
a.count(1)                      # occurrences
```

---

## 🔗 Tuples & Unpacking

```python
p = (3, 4)
x, y = p             # unpack
a, b = b, a          # swap
first, *rest = [1, 2, 3]   # first=1, rest=[2,3]

# tuples as dict keys / set elements (hashable)
seen = {(0, 0), (1, 2)}
```

---

## 🗺️ Dictionaries

```python
d = {}
d["a"] = 1                 # insert / update
d.get("b")                 # None if missing
d.get("b", 0)              # default
d.setdefault("c", []).append(1)
"a" in d                   # key check   O(1)
del d["a"]
d.keys(); d.values(); d.items()

for k, v in d.items():
    print(k, v)

# frequency count
from collections import Counter
freq = Counter("aabbbc")   # {'b':3,'a':2,'c':1}
freq.most_common(2)        # [('b',3),('a',2)]

# grouping
from collections import defaultdict
adj = defaultdict(list)
adj[1].append(2)           # no KeyError
```

---

## 🎯 Sets

```python
s = set()
s.add(1)
s.discard(1)       # no error if missing
s.remove(1)        # KeyError if missing
1 in s             # O(1)

a, b = {1,2,3}, {2,3,4}
a | b              # union {1,2,3,4}
a & b              # intersection {2,3}
a - b              # difference {1}
a ^ b              # symmetric diff {1,4}

seen = set(nums)   # dedupe
```

---

## 🧰 collections & heapq

```python
from collections import deque, Counter, defaultdict

# deque — O(1) both ends (use as stack OR queue)
dq = deque()
dq.append(1); dq.appendleft(0)
dq.pop(); dq.popleft()
dq[0]              # peek front

# heapq — MIN-heap only
import heapq
h = []
heapq.heappush(h, 5)
heapq.heappop(h)       # smallest       O(log n)
h[0]                   # peek min        O(1)
heapq.heapify(nums)    # list -> heap   O(n)

# MAX-heap: push negatives
heapq.heappush(h, -x); -heapq.heappop(h)

# k smallest / largest
heapq.nsmallest(3, nums)
heapq.nlargest(3, nums)
```

---

## ⚖️ Sorting & Comparators

```python
nums.sort()                       # ascending, in place
nums.sort(reverse=True)           # descending
s = sorted(nums)                  # returns new list

# by key
words.sort(key=len)
pairs.sort(key=lambda p: p[1])            # by 2nd element
pairs.sort(key=lambda p: (p[0], -p[1]))   # multi-key

# custom comparator
from functools import cmp_to_key
nums.sort(key=cmp_to_key(lambda a, b: a - b))
```

---

## ➗ Math & Numbers

```python
7 // 2           # 3   floor division
7 % 3            # 1   modulo
divmod(7, 3)     # (2, 1)
2 ** 10          # 1024
abs(-5); round(3.5)
pow(2, 10, 1000) # (2^10) % 1000  fast modular pow

import math
math.gcd(12, 8)  # 4
math.sqrt(16)    # 4.0
math.floor(3.7)  # 3
math.ceil(3.2)   # 4
math.inf         # infinity
```

---

## 🔁 Comprehensions & Iteration

```python
squares = [x * x for x in range(5)]
evens   = [x for x in nums if x % 2 == 0]
grid    = [[0] * c for _ in range(r)]
lookup  = {x: i for i, x in enumerate(nums)}
uniq    = {x for x in nums}

for i, x in enumerate(nums):      # index + value
    ...
for a, b in zip(list1, list2):    # parallel
    ...
for i in range(len(nums) - 1, -1, -1):  # reverse
    ...
```

---

## ⚠️ Important — Easy to Forget

- **Mutable default args** — never use `def f(x=[])`. The list is shared across calls. Use `def f(x=None): x = x or []`.
- **`is` vs `==`** — `==` compares values, `is` compares identity. Use `==` except for `None` (`if x is None`).
- **Copying** — `b = a` aliases the same list. Use `a.copy()`, `a[:]`, or `copy.deepcopy` for nested.
- **2D grid trap** — `[[0]*c]*r` makes `r` references to ONE row. Use a comprehension.
- **Integer division** — `//` floors toward negative infinity: `-7 // 2 == -4`.
- **`dict`/`set` need hashable keys** — lists can't be keys; use tuples.
- **Default `sort` is stable** — equal elements keep their order.
- **String `+=` in a loop is O(n²)** — collect in a list and `"".join()`.
