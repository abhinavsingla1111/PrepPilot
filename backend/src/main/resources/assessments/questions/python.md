# Python — 100 Interview MCQs

> Format: Each question has a difficulty tag `[Easy] / [Medium] / [Hard]`, options (or True/False, or one-word), the correct answer, and a one-line justification. Assumes Python 3.x.

---

## Section A — Easy (Q1–Q35)

**Q1. [Easy]** Which keyword defines a function?

- A) func
- B) def
- C) function
- D) lambda

**Answer:** B — `def` defines a named function.

**Q2. [Easy]** Python is:

- A) Compiled to machine code directly
- B) Interpreted / bytecode-compiled and dynamically typed
- C) Statically typed
- D) Assembly-based

**Answer:** B — CPython compiles to bytecode run by the interpreter.

**Q3. [Easy] (One word)** The data type for an ordered, immutable sequence is **\_\_**.

**Answer:** tuple — Tuples are immutable ordered sequences.

**Q4. [Easy]** Which is a mutable built-in type?

- A) tuple
- B) str
- C) list
- D) frozenset

**Answer:** C — Lists are mutable.

**Q5. [Easy]** How do you start a comment in Python?

- A) //
- B) #
- C) /\* \*/
- D) --

**Answer:** B — `#` begins a single-line comment.

**Q6. [Easy] (True/False)** Indentation is syntactically significant in Python.

**Answer:** True — Blocks are defined by indentation.

**Q7. [Easy]** Which creates a dictionary?

- A) []
- B) ()
- C) {}
- D) <>

**Answer:** C — `{}` creates a dict (or set).

**Q8. [Easy]** What does `len("hello")` return?

- A) 4
- B) 5
- C) 6
- D) Error

**Answer:** B — There are 5 characters.

**Q9. [Easy] (One word)** The value representing "no value" in Python is **\_\_**.

**Answer:** None — `None` denotes absence of a value.

**Q10. [Easy]** Which operator is exponentiation?

- A) ^
- B) \*\*
- C) //
- D) %

**Answer:** B — `**` raises to a power.

**Q11. [Easy]** `//` operator performs:

- A) Float division
- B) Floor (integer) division
- C) Modulo
- D) Exponent

**Answer:** B — `//` is floor division.

**Q12. [Easy] (True/False)** Strings in Python are immutable.

**Answer:** True — String objects cannot be changed in place.

**Q13. [Easy]** How do you convert a string `"5"` to an integer?

- A) str(5)
- B) int("5")
- C) float("5")
- D) chr("5")

**Answer:** B — `int("5")` parses to 5.

**Q14. [Easy]** Which loop iterates over a sequence?

- A) for
- B) do-while
- C) foreach
- D) repeat

**Answer:** A — `for` iterates over iterables.

**Q15. [Easy] (One word)** A block of reusable code that may return a value using `return` is a **\_\_**.

**Answer:** function — Functions encapsulate reusable logic.

**Q16. [Easy]** `range(3)` yields:

- A) 1,2,3
- B) 0,1,2
- C) 0,1,2,3
- D) 3

**Answer:** B — `range(3)` gives 0,1,2.

**Q17. [Easy]** Which keyword handles exceptions?

- A) catch
- B) except
- C) rescue
- D) handle

**Answer:** B — Python uses `try/except`.

**Q18. [Easy] (True/False)** A set stores unique, unordered elements.

**Answer:** True — Sets hold unique items without order.

**Q19. [Easy]** How do you print to the console?

- A) echo
- B) print()
- C) printf()
- D) console.log()

**Answer:** B — `print()` outputs to stdout.

**Q20. [Easy]** Which is the boolean value for an empty list `[]`?

- A) True
- B) False
- C) None
- D) Error

**Answer:** B — Empty containers are falsy.

**Q21. [Easy] (One word)** The keyword to create an anonymous function is **\_\_**.

**Answer:** lambda — `lambda` creates inline functions.

**Q22. [Easy]** Which method adds an item to a list end?

- A) add()
- B) append()
- C) push()
- D) insert()

**Answer:** B — `append()` adds to the end.

**Q23. [Easy]** `type(3.0)` returns:

