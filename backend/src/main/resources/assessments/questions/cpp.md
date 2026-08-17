# C++ — 100 Interview MCQs

> Format: Each question has a difficulty tag `[Easy] / [Medium] / [Hard]`, options (or True/False, or one-word), the correct answer, and a one-line justification. Assumes modern C++ (C++11–C++20) where relevant.

---

## Section A — Easy (Q1–Q35)

**Q1. [Easy]** Which header is needed for `std::cout`?

- A) <stdio.h>
- B) <iostream>
- C) <string>
- D) <vector>

**Answer:** B — `std::cout` lives in `<iostream>`.

**Q2. [Easy]** The correct namespace for standard library entities is:

- A) sys
- B) std
- C) lib
- D) cpp

**Answer:** B — Standard components are in namespace `std`.

**Q3. [Easy] (One word)** The operator used to allocate memory dynamically is **\_\_**.

**Answer:** new — `new` allocates on the heap.

**Q4. [Easy]** Which frees memory allocated by `new`?

- A) free
- B) delete
- C) remove
- D) dispose

**Answer:** B — `delete` releases memory from `new`.

**Q5. [Easy] (True/False)** C++ supports both procedural and object-oriented programming.

**Answer:** True — C++ is multi-paradigm.

**Q6. [Easy]** What is the size of `char` in C++ (guaranteed)?

- A) Always 8 bits
- B) 1 byte (at least 8 bits)
- C) 2 bytes
- D) 4 bytes

**Answer:** B — `sizeof(char) == 1` by definition.

**Q7. [Easy]** A reference in C++ is:

- A) A pointer
- B) An alias to an existing variable
- C) A copy
- D) A macro

**Answer:** B — A reference is another name for an object.

**Q8. [Easy] (One word)** A function that has the same name as its class and initializes objects is the **\_\_**.

**Answer:** Constructor — It initializes instances.

**Q9. [Easy]** Which correctly declares a pointer to int?

- A) int p;
- B) int \*p;
- C) int &p;
- D) pointer int p;

**Answer:** B — `int *p;` declares a pointer to int.

**Q10. [Easy]** The `->` operator is used to:

- A) Access members via a pointer
- B) Declare references
- C) Cast types
- D) Allocate memory

**Answer:** A — `ptr->member` dereferences and accesses a member.

**Q11. [Easy] (True/False)** `std::vector` can grow dynamically.

**Answer:** True — `vector` resizes automatically.

**Q12. [Easy]** Which access specifier is default for a `class`?

- A) public
- B) private
- C) protected
- D) internal

**Answer:** B — Class members are private by default.

**Q13. [Easy]** Which access specifier is default for a `struct`?

- A) private
- B) public
- C) protected
- D) internal

**Answer:** B — Struct members are public by default.

**Q14. [Easy] (One word)** A function defined inside a class is implicitly a candidate for being **\_\_**.

**Answer:** inline — In-class definitions are implicitly inline.

**Q15. [Easy]** Which loop executes at least once?

- A) for
- B) while
- C) do-while
- D) range-for

**Answer:** C — `do-while` checks after executing the body.

**Q16. [Easy]** The `const` keyword on a variable means:

- A) It can change
- B) It cannot be modified after initialization
- C) It is static
- D) It is global

**Answer:** B — `const` makes a value read-only.

**Q17. [Easy] (True/False)** C++ supports function overloading.

**Answer:** True — Functions can share a name with different signatures.

**Q18. [Easy]** Which is used to read input from the console?

- A) std::cin
- B) std::cout
- C) std::cerr
- D) std::clog

**Answer:** A — `std::cin` reads standard input.

**Q19. [Easy]** A `virtual` function enables:

- A) Compile-time binding
- B) Runtime polymorphism
- C) Faster code
- D) Memory saving

**Answer:** B — Virtual functions dispatch dynamically.

**Q20. [Easy] (One word)** A pure virtual function makes its class **\_\_**.

**Answer:** abstract — It cannot be instantiated.

**Q21. [Easy]** Which STL container is a key-value map (sorted)?

- A) std::vector
- B) std::map
- C) std::set
- D) std::list

**Answer:** B — `std::map` stores sorted key-value pairs.

**Q22. [Easy]** Which container provides O(1) average lookup by key?

- A) std::map
- B) std::unordered_map
- C) std::vector
- D) std::list

