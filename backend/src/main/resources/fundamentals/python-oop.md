# 🏛️ Python OOP

> Modeling your program as objects — data bundled with the behavior that acts on it.

## 🧠 What & Why

Object-Oriented Programming (OOP) is a way of organizing code around **objects**: self-contained bundles of data (attributes) and behavior (methods). Instead of scattering related variables and functions everywhere, you group them into a **class** — a blueprint — and create **instances** from it.

Why bother? OOP shines when you have many things that share structure and behavior: users, orders, animals, HTTP requests. Define the shape once in a class, then stamp out as many instances as you need. Features like **inheritance** let you reuse and specialize behavior, while **encapsulation** keeps internal details tidy.

Python's OOP is deliberately lightweight. There's no forced "everything must be a class" — but when you need it, the tools are clean and expressive, including special **dunder methods** that hook into Python's built-in behavior.

## 🔑 Key Concepts

- **Class** — A blueprint defining attributes and methods.
- **Instance** — A concrete object built from a class.
- **`__init__`** — The constructor; runs when you create an instance.
- **`self`** — Reference to the current instance, passed automatically.
- **Instance vs class attribute** — Per-object data vs data shared by all instances.
- **Inheritance** — A class deriving from another to reuse/extend behavior.
- **Dunder methods** — Special `__name__` methods like `__str__`, `__eq__`.
- **`@property`** — Expose a method like an attribute.

## 💻 Example

```python
class Animal:
    kingdom = "Animalia"            # class attribute — shared by all

    def __init__(self, name, sound):
        self.name = name            # instance attribute — per object
        self.sound = sound

    def speak(self):                # instance method
        return f"{self.name} says {self.sound}"

    def __str__(self):              # human-readable string
        return f"Animal({self.name})"

    def __repr__(self):             # unambiguous, for debugging
        return f"Animal(name={self.name!r}, sound={self.sound!r})"

    def __eq__(self, other):        # define equality
        return self.name == other.name


# --- Inheritance ---
class Dog(Animal):
    def __init__(self, name):
        super().__init__(name, sound="Woof")   # call parent constructor

    def fetch(self):                            # new behavior
        return f"{self.name} fetches the ball"


d = Dog("Rex")
print(d.speak())     # Rex says Woof
print(d.fetch())     # Rex fetches the ball
print(str(d))        # Animal(Rex)  -> uses __str__
print(Dog.kingdom)   # Animalia     -> inherited class attribute
```

## 🎛️ Properties and Static/Class Methods

```python
class Temperature:
    def __init__(self, celsius):
        self._celsius = celsius     # "_" = internal, by convention

    @property
    def fahrenheit(self):           # access like an attribute: t.fahrenheit
        return self._celsius * 9 / 5 + 32

    @staticmethod
    def freezing_point():           # no self — utility tied to the class
        return 0

    @classmethod
    def from_fahrenheit(cls, f):    # cls = the class; alternate constructor
        return cls((f - 32) * 5 / 9)


t = Temperature(25)
print(t.fahrenheit)             # 77.0  (no parentheses — it's a property)
print(Temperature.freezing_point())   # 0
boiling = Temperature.from_fahrenheit(212)   # builds a new instance
```

## 🔒 Encapsulation Conventions

Python doesn't enforce private access; it relies on naming conventions:

| Convention | Meaning |
|---|---|
| `name` | Public — use freely |
| `_name` | "Internal" — please don't touch from outside |
| `__name` | Name-mangled — strongly discourages external access |

## ⚠️ Common Pitfalls

- **Forgetting `self`** in method definitions causes `TypeError`. Every instance method takes `self` first.
- **Mutable class attributes** (like a shared list) are accidentally shared across all instances. Put per-object state in `__init__`.
- **Confusing `__str__` and `__repr__`.** `__str__` is for users; `__repr__` is for developers/debugging.
- **Thinking `_` or `__` makes things truly private.** They're conventions/mangling, not hard access control.

## ❓ FAQs

### What's the difference between an instance attribute and a class attribute?

An instance attribute (set with `self.x = ...` in `__init__`) belongs to one specific object, so each instance has its own copy. A class attribute is defined directly in the class body and is shared by all instances — great for constants, but risky if it's mutable.

### What does `self` do, and why is it everywhere?

`self` is a reference to the specific instance a method is operating on. Python passes it automatically when you call `obj.method()`, letting the method read and modify that object's attributes. It's explicit in Python (unlike the implicit `this` in some languages), which keeps behavior clear.

### What's the difference between `__str__` and `__repr__`?

`__str__` returns a friendly, human-readable description used by `print()` and `str()`. `__repr__` returns an unambiguous, developer-focused representation (ideally one you could paste back into code) used in the REPL and debugging. If you only define one, define `__repr__`.

### When should I use `@staticmethod` vs `@classmethod`?

Use `@staticmethod` for a utility function that logically belongs to the class but needs neither the instance (`self`) nor the class (`cls`). Use `@classmethod` when you need the class itself — most often to build alternate constructors, like `from_fahrenheit`, that return new instances via `cls(...)`.

### Does Python have real private variables?

Not enforced ones. A leading underscore (`_name`) is a convention meaning "internal, please don't use directly," and a double underscore (`__name`) triggers name mangling that makes accidental external access harder. But a determined caller can still reach them — encapsulation in Python is a gentleman's agreement.
