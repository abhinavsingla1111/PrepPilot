# 🧱 Python Basics

> The everyday building blocks — variables, types, operators, and control flow — that every Python program is made of.

## 🧠 What & Why

Before you can build anything interesting, you need the basics: how to store values, do math, make decisions, and repeat work. Python makes all of this refreshingly clean, so you can focus on the logic instead of fighting the syntax.

The core idea is that **variables are just names pointing to values**. You don't declare a type; you assign a value and Python remembers what kind of thing it is. This is *dynamic typing*, and it's why the same variable can hold a number now and a string later (though doing that on purpose is usually a bad idea).

Python leans hard on **indentation** to define structure. Where other languages use curly braces `{}`, Python uses consistent spacing. This forces readable code — and occasionally trips up beginners who mix tabs and spaces.

## 🔑 Key Concepts

- **Variable** — A name bound to a value, e.g. `age = 30`. No type declaration needed.
- **Dynamic typing** — The type lives with the value, not the variable name.
- **Truthiness** — Any value can be treated as `True` or `False` in a condition.
- **f-string** — A readable way to embed values inside text: `f"Hi {name}"`.
- **`None`** — Python's "nothing here" value, used for absence of a result.
- **Indentation** — Whitespace defines blocks; be consistent (4 spaces is standard).

## 💻 Example

```python
# --- Variables and basic types ---
name = "Sam"          # str
age = 27              # int
height = 1.75         # float
is_student = True     # bool
nickname = None       # NoneType — "no value yet"

# --- Operators ---
total = 10 + 5 * 2    # 20 (normal math precedence)
is_adult = age >= 18  # comparison -> True

# --- f-strings for clean output ---
print(f"{name} is {age} years old.")

# --- Control flow ---
if age < 13:
    print("child")
elif age < 20:
    print("teen")
else:
    print("adult")

# --- Loops ---
for i in range(3):        # 0, 1, 2
    print("count", i)

n = 3
while n > 0:              # runs until condition is False
    print("countdown", n)
    n -= 1

# --- Getting input (always returns a string) ---
# user = input("Your name? ")
# print(f"Hello, {user}!")
```

## 📊 Common Data Types

| Type | Example | Notes |
|---|---|---|
| `int` | `42` | Whole numbers, unlimited size |
| `float` | `3.14` | Decimal numbers |
| `str` | `"hello"` | Text, immutable |
| `bool` | `True` / `False` | Subtype of `int` (True == 1) |
| `NoneType` | `None` | Represents "no value" |

## 🔍 Truthiness

In a boolean context, Python treats many values as `False` without you writing `== something`:

- **Falsy:** `0`, `0.0`, `""` (empty string), `[]`, `{}`, `None`, `False`
- **Truthy:** basically everything else — non-zero numbers, non-empty strings/collections

```python
items = []
if items:                 # cleaner than: if len(items) > 0
    print("has items")
else:
    print("empty")        # this runs
```

## ⚠️ Common Pitfalls

- **`input()` always returns a string.** Convert with `int()` or `float()` if you need a number.
- **Mixing tabs and spaces** causes `IndentationError`. Pick spaces (4) and stick to it.
- **`=` vs `==`.** `=` assigns, `==` compares. A classic bug source.
- **Assuming `None` is `False`.** It's *falsy*, but `None == False` is `False`. Use `is None` to check.
- **Integer vs float division.** `5 / 2` gives `2.5`; use `5 // 2` for floor division (`2`).

## ❓ FAQs

### What's the difference between `==` and `is`?

`==` checks whether two values are *equal*, while `is` checks whether they are the *same object* in memory. Use `==` for value comparison and reserve `is` for checking against `None` (e.g. `if x is None`).

### Why does `input()` give me a string even when I type a number?

`input()` reads whatever the user types as text, always returning a `str`. If you type `42`, you get the string `"42"`, not the number. Wrap it in `int(input(...))` or `float(input(...))` to get a numeric value.

### What is truthiness and why is it useful?

Truthiness lets you use any value directly in a condition — empty collections, `0`, `None`, and empty strings count as `False`, and most other values count as `True`. It makes code cleaner: `if my_list:` reads better than `if len(my_list) > 0:`.

### Why does Python care so much about indentation?

Python uses indentation instead of braces to define code blocks, which enforces a consistent, readable style. The trade-off is that inconsistent spacing (or mixing tabs and spaces) causes errors, so tooling and editors usually standardize on 4 spaces.

### What is `None` and when should I use it?

`None` is Python's way of saying "no value." It's commonly used as a default for optional parameters, a placeholder before a real value is known, or the implicit return of a function that doesn't return anything. Check for it with `is None`.