**Answer:** B — `unordered_map` uses hashing for average O(1).

**Q23. [Easy] (True/False)** In C++, arrays decay to pointers when passed to functions.

**Answer:** True — Array names decay to pointers to their first element.

**Q24. [Easy]** The scope resolution operator is:

- A) .
- B) ::
- C) ->
- D) :

**Answer:** B — `::` accesses namespace/class scope.

**Q25. [Easy] (One word)** The keyword to define a symbolic constant at compile time (C++11) is constexpr or **\_\_**.

**Answer:** const — `const`/`constexpr` define constants.

**Q26. [Easy]** Which correctly creates a smart pointer with sole ownership (C++11)?

- A) std::shared_ptr
- B) std::unique_ptr
- C) std::weak_ptr
- D) raw pointer

**Answer:** B — `unique_ptr` models exclusive ownership.

**Q27. [Easy]** `nullptr` is:

- A) The integer 0
- B) A typed null pointer literal
- C) A macro
- D) A string

**Answer:** B — `nullptr` is a `std::nullptr_t` literal.

**Q28. [Easy] (True/False)** A destructor's name is preceded by `~`.

**Answer:** True — E.g., `~ClassName()`.

**Q29. [Easy]** `std::string` is defined in:

- A) <string>
- B) <cstring>
- C) <iostream>
- D) <vector>

**Answer:** A — `std::string` is in `<string>`.

**Q30. [Easy]** Which is TRUE about `endl` vs `'\n'`?

- A) Identical
- B) `endl` also flushes the stream
- C) `'\n'` flushes
- D) Neither prints newline

**Answer:** B — `std::endl` inserts newline and flushes.

**Q31. [Easy] (One word)** The C++ mechanism to handle runtime errors uses try/catch and **\_\_**.

**Answer:** throw — Exceptions are raised with `throw`.

**Q32. [Easy]** Which is a compile-time construct for generic programming?

- A) Templates
- B) Virtual functions
- C) Macros only
- D) Namespaces

**Answer:** A — Templates enable generic code.

**Q33. [Easy]** The default parameter passing in C++ (without reference/pointer) is:

- A) By reference
- B) By value (copy)
- C) By pointer
- D) By name

**Answer:** B — Arguments are passed by value by default.

**Q34. [Easy] (True/False)** `std::vector::size()` returns the number of elements.

**Answer:** True — It returns the current element count.

**Q35. [Easy]** Which keyword prevents implicit conversions by a single-arg constructor?

- A) const
- B) explicit
- C) mutable
- D) virtual

**Answer:** B — `explicit` blocks implicit conversions.

---

## Section B — Medium (Q36–Q75)

**Q36. [Medium]** Output?

```cpp
int a = 5;
int& b = a;
b = 10;
std::cout << a;
```

- A) 5
- B) 10
- C) 0
- D) Error

**Answer:** B — `b` is an alias for `a`, so changing `b` changes `a`.

**Q37. [Medium]** The Rule of Three refers to:

- A) Three constructors
- B) Destructor, copy constructor, copy assignment operator
- C) Three templates
- D) Three namespaces

**Answer:** B — If you define one, you likely need all three.

**Q38. [Medium]** The Rule of Five adds which two to the Rule of Three?

- A) Two constructors
- B) Move constructor and move assignment operator
- C) Two destructors
- D) Two operators

**Answer:** B — Move semantics (C++11) add move ctor/assignment.

**Q39. [Medium] (True/False)** `std::move` actually moves data by itself.

**Answer:** False — It only casts to an rvalue reference; the move happens in the move ctor/assignment.

**Q40. [Medium]** A dangling pointer results from:

- A) Using a pointer after the memory it points to is freed
- B) Null initialization
- C) Using references
- D) Const correctness

**Answer:** A — Accessing freed memory yields undefined behavior.

**Q41. [Medium]** `std::unique_ptr` cannot be:

- A) Moved
- B) Copied
- C) Dereferenced
- D) Reset

**Answer:** B — It is move-only (copying is disabled).

**Q42. [Medium]** `std::shared_ptr` uses **\_\_** to manage lifetime:

- A) Garbage collection
- B) Reference counting
- C) Stack allocation
- D) Manual delete

**Answer:** B — A shared control block counts references.

**Q43. [Medium]** `std::weak_ptr` is used to:

- A) Own an object
- B) Break reference cycles / observe without owning
- C) Speed up shared_ptr
- D) Allocate arrays

