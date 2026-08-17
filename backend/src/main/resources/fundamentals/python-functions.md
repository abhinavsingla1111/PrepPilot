# 🧩 Python Functions

> Reusable blocks of logic — plus the powerful extras like *args, closures, and decorators that interviewers love to probe.

## 🧠 What & Why

Functions let you name a piece of logic once and reuse it everywhere. They keep code DRY (Don't Repeat Yourself), make it testable, and give complex programs structure. In Python, functions are **first-class citizens** — you can pass them around, return them, and store them in variables, just like any other value.

That "functions are values" idea unlocks some of Python's most elegant features: **closures** (functions that remember their surrounding state) and **decorators** (functions that wrap other functions to add behavior). These show up constantly in real frameworks — FastAPI routes, Flask views, and caching all rely on decorators.

Python is also flexible about how you pass arguments: positional, keyword, defaults, and the catch-all `*args`/`**kwargs`. Mastering these lets you write clean, adaptable APIs.

## 🔑 Key Concepts

- **Positional args** — Matched by order: `greet("Sam", 27)`.
- **Keyword args** — Matched by name: `greet(name="Sam", age=27)`.
- **Default args** — Provide a fallback value: `def greet(name, greeting="Hi")`.
- **`*args`** — Collects extra positional args into a tuple.
- **`**kwargs`** — Collects extra keyword args into a dict.
- **Lambda** — A tiny anonymous function: `lambda x: x * 2`.
- **Closure** — A function that captures variables from its enclosing scope.
- **Decorator** — A function that wraps another to extend its behavior.

## 💻 Example

```python
# --- Basic function with default + keyword args ---
def greet(name, greeting="Hi"):
    return f"{greeting}, {name}!"

print(greet("Sam"))                 # "Hi, Sam!"
print(greet("Sam", greeting="Yo"))  # "Yo, Sam!"

# --- *args and **kwargs ---
def report(*args, **kwargs):
    print("positional:", args)      # a tuple
    print("keyword:", kwargs)       # a dict

report(1, 2, role="admin")          # positional: (1, 2)  keyword: {'role': 'admin'}

# --- Lambda (anonymous function) ---
double = lambda x: x * 2
print(double(5))                    # 10

# --- Closure: inner function remembers `factor` ---
def multiplier(factor):
    def multiply(n):
        return n * factor           # captures `factor`
    return multiply

triple = multiplier(3)
print(triple(10))                   # 30
```

## 🎁 Decorators

A decorator wraps a function to add behavior — logging, timing, access checks — without touching the original code.

```python
import functools

def log_calls(func):
    @functools.wraps(func)          # preserves the original name/docstring
    def wrapper(*args, **kwargs):
        print(f"Calling {func.__name__}")
        result = func(*args, **kwargs)
        print(f"{func.__name__} returned {result}")
        return result
    return wrapper

@log_calls                          # same as: add = log_calls(add)
def add(a, b):
    return a + b

add(2, 3)
# Calling add
# add returned 5
```

## 🔎 Scope: The LEGB Rule

Python resolves variable names by searching scopes in this order:

| Level | Meaning |
|---|---|
| **L** — Local | Names inside the current function |
| **E** — Enclosing | Names in any outer (enclosing) function |
| **G** — Global | Names at the top level of the module |
| **B** — Built-in | Names Python provides (`len`, `print`, ...) |

Python stops at the first match. To reassign a global from inside a function, use the `global` keyword; to reassign an enclosing variable, use `nonlocal`.

## ⚠️ Common Pitfalls

- **Mutable default arguments.** `def f(items=[])` reuses the *same* list across calls. Use `None`:

```python
def add_item(item, items=None):
    if items is None:
        items = []          # fresh list every call
    items.append(item)
    return items
```

- **Forgetting `return`.** A function with no `return` gives back `None`.
- **Late binding in closures/loops.** Closures capture variables, not their values at loop time.
- **Overusing lambdas.** If it needs a name or multiple lines, use `def`.

## ❓ FAQs

### What's the difference between `*args` and `**kwargs`?

`*args` collects any extra *positional* arguments into a tuple, while `**kwargs` collects extra *keyword* arguments into a dictionary. Together they let a function accept any number of arguments, which is handy for wrappers and flexible APIs.

### Why is a mutable default argument dangerous?

Default argument values are created **once**, when the function is defined — not on each call. So a default like `items=[]` is shared across every call, and mutations persist between them. The fix is to default to `None` and create a fresh object inside the function.

### What is a decorator and how does it work?

A decorator is a function that takes another function, wraps it in extra behavior, and returns the wrapped version. The `@decorator` syntax above a function is just shorthand for `func = decorator(func)`. They're used everywhere for logging, timing, authentication, and route registration.

### What is a closure?

A closure is a function that "remembers" variables from the scope where it was created, even after that outer function has finished running. In the `multiplier` example, the inner function keeps access to `factor`, so `triple` permanently multiplies by 3.

### What does the LEGB rule mean?

LEGB is the order Python searches for a variable name: **L**ocal, then **E**nclosing, then **G**lobal, then **B**uilt-in. It stops at the first place it finds the name. Understanding it explains why a local variable can shadow a global one and how nested functions access outer variables.