- A) int
- B) float
- C) double
- D) number

**Answer:** B — `3.0` is a float.

**Q24. [Easy] (True/False)** Python supports multiple inheritance.

**Answer:** True — A class can inherit from multiple bases.

**Q25. [Easy]** Which imports a module?

- A) include
- B) import
- C) require
- D) using

**Answer:** B — `import` loads modules.

**Q26. [Easy]** String formatting with f-strings uses:

- A) "%s"
- B) f"{value}"
- C) format()
- D) +

**Answer:** B — f-strings embed expressions: `f"{value}"`.

**Q27. [Easy] (One word)** The interactive Python shell is often called the **\_\_** (Read-Eval-Print Loop).

**Answer:** REPL — Read-Eval-Print Loop.

**Q28. [Easy]** Which slices a list from index 1 to 3 (exclusive)?

- A) list[1:3]
- B) list[1,3]
- C) list(1:3)
- D) list.slice(1,3)

**Answer:** A — `list[1:3]` returns elements at indices 1 and 2.

**Q29. [Easy]** What does `bool("")` return?

- A) True
- B) False
- C) None
- D) Error

**Answer:** B — Empty strings are falsy.

**Q30. [Easy] (True/False)** `is` checks identity, `==` checks equality.

**Answer:** True — `is` compares object identity; `==` compares values.

**Q31. [Easy]** Which comprehension creates a list of squares?

- A) [x*x for x in range(5)]
- B) {x\*x for x in range(5)}
- C) (x\*x for x in range(5))
- D) list(x\*x)

**Answer:** A — List comprehension with `[]`.

**Q32. [Easy]** The `pass` statement:

- A) Skips a loop iteration
- B) Is a no-op placeholder
- C) Exits a function
- D) Raises an error

**Answer:** B — `pass` does nothing (placeholder).

**Q33. [Easy] (One word)** The function to get user input from console is **\_\_**.

**Answer:** input — `input()` reads a line from stdin.

**Q34. [Easy]** Which removes and returns the last list element?

- A) remove()
- B) pop()
- C) del
- D) discard()

**Answer:** B — `pop()` removes and returns the last item by default.

**Q35. [Easy]** `"abc"[::-1]` returns:

- A) abc
- B) cba
- C) Error
- D) a

**Answer:** B — Slicing with step -1 reverses the string.

---

## Section B — Medium (Q36–Q75)

**Q36. [Medium]** Output?

```python
def f(a, b=[]):
    b.append(a)
    return b
print(f(1)); print(f(2))
```

- A) [1] [2]
- B) [1] [1, 2]
- C) [1, 2] [1, 2]
- D) Error

**Answer:** B — The mutable default is shared across calls (classic gotcha).

**Q37. [Medium]** The fix for Q36's mutable default is:

- A) b=[] is fine
- B) Use b=None then b = b or []
- C) Use a tuple always
- D) Global variable

**Answer:** B — Default to `None` and create a new list inside.

**Q38. [Medium] (True/False)** In Python, integers have arbitrary precision.

**Answer:** True — Python ints grow beyond machine word size automatically.

**Q39. [Medium]** `*args` in a function signature captures:

- A) Keyword arguments
- B) Extra positional arguments as a tuple
- C) A single argument
- D) Nothing

**Answer:** B — `*args` collects positional args into a tuple.

**Q40. [Medium]** `**kwargs` captures:

- A) Positional args
- B) Extra keyword arguments as a dict
- C) A list
- D) None

**Answer:** B — `**kwargs` gathers keyword args into a dict.

**Q41. [Medium]** Output?

```python
a = [1, 2, 3]
b = a
b.append(4)
print(a)
```

- A) [1, 2, 3]
- B) [1, 2, 3, 4]
- C) Error
- D) [4]

**Answer:** B — `b` and `a` reference the same list.

**Q42. [Medium]** A shallow copy of a list can be made with:

- A) a.copy() or a[:]
- B) copy.deepcopy(a)
- C) a = a
- D) list.new(a)

**Answer:** A — `.copy()`/slicing creates a shallow copy.

**Q43. [Medium] (One word)** The module for deep copying nested objects is **\_\_**.