**Answer:** B — It observes a shared object without affecting the count.

**Q44. [Medium] (One word)** RAII stands for Resource Acquisition Is **\_\_**.

**Answer:** Initialization — Resource lifetime tied to object scope.

**Q45. [Medium]** Output?

```cpp
std::vector<int> v{1,2,3};
for (auto x : v) x *= 2;
std::cout << v[0];
```

- A) 2
- B) 1
- C) 6
- D) Error

**Answer:** B — `x` is a copy; use `auto&` to modify elements.

**Q46. [Medium]** A `const` member function promises:

- A) To not modify the object's observable state
- B) To be virtual
- C) To be static
- D) To be inline

**Answer:** A — It cannot modify non-mutable members.

**Q47. [Medium]** The `mutable` keyword allows:

- A) Modifying a member even in a const method
- B) Making a class abstract
- C) Overloading operators
- D) Template specialization

**Answer:** A — `mutable` members are modifiable in const methods.

**Q48. [Medium] (True/False)** A base class destructor should be virtual if the class is deleted polymorphically.

**Answer:** True — It ensures the derived destructor runs.

**Q49. [Medium]** Which cast is checked at runtime for polymorphic types?

- A) static_cast
- B) dynamic_cast
- C) reinterpret_cast
- D) const_cast

**Answer:** B — `dynamic_cast` performs runtime type checking.

**Q50. [Medium]** `reinterpret_cast` is used for:

- A) Safe numeric conversions
- B) Low-level bit reinterpretation (unsafe)
- C) Removing const
- D) Base/derived conversions

**Answer:** B — It reinterprets bit patterns without safety guarantees.

**Q51. [Medium]** `const_cast` is used to:

- A) Change numeric type
- B) Add or remove const/volatile qualifiers
- C) Downcast
- D) Reinterpret bits

**Answer:** B — It adjusts const-ness (use with caution).

**Q52. [Medium] (One word)** A template that is specialized for a specific type is called a template **\_\_**.

**Answer:** Specialization — Custom behavior for specific types.

**Q53. [Medium]** Which is TRUE about `std::vector` capacity vs size?

- A) They are always equal
- B) Capacity ≥ size; capacity is allocated storage
- C) Size ≥ capacity
- D) Capacity is the byte count

**Answer:** B — Capacity is reserved space; size is used elements.

**Q54. [Medium]** Iterator invalidation on `vector::push_back` can occur when:

- A) Never
- B) Reallocation happens (capacity exceeded)
- C) Only on erase
- D) Only with const iterators

**Answer:** B — Growth may reallocate, invalidating iterators/pointers.

**Q55. [Medium] (True/False)** `std::map` is typically implemented as a red-black tree.

**Answer:** True — It's a balanced BST giving O(log n) operations.

**Q56. [Medium]** Output?

```cpp
int x = 10;
auto f = [x]() mutable { x++; return x; };
std::cout << f() << x;
```

- A) 1110
- B) 1111
- C) 1010
- D) Error

**Answer:** A — Capture by value copies `x`; the lambda returns 11 but outer `x` stays 10.

**Q57. [Medium]** Lambda capture `[&]` means:

- A) Capture nothing
- B) Capture all used variables by reference
- C) Capture by value
- D) Capture this only

**Answer:** B — `[&]` captures used variables by reference.

**Q58. [Medium] (One word)** The keyword to deduce a type automatically is **\_\_**.

**Answer:** auto — `auto` performs type deduction.

**Q59. [Medium]** What does `emplace_back` do better than `push_back`?

- A) Nothing
- B) Constructs the element in place, avoiding a copy/move
- C) Sorts elements
- D) Reserves memory

**Answer:** B — It forwards arguments to construct in place.

**Q60. [Medium]** A memory leak in C++ typically means:

- A) Freed memory reused
- B) Allocated memory never freed
- C) Stack overflow
- D) Null dereference

**Answer:** B — Lost pointers to heap memory cause leaks.

**Q61. [Medium] (True/False)** Passing large objects by `const&` avoids copies.

**Answer:** True — It passes a reference without copying.

**Q62. [Medium]** An rvalue reference is declared with:

- A) &
- B) &&
- C) \*
- D) const&

**Answer:** B — `T&&` binds to rvalues, enabling moves.

**Q63. [Medium]** Perfect forwarding uses:

