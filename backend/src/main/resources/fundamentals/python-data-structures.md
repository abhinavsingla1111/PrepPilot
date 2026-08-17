# 🗃️ Python Data Structures

> The four workhorses — list, tuple, dict, and set — and how to pick the right one for the job.

## 🧠 What & Why

Most real programs are about moving data around: collecting items, looking things up, removing duplicates, keeping order. Python gives you four built-in structures that cover the vast majority of these needs, each tuned for a different pattern.

The trick in interviews (and real code) isn't just knowing *how* to use them — it's knowing *why* you'd pick one over another. That comes down to two questions: **Does order matter?** and **How fast do I need lookups?** A `dict` and `set` give near-instant lookups via hashing; a `list` keeps order but searching it is slow.

Get comfortable with their **Big-O** costs and you'll instantly reach for the right tool — turning an accidental O(n²) loop into a clean O(n) solution.

## 🔑 Key Concepts

- **list** — Ordered, mutable, allows duplicates. Your default "sequence of things."
- **tuple** — Ordered, **immutable**. Great for fixed records and dict keys.
- **dict** — Key → value mapping with fast lookups. Ordered since Python 3.7.
- **set** — Unordered collection of **unique** items with fast membership tests.
- **Comprehension** — A compact way to build a collection from a loop in one line.
- **Slicing** — Grab a sub-section of a sequence with `seq[start:stop:step]`.

## 💻 Example

```python
# --- list: ordered, mutable ---
nums = [3, 1, 2]
nums.append(4)          # [3, 1, 2, 4]
nums.sort()             # [1, 2, 3, 4]
print(nums[0])          # 1  (index access)

# --- tuple: ordered, immutable ---
point = (10, 20)
x, y = point            # unpacking
# point[0] = 99         # ❌ TypeError — tuples can't change

# --- dict: key -> value ---
user = {"name": "Sam", "age": 27}
print(user["name"])     # "Sam"
user["email"] = "a@b.c" # add a key
print("age" in user)    # True (fast key check)

# --- set: unique items ---
seen = {1, 2, 2, 3}     # {1, 2, 3} — duplicate dropped
seen.add(4)
print(3 in seen)        # True (fast membership)

# --- Comprehensions ---
squares = [n * n for n in range(5)]          # [0, 1, 4, 9, 16]
lengths = {w: len(w) for w in ["hi", "bye"]} # {'hi': 2, 'bye': 3}
evens = {n for n in range(10) if n % 2 == 0} # {0, 2, 4, 6, 8}

# --- Slicing ---
letters = ["a", "b", "c", "d", "e"]
print(letters[1:4])     # ['b', 'c', 'd']
print(letters[::-1])    # ['e', 'd', 'c', 'b', 'a']  (reversed)
```

## 📊 Big-O Cheat Sheet

| Operation | list | tuple | dict | set |
|---|---|---|---|---|
| Access by index | O(1) | O(1) | — | — |
| Access by key | — | — | O(1) avg | — |
| Search (`in`) | O(n) | O(n) | O(1) avg | O(1) avg |
| Insert / append | O(1)* | — (immutable) | O(1) avg | O(1) avg |
| Delete | O(n) | — (immutable) | O(1) avg | O(1) avg |
| Ordered? | ✅ | ✅ | ✅ (insertion) | ❌ |
| Mutable? | ✅ | ❌ | ✅ | ✅ |

*`append` is amortized O(1); inserting at the front is O(n).

## 🧭 When to Use Which

- **Need order and will modify it?** → `list`
- **Fixed group of values that shouldn't change?** → `tuple`
- **Looking things up by a key/name?** → `dict`
- **Only care about uniqueness or fast membership tests?** → `set`

## ⚠️ Common Pitfalls

- **Searching a big list with `in`** is O(n). If you do it repeatedly, convert to a `set` or `dict` first.
- **Using a mutable object (like a list) as a dict key** fails — keys must be hashable. Use a `tuple` instead.
- **Modifying a list while looping over it** causes skipped items or errors. Iterate over a copy.
- **Assuming sets keep order** — they don't. Use a `list` (or `dict`) if order matters.

## ❓ FAQs

### When would you use a tuple instead of a list?

Use a tuple when the collection is fixed and shouldn't change — like coordinates `(x, y)` or a database row. Immutability makes tuples hashable (so they can be dict keys or set members) and signals intent: "this data is a constant record."

### Why are dict and set lookups so much faster than list lookups?

Dicts and sets use **hashing**: each item's location is computed from its value, so finding it takes roughly constant time O(1). A list has to walk element by element to find a match, which is O(n). For frequent membership checks, that difference is huge.

### What's a comprehension and why is it preferred?

A comprehension builds a list, dict, or set in a single readable expression, e.g. `[x*x for x in range(5)]`. It's usually faster and clearer than an equivalent `for` loop with `.append()`, and it signals to readers "I'm transforming or filtering a collection."

### Are Python dictionaries ordered?

Yes — since Python 3.7, dictionaries preserve insertion order as a language guarantee. However, dicts are still meant for key-based lookup, not positional access, so you can't index them by number like a list.

### How does slicing work, and what does `[::-1]` do?

Slicing uses `sequence[start:stop:step]` to extract a portion of a list, tuple, or string. Omitted values default to the start, end, and step of 1. The idiom `[::-1]` uses a step of `-1` to walk the sequence backwards, giving you a reversed copy.