**Answer:** copy — `copy.deepcopy` copies recursively.

**Q44. [Medium]** A generator function uses which keyword?

- A) return
- B) yield
- C) async
- D) def only

**Answer:** B — `yield` produces values lazily.

**Q45. [Medium]** Generators are useful because they:

- A) Store all values in memory
- B) Produce items lazily, saving memory
- C) Are faster to write only
- D) Cannot be iterated

**Answer:** B — Lazy evaluation avoids materializing all values.

**Q46. [Medium] (True/False)** A decorator is a function that takes a function and returns a modified function.

**Answer:** True — Decorators wrap/extend callables.

**Q47. [Medium]** Output?

```python
print(2 == 2.0)
print(2 is 2.0)
```

- A) True True
- B) True False
- C) False False
- D) Error

**Answer:** B — Equal in value but different objects/types (identity differs).

**Q48. [Medium]** `list(map(lambda x: x*2, [1,2,3]))` returns:

- A) [1,2,3]
- B) [2,4,6]
- C) [1,4,9]
- D) Error

**Answer:** B — `map` doubles each element.

**Q49. [Medium]** `filter(lambda x: x%2==0, range(6))` yields:

- A) 1,3,5
- B) 0,2,4
- C) 0,1,2,3,4,5
- D) empty

**Answer:** B — It keeps even numbers.

**Q50. [Medium] (One word)** The special method to make an object printable/readable is **repr** or **str**; the "official" one is **r\_\_\_\_**.

**Answer:** repr — `__repr__` gives the official representation.

**Q51. [Medium]** Which creates a dictionary comprehension?

- A) {k: v for k, v in pairs}
- B) [k: v for k in pairs]
- C) (k, v for k in pairs)
- D) dict[k]=v

**Answer:** A — Dict comprehension uses `{k: v for ...}`.

**Q52. [Medium]** What does `enumerate(['a','b'])` provide?

- A) Just values
- B) (index, value) pairs
- C) Just indices
- D) A dict

**Answer:** B — It yields index-value tuples.

**Q53. [Medium] (True/False)** `zip` stops at the shortest iterable.

**Answer:** True — It pairs until the shortest is exhausted.

**Q54. [Medium]** Output?

```python
x = "hello"
print(x[10])
```

- A) o
- B) IndexError
- C) None
- D) empty

**Answer:** B — Index out of range raises `IndexError`.

**Q55. [Medium]** The `with` statement is used for:

- A) Loops
- B) Context management (auto resource cleanup)
- C) Conditionals
- D) Imports

**Answer:** B — It manages resources via context managers.

**Q56. [Medium] (One word)** A context manager implements **enter** and **exit**; the decorator to create one easily is contextlib.**\_\_**.

**Answer:** contextmanager — `@contextmanager` builds one from a generator.

**Q57. [Medium]** `sorted([3,1,2], reverse=True)` returns:

- A) [1,2,3]
- B) [3,2,1]
- C) [2,1,3]
- D) Error

**Answer:** B — Sorted descending.

**Q58. [Medium]** Which is TRUE about `dict` ordering (Python 3.7+)?

- A) Unordered
- B) Insertion order is preserved
- C) Sorted by key
- D) Random

**Answer:** B — Dicts maintain insertion order since 3.7.

**Q59. [Medium] (True/False)** `set` supports indexing like `s[0]`.

**Answer:** False — Sets are unordered and not subscriptable.

**Q60. [Medium]** Output?

```python
print(bool(0), bool([]), bool("0"))
```

- A) False False False
- B) False False True
- C) True True True
- D) False True False

**Answer:** B — `"0"` is a non-empty string (truthy); `0` and `[]` are falsy.

**Q61. [Medium]** Which exception is raised by `int("abc")`?

- A) TypeError
- B) ValueError
- C) KeyError
- D) NameError

**Answer:** B — Invalid literal raises `ValueError`.

**Q62. [Medium] (One word)** The keyword to define a class is **\_\_**.

**Answer:** class — Classes are defined with `class`.

**Q63. [Medium]** `self` in a method refers to:

- A) The class
- B) The instance
- C) The module
- D) The parent

**Answer:** B — `self` is the current instance.