- A) std::move
- B) std::forward with a forwarding reference
- C) static_cast only
- D) const_cast

**Answer:** B — `std::forward<T>` preserves value category.

**Q64. [Medium]** Which STL algorithm sorts a range?

- A) std::find
- B) std::sort
- C) std::accumulate
- D) std::copy

**Answer:** B — `std::sort` orders elements (typically introsort).

**Q65. [Medium] (One word)** `std::sort` has average time complexity O(n log **\_\_**)?

**Answer:** n — O(n log n) average.

**Q66. [Medium]** Which is TRUE about `static` local variables?

- A) Created each call
- B) Initialized once and persist across calls
- C) Global scope
- D) Thread-local by default

**Answer:** B — They retain value between calls.

**Q67. [Medium]** Output?

```cpp
std::cout << (5 / 2) << " " << (5.0 / 2);
```

- A) 2 2.5
- B) 2.5 2.5
- C) 2 2
- D) 2.5 2

**Answer:** A — Integer division gives 2; floating division gives 2.5.

**Q68. [Medium] (True/False)** A pure virtual function can still have a definition (body) in C++.

**Answer:** True — You may provide a body, callable via explicit qualification.

**Q69. [Medium]** Which container is best for frequent insertions/deletions at both ends?

- A) std::vector
- B) std::deque
- C) std::set
- D) std::array

**Answer:** B — `std::deque` supports efficient front/back operations.

**Q70. [Medium]** What is the output type of `sizeof`?

- A) int
- B) std::size_t
- C) long
- D) unsigned int

**Answer:** B — `sizeof` yields `std::size_t`.

**Q71. [Medium] (One word)** A class template parameter list starts with the keyword **\_\_**.

**Answer:** template — e.g., `template<typename T>`.

**Q72. [Medium]** `std::array` differs from a C array because it:

- A) Is dynamically sized
- B) Knows its size and has STL interface, fixed at compile time
- C) Is heap allocated
- D) Cannot be copied

**Answer:** B — Fixed-size, size-aware STL container.

**Q73. [Medium]** Which prevents copying of a class?

- A) = default on copy ctor
- B) = delete on copy ctor and copy assignment
- C) explicit
- D) mutable

**Answer:** B — Deleting copy operations disables copying.

**Q74. [Medium] (True/False)** `constexpr` functions can be evaluated at compile time.

**Answer:** True — They may run at compile time given constant inputs.

**Q75. [Medium]** Output?

```cpp
const char* s = "hello";
std::cout << strlen(s);
```

- A) 5
- B) 6
- C) 4
- D) Error

**Answer:** A — `strlen` counts characters excluding the null terminator.

---

## Section C — Hard (Q76–Q100)

**Q76. [Hard]** What is undefined behavior in:

```cpp
int i = 0;
std::cout << i++ << i++;
```

- A) Always 01
- B) Unspecified/UB due to unsequenced modifications (pre-C++17 nuances)
- C) 10 always
- D) Compile error

**Answer:** B — Multiple unsequenced side effects on `i` cause UB/unspecified order.

**Q77. [Hard]** The "most vexing parse" refers to:

- A) A runtime crash
- B) A declaration being parsed as a function declaration instead of an object
- C) A template error
- D) A linker error

**Answer:** B — E.g., `Widget w();` is parsed as a function declaration.

**Q78. [Hard] (True/False)** `std::vector<bool>` is a space-optimized specialization that does not store real `bool` objects.

**Answer:** True — It packs bits, so it doesn't behave like a normal container of bool.

**Q79. [Hard]** SFINAE stands for:

- A) Standard Function In Namespace Access Error
- B) Substitution Failure Is Not An Error
- C) Static Function Inheritance
- D) Safe For New Allocation Errors

**Answer:** B — Failed template substitutions are silently ignored, not errors.

**Q80. [Hard]** What does `std::enable_if` enable?

- A) Runtime checks
- B) Conditional template overload selection via SFINAE
- C) Exception handling
- D) Memory pooling

**Answer:** B — It conditionally includes/excludes overloads.

**Q81. [Hard] (One word)** C++20 introduced **\_\_** as a cleaner alternative to SFINAE for constraints.

**Answer:** Concepts — Concepts constrain template parameters readably.

**Q82. [Hard]** Object slicing occurs when:

```cpp
Base b = derivedObject; // by value
```