**Q64. [Medium]** `__init__` is:

- A) A destructor
- B) The initializer/constructor method
- C) A class method
- D) A static method

**Answer:** B — It initializes new instances.

**Q65. [Medium] (True/False)** `@staticmethod` methods do not receive `self` or `cls`.

**Answer:** True — Static methods take neither implicit argument.

**Q66. [Medium]** `@classmethod` receives which first parameter?

- A) self
- B) cls
- C) nothing
- D) instance

**Answer:** B — It receives the class as `cls`.

**Q67. [Medium]** Output?

```python
print([i for i in range(5) if i % 2])
```

- A) [0,2,4]
- B) [1,3]
- C) [0,1,2,3,4]
- D) []

**Answer:** B — Keeps odd numbers (truthy `i % 2`).

**Q68. [Medium]** What does `dict.get('k', 0)` do?

- A) Raises KeyError if missing
- B) Returns 0 if 'k' is absent
- C) Deletes 'k'
- D) Adds 'k'

**Answer:** B — Returns the default when the key is missing.

**Q69. [Medium] (One word)** The module providing high-performance container datatypes (deque, Counter) is **\_\_**.

**Answer:** collections — `collections` offers specialized containers.

**Q70. [Medium]** `a, b = b, a` performs:

- A) A syntax error
- B) Swapping values of a and b
- C) Nothing
- D) Concatenation

**Answer:** B — Tuple packing/unpacking swaps values.

**Q71. [Medium]** Which is TRUE about slicing beyond bounds `"abc"[1:99]`?

- A) IndexError
- B) Returns "bc" (clamped)
- C) Returns ""
- D) Returns "abc"

**Answer:** B — Slicing clamps to valid range.

**Q72. [Medium] (True/False)** `all([])` returns True.

**Answer:** True — `all` of an empty iterable is vacuously True.

**Q73. [Medium]** `any([0, "", None])` returns:

- A) True
- B) False
- C) None
- D) Error

**Answer:** B — All elements are falsy, so `any` is False.

**Q74. [Medium]** The walrus operator `:=` allows:

- A) Multiple assignment
- B) Assignment within an expression
- C) Comparison
- D) Slicing

**Answer:** B — It assigns and returns a value inline (Python 3.8+).

**Q75. [Medium] (One word)** The keyword to reference and modify a module-level variable inside a function is **\_\_**.

**Answer:** global — `global` binds to the module-level name.

---

## Section C — Hard (Q76–Q100)

**Q76. [Hard]** The GIL (Global Interpreter Lock) in CPython means:

- A) Only one thread executes Python bytecode at a time
- B) No threads allowed
- C) True parallel CPU-bound threading
- D) It disables multiprocessing

**Answer:** A — The GIL serializes bytecode execution across threads.

**Q77. [Hard]** For CPU-bound parallelism in CPython, prefer:

- A) threading
- B) multiprocessing
- C) asyncio
- D) more threads

**Answer:** B — Separate processes bypass the GIL for CPU-bound work.

**Q78. [Hard] (True/False)** `asyncio` provides concurrency via cooperative multitasking, not parallelism.

**Answer:** True — It uses an event loop and awaitable coroutines on one thread.

**Q79. [Hard]** Output?

```python
def make():
    return [lambda: i for i in range(3)]
print([f() for f in make()])
```

- A) [0, 1, 2]
- B) [2, 2, 2]
- C) [0, 0, 0]
- D) Error

**Answer:** B — Closures capture the variable `i`, which is 2 after the loop.

**Q80. [Hard]** The MRO (Method Resolution Order) uses:

- A) DFS
- B) C3 linearization
- C) Random order
- D) Alphabetical

**Answer:** B — Python computes MRO via C3 linearization.

**Q81. [Hard] (One word)** The attribute that shows a class's method resolution order is **m\_\_\_\_** or the mro() method.

**Answer:** mro — `Class.__mro__` / `Class.mro()`.

**Q82. [Hard]** `__slots__` is used to:

- A) Add methods
- B) Restrict instance attributes and save memory (no per-instance **dict**)
- C) Enable inheritance
- D) Speed up imports

**Answer:** B — It avoids `__dict__`, reducing memory per instance.

**Q83. [Hard]** Output?

```python
print(0.1 + 0.2 == 0.3)
```

- A) True
- B) False
- C) Error
- D) None

**Answer:** B — Floating-point representation makes it slightly off.

**Q84. [Hard] (True/False)** Everything in Python is an object, including functions and classes.

**Answer:** True — Functions/classes are first-class objects.

**Q85. [Hard]** A metaclass is:

- A) A parent class
- B) The class of a class (controls class creation)
- C) An abstract class
- D) A decorator

**Answer:** B — Metaclasses define how classes themselves are built (`type`).

**Q86. [Hard]** What does `functools.lru_cache` do?

- A) Logs calls
- B) Memoizes function results with an LRU cache
- C) Limits recursion
- D) Times functions

**Answer:** B — It caches results to avoid recomputation.

**Q87. [Hard] (One word)** The protocol methods like **len**, **getitem** that make objects behave like built-ins are called **\_\_** methods (a.k.a. magic/special).

**Answer:** dunder — Double-underscore (dunder) methods.

**Q88. [Hard]** Output?

```python
a = 256; b = 256
c = 257; d = 257
print(a is b, c is d)
```

- A) True True
- B) True False (interpreter dependent)
- C) False False
- D) Error

**Answer:** B — Small ints (-5..256) are cached/interned; 257 may be distinct objects.

**Q89. [Hard]** `__new__` vs `__init__`:

- A) Identical
- B) `__new__` creates the instance; `__init__` initializes it
- C) `__init__` creates; `__new__` initializes
- D) Neither creates

**Answer:** B — `__new__` allocates/returns the object; `__init__` sets it up.

**Q90. [Hard] (True/False)** Coroutines defined with `async def` return coroutine objects that must be awaited or run by an event loop.

**Answer:** True — Calling them doesn't execute the body immediately.

**Q91. [Hard]** Why can catching `Exception` broadly be problematic?

- A) It's faster
- B) It can hide bugs and swallow unexpected errors (including programming errors)
- C) It's illegal
- D) It catches SystemExit always

**Answer:** B — Overly broad catches mask real problems.

**Q92. [Hard]** `id(x)` returns:

- A) The value
- B) The object's identity (memory address in CPython)
- C) The hash
- D) The type

**Answer:** B — `id` gives a unique identity for the object's lifetime.

**Q93. [Hard] (One word)** Objects usable as dict keys must be **\_\_** (support **hash** and **eq**).

**Answer:** hashable — Keys must be hashable.

**Q94. [Hard]** Why are tuples usable as dict keys but lists are not?

- A) Tuples are faster
- B) Tuples are immutable/hashable; lists are mutable/unhashable
- C) Lists are smaller
- D) No reason

**Answer:** B — Hashability requires immutability of contents.

**Q95. [Hard]** Output?

```python
print(type(type))
```

- A) <class 'object'>
- B) <class 'type'>
- C) <class 'class'>
- D) Error

**Answer:** B — `type` is its own metaclass; `type(type) is type`.

**Q96. [Hard] (True/False)** `is` should be used to compare with `None` (e.g., `x is None`).

**Answer:** True — Identity comparison with `None` is idiomatic and correct.

**Q97. [Hard]** Descriptors are objects that define:

- A) **enter**/**exit**
- B) **get**/**set**/**delete** to customize attribute access
- C) **iter** only
- D) **call** only

**Answer:** B — Descriptors control attribute access (e.g., `property`).

**Q98. [Hard]** `property` is implemented using:

- A) Decorators only
- B) The descriptor protocol
- C) Metaclasses
- D) Generators

**Answer:** B — `property` is a data descriptor.

**Q99. [Hard] (One word)** The special method to make an instance callable like a function is **c\_\_\_\_**.

**Answer:** call — `__call__` makes instances callable.

**Q100. [Hard]** Output?

```python
x = [1, 2, 3]
print(x * 2)
```

- A) [2, 4, 6]
- B) [1, 2, 3, 1, 2, 3]
- C) Error
- D) [1, 2, 3, 2]

**Answer:** B — `*` repeats/concatenates the list, not element-wise multiply.