- A) Nothing is lost
- B) The derived-specific part is sliced off
- C) It fails to compile
- D) It moves the object

**Answer:** B — Copying to a base by value drops the derived portion.

**Q83. [Hard]** A vtable is:

- A) A hash map
- B) A per-class table of virtual function pointers
- C) A stack frame
- D) A namespace

**Answer:** B — Objects hold a vptr to their class's vtable.

**Q84. [Hard] (True/False)** Calling a virtual function inside a constructor uses the current (base) class's version, not the derived override.

**Answer:** True — The object isn't fully derived during base construction.

**Q85. [Hard]** `noexcept` on a move constructor matters because:

- A) It's decorative
- B) STL containers use it to choose move vs copy during reallocation (strong guarantee)
- C) It disables moves
- D) It throws

**Answer:** B — `vector` moves only if the move ctor is `noexcept`.

**Q86. [Hard]** The strict aliasing rule implies:

- A) Any pointer can alias any type
- B) Accessing an object through an incompatible pointer type is UB
- C) All casts are safe
- D) Only const matters

**Answer:** B — Type-punning via incompatible pointers is undefined.

**Q87. [Hard] (One word)** The idiom that copies-and-swaps to implement exception-safe assignment is the copy-and-**\_\_** idiom.

**Answer:** swap — Copy-and-swap provides strong exception safety.

**Q88. [Hard]** Why can `std::shared_ptr` cycles cause leaks?

- A) They don't
- B) Mutual references keep reference counts > 0 forever
- C) The heap is full
- D) Weak pointers cause it

**Answer:** B — Cyclic ownership prevents counts from reaching zero (use `weak_ptr`).

**Q89. [Hard]** `virtual` inheritance solves the diamond problem by:

- A) Duplicating the base
- B) Creating one shared base subobject
- C) Removing the base
- D) Making it abstract

**Answer:** B — A single shared base avoids ambiguity/duplication.

**Q90. [Hard] (True/False)** In C++, the order of member initialization follows declaration order, not the initializer-list order.

**Answer:** True — Members initialize in declaration order regardless of list order.

**Q91. [Hard]** What does `std::launder` address?

- A) Formatting
- B) Reusing storage where the compiler might assume old object identity
- C) Threading
- D) I/O

**Answer:** B — It informs the compiler about object lifetime after placement-new reuse.

**Q92. [Hard]** ADL (Argument-Dependent Lookup) means:

- A) Functions are found in namespaces of their argument types
- B) All functions are global
- C) Only member functions are found
- D) Templates aren't considered

**Answer:** A — Name lookup includes associated namespaces of arguments.

**Q93. [Hard] (One word)** A universal/forwarding reference has the form `T&&` where T is a deduced template **\_\_**.

**Answer:** parameter — Deduced `T&&` is a forwarding reference.

**Q94. [Hard]** Output/behavior of returning a reference to a local variable:

- A) Safe
- B) Undefined behavior (dangling reference)
- C) Copies the value
- D) Compile error always

**Answer:** B — The local is destroyed; the reference dangles.

**Q95. [Hard]** `std::optional` represents:

- A) A pointer
- B) A value that may or may not be present
- C) A variant of types
- D) A thread

**Answer:** B — It models an optional (nullable) value type-safely.

**Q96. [Hard] (True/False)** `std::variant` is a type-safe union.

**Answer:** True — It holds one of several alternative types safely.

**Q97. [Hard]** Placement new is used to:

- A) Allocate on the heap
- B) Construct an object at a pre-allocated memory address
- C) Delete objects
- D) Overload operators

**Answer:** B — It constructs in provided storage without allocating.

**Q98. [Hard]** Empty Base Optimization (EBO) allows:

- A) Faster loops
- B) An empty base class to occupy no additional space in a derived object
- C) Larger objects
- D) Virtual dispatch removal

**Answer:** B — Empty bases add zero size to the derived type.

**Q99. [Hard] (One word)** The C++11 memory model uses `std::atomic` and memory **\_\_** to control ordering.

**Answer:** order — `std::memory_order` controls atomic ordering.

**Q100. [Hard]** Why is `std::endl` in a hot loop often discouraged?

- A) It doesn't print newline
- B) It flushes the buffer each time, hurting performance
- C) It's deprecated
- D) It throws

**Answer:** B — Frequent flushing is costly; prefer `'\n'`.
