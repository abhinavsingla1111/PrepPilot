# Object-Oriented Programming (OOP) — 100 Interview MCQs

> Format: Each question has a difficulty tag `[Easy] / [Medium] / [Hard]`, options (or True/False, or one-word), the correct answer, and a one-line justification.

---

## Section A — Easy (Q1–Q35)

**Q1. [Easy]** Which of the following is NOT one of the four pillars of OOP?

- A) Encapsulation
- B) Inheritance
- C) Compilation
- D) Polymorphism

**Answer:** C — The four pillars are Encapsulation, Inheritance, Polymorphism, and Abstraction; compilation is not one.

**Q2. [Easy]** What is an object?

- A) A blueprint for a class
- B) An instance of a class
- C) A global function
- D) A namespace

**Answer:** B — An object is a concrete instance created from a class blueprint.

**Q3. [Easy]** A class is best described as:

- A) A runtime memory block
- B) A template/blueprint defining state and behavior
- C) An interface only
- D) A compiled binary

**Answer:** B — A class defines the structure (fields) and behavior (methods) for its objects.

**Q4. [Easy]** Encapsulation primarily achieves:

- A) Faster compilation
- B) Data hiding and bundling data with methods
- C) Multiple inheritance
- D) Automatic garbage collection

**Answer:** B — Encapsulation bundles data and methods while restricting direct access to internal state.

**Q5. [Easy] (True/False)** Inheritance promotes code reuse.

**Answer:** True — A subclass reuses fields and methods of its superclass.

**Q6. [Easy]** Which access modifier makes a member accessible only within its own class?

- A) public
- B) protected
- C) private
- D) default

**Answer:** C — `private` restricts access to the declaring class itself.

**Q7. [Easy]** The ability of a single interface to represent different underlying forms is called:

- A) Abstraction
- B) Polymorphism
- C) Encapsulation
- D) Composition

**Answer:** B — Polymorphism lets one interface operate on many types/forms.

**Q8. [Easy] (One word)** The special method used to initialize an object is called a **\_\_**.

**Answer:** Constructor — It initializes an object's state at creation.

**Q9. [Easy]** Abstraction is best defined as:

- A) Hiding implementation and exposing only essential features
- B) Copying objects
- C) Overloading operators
- D) Freeing memory

**Answer:** A — Abstraction exposes the "what" while hiding the "how".

**Q10. [Easy]** "IS-A" relationship is modeled by:

- A) Composition
- B) Aggregation
- C) Inheritance
- D) Association

**Answer:** C — Inheritance expresses an IS-A relationship (Dog IS-A Animal).

**Q11. [Easy]** "HAS-A" relationship is modeled by:

- A) Inheritance
- B) Composition/Aggregation
- C) Polymorphism
- D) Abstraction

**Answer:** B — Composition/aggregation models HAS-A (Car HAS-A Engine).

**Q12. [Easy] (True/False)** A subclass can override a method of its superclass.

**Answer:** True — Overriding provides a subclass-specific implementation.

**Q13. [Easy]** Method overloading is an example of:

- A) Runtime polymorphism
- B) Compile-time polymorphism
- C) Inheritance
- D) Encapsulation

**Answer:** B — Overloading is resolved at compile time based on signatures.

**Q14. [Easy]** Method overriding is an example of:

- A) Compile-time polymorphism
- B) Runtime polymorphism
- C) Static binding
- D) Operator overloading

**Answer:** B — Overriding is resolved dynamically at runtime.

**Q15. [Easy]** Which keyword typically refers to the current object?

- A) self/this
- B) base
- C) new
- D) static

**Answer:** A — `this` (or `self` in Python) references the current instance.

**Q16. [Easy]** A destructor/finalizer is used to:

- A) Create objects
- B) Release resources before object destruction
- C) Overload operators
- D) Cast types

**Answer:** B — It cleans up resources when an object's lifecycle ends.

**Q17. [Easy] (True/False)** Abstract classes can have both abstract and concrete methods.

**Answer:** True — Abstract classes may mix unimplemented and implemented methods.

**Q18. [Easy]** An interface (in classic OOP) typically contains:

- A) Only fully implemented methods
- B) Only method signatures (contract)
- C) Only private fields
- D) Only constructors

**Answer:** B — An interface declares a contract of method signatures.

**Q19. [Easy]** Which is a benefit of encapsulation?

- A) Increased coupling
- B) Controlled access via getters/setters
- C) Slower runtime
- D) Loss of modularity

**Answer:** B — Getters/setters give controlled, validated access to internal state.

**Q20. [Easy] (One word)** A method with the same name but different parameters demonstrates method **\_\_**.

**Answer:** Overloading — Same name, differing parameter lists.

**Q21. [Easy]** A static member belongs to:

- A) Each individual object
- B) The class itself
- C) The garbage collector
- D) The constructor only

**Answer:** B — Static members are shared at the class level, not per object.

**Q22. [Easy] (True/False)** Composition is generally preferred over inheritance for flexibility.

**Answer:** True — "Favor composition over inheritance" reduces tight coupling.

**Q23. [Easy]** Which relationship is the weakest coupling?

- A) Inheritance
- B) Composition
- C) Association
- D) Aggregation

**Answer:** C — Association is a loose "uses-a" link between objects.

**Q24. [Easy]** A concrete class is one that:

- A) Cannot be instantiated
- B) Can be instantiated
- C) Has only abstract methods
- D) Is always static

**Answer:** B — A concrete class provides implementations and can be instantiated.

**Q25. [Easy] (One word)** The principle of restricting direct access to some of an object's components is **\_\_**.

**Answer:** Encapsulation — It hides internal representation.

**Q26. [Easy]** Which of these is a valid reason to use getters and setters?

- A) To make all fields public
- B) To validate and control access to fields
- C) To increase memory usage
- D) To avoid inheritance

**Answer:** B — They enforce validation and encapsulation.

**Q27. [Easy] (True/False)** Every class in Java implicitly inherits from `Object`.

**Answer:** True — `java.lang.Object` is the root of the class hierarchy.

**Q28. [Easy]** Polymorphism literally means:

- A) One shape
- B) Many forms
- C) No form
- D) Fixed form

**Answer:** B — From Greek "poly" (many) + "morph" (form).

**Q29. [Easy]** A base/parent class is also called a:

- A) Subclass
- B) Superclass
- C) Nested class
- D) Anonymous class

**Answer:** B — The class being inherited from is the superclass.

**Q30. [Easy]** A derived class is also called a:

- A) Superclass
- B) Subclass
- C) Abstract class
- D) Sealed class

**Answer:** B — The inheriting class is the subclass.

**Q31. [Easy] (True/False)** Constructors can be overloaded.

**Answer:** True — Multiple constructors with different parameter lists are allowed.

**Q32. [Easy]** Which pillar helps reduce complexity by hiding details?

- A) Inheritance
- B) Abstraction
- C) Polymorphism
- D) Serialization

**Answer:** B — Abstraction hides complexity and exposes essentials.

**Q33. [Easy]** In UML, a hollow triangle arrow represents:

- A) Composition
- B) Inheritance/generalization
- C) Dependency
- D) Aggregation

**Answer:** B — A hollow triangle denotes generalization (inheritance).

**Q34. [Easy] (One word)** Creating an object from a class is called **\_\_**.

**Answer:** Instantiation — Allocating and initializing an object.

**Q35. [Easy]** Which is true about abstraction vs encapsulation?

- A) They are identical
- B) Abstraction hides complexity; encapsulation hides data
- C) Both hide only data
- D) Neither relates to hiding

**Answer:** B — Abstraction hides design/complexity; encapsulation hides internal state.

---

## Section B — Medium (Q36–Q75)

**Q36. [Medium]** Consider the Java snippet:

```java
class A { void show(){ System.out.println("A"); } }
class B extends A { void show(){ System.out.println("B"); } }
A obj = new B();
obj.show();
```

Output?

- A) A
- B) B
- C) Compile error
- D) Runtime error

**Answer:** B — Dynamic dispatch calls the overridden method of the actual object (B).

**Q37. [Medium]** Which enables runtime polymorphism in most OOP languages?

- A) Static methods
- B) Virtual methods / dynamic dispatch
- C) Final methods
- D) Constructors

**Answer:** B — Virtual method tables (vtables) enable dynamic dispatch.

**Q38. [Medium] (True/False)** In Java, a `final` method cannot be overridden.

**Answer:** True — `final` prevents overriding in subclasses.

**Q39. [Medium]** The Liskov Substitution Principle states:

- A) Subtypes must be substitutable for their base types
- B) Classes must be final
- C) Interfaces must be small
- D) Prefer inheritance over composition

**Answer:** A — Objects of a superclass should be replaceable with subclass objects without breaking behavior.

**Q40. [Medium]** The "S" in SOLID stands for:

- A) Substitution
- B) Single Responsibility Principle
- C) Static binding
- D) Serialization

**Answer:** B — A class should have only one reason to change.

**Q41. [Medium]** The Open/Closed Principle means software entities should be:

- A) Open for modification, closed for extension
- B) Open for extension, closed for modification
- C) Always modifiable
- D) Never extended

**Answer:** B — Extend behavior without altering existing tested code.

**Q42. [Medium]** The Dependency Inversion Principle recommends depending on:

- A) Concrete classes
- B) Abstractions, not concretions
- C) Global variables
- D) Static utilities

**Answer:** B — High-level modules should depend on abstractions.

**Q43. [Medium]** The Interface Segregation Principle advocates:

- A) One large interface
- B) Many small, client-specific interfaces
- C) No interfaces
- D) Only abstract classes

**Answer:** B — Clients shouldn't depend on methods they don't use.

**Q44. [Medium]** What is the diamond problem?

- A) A memory leak
- B) Ambiguity from multiple inheritance of the same base
- C) A sorting issue
- D) A concurrency bug

**Answer:** B — Multiple inheritance can create ambiguous inherited members from a shared ancestor.

**Q45. [Medium]** How does Java avoid the diamond problem for classes?

- A) Allows multiple class inheritance
- B) Disallows multiple class inheritance; uses interfaces
- C) Uses pointers
- D) Uses friend classes

**Answer:** B — Java permits single class inheritance but multiple interface implementation.

**Q46. [Medium] (True/False)** An abstract class can have a constructor.

**Answer:** True — Its constructor runs when a subclass is instantiated (via `super`).

**Q47. [Medium]** Which best describes "association" vs "aggregation" vs "composition" lifecycle coupling (weak→strong)?

- A) Composition < Aggregation < Association
- B) Association < Aggregation < Composition
- C) Aggregation < Association < Composition
- D) All equal

**Answer:** B — Composition implies strongest ownership (part dies with whole).

**Q48. [Medium]** In composition, if the container is destroyed:

- A) Parts survive independently
- B) Parts are also destroyed (owned lifecycle)
- C) Parts are garbage only sometimes
- D) Parts become static

**Answer:** B — Composition means the part cannot exist without the whole.

**Q49. [Medium]** Method hiding (as opposed to overriding) occurs with:

- A) Instance virtual methods
- B) Static methods (resolved by reference type)
- C) Abstract methods
- D) Constructors

**Answer:** B — Static methods are bound to the reference's compile-time type, hiding not overriding.

**Q50. [Medium]** Given C++:

```cpp
class Base { public: void f(){} virtual void g(){} };
```

Which call uses dynamic dispatch?

- A) f()
- B) g()
- C) Both
- D) Neither

**Answer:** B — Only `virtual` functions use the vtable for dynamic dispatch.

**Q51. [Medium] (True/False)** An interface can extend multiple interfaces in Java.

**Answer:** True — Interfaces support multiple inheritance of type.

**Q52. [Medium]** Covariant return types allow an overriding method to:

- A) Change parameter count
- B) Return a subtype of the original return type
- C) Change access to private
- D) Throw broader checked exceptions

**Answer:** B — Overrides may narrow the return type to a subtype.

**Q53. [Medium]** Which is TRUE about constructors and inheritance?

- A) Constructors are inherited
- B) Subclass constructor implicitly calls the superclass constructor
- C) Superclass constructor runs last
- D) Constructors cannot call other constructors

**Answer:** B — The base constructor runs first (implicit `super()`).

**Q54. [Medium]** Upcasting refers to:

- A) Casting a subclass reference to a superclass type
- B) Casting a superclass to subclass
- C) Casting int to double
- D) Boxing

**Answer:** A — Treating a derived object as its base type (always safe).

**Q55. [Medium]** Downcasting is:

- A) Always safe
- B) Casting a base reference to a derived type (may fail at runtime)
- C) Impossible
- D) A compile-only operation

**Answer:** B — It requires the object to actually be of the derived type.

**Q56. [Medium] (One word)** The design principle "Don't call us, we'll call you" is known as **\_\_** of Control.

**Answer:** Inversion — Inversion of Control (IoC).

**Q57. [Medium]** A pure virtual function in C++ (`= 0`) makes the class:

- A) Concrete
- B) Abstract
- C) Final
- D) Static

**Answer:** B — Any class with a pure virtual function is abstract.

**Q58. [Medium]** Which best explains "cohesion"?

- A) Degree to which module elements belong together
- B) Number of subclasses
- C) Memory footprint
- D) Number of interfaces

**Answer:** A — High cohesion = focused, single-purpose modules.

**Q59. [Medium]** Low coupling is desirable because:

- A) It increases dependencies
- B) It improves maintainability and testability
- C) It reduces cohesion
- D) It forces inheritance

**Answer:** B — Loosely coupled modules change independently.

**Q60. [Medium] (True/False)** In Java, you can override a `static` method.

**Answer:** False — Static methods are hidden, not overridden.

**Q61. [Medium]** Which design pattern ensures a class has only one instance?

- A) Factory
- B) Singleton
- C) Observer
- D) Strategy

**Answer:** B — Singleton restricts instantiation to one object.

**Q62. [Medium]** The Factory pattern primarily:

- A) Clones objects
- B) Encapsulates object creation
- C) Notifies observers
- D) Adds responsibilities dynamically

**Answer:** B — It abstracts and centralizes instantiation logic.

**Q63. [Medium]** The Strategy pattern is used to:

- A) Choose an algorithm at runtime
- B) Guarantee a single instance
- C) Compose tree structures
- D) Cache results

**Answer:** A — It encapsulates interchangeable algorithms behind an interface.

**Q64. [Medium]** The Observer pattern models:

- A) One-to-many dependency with notifications
- B) One instance
- C) Algorithm selection
- D) Object cloning

**Answer:** A — Subjects notify subscribed observers of state changes.

**Q65. [Medium]** The Decorator pattern:

- A) Adds behavior to objects dynamically without subclassing
- B) Ensures one instance
- C) Selects strategies
- D) Builds trees

**Answer:** A — It wraps objects to extend behavior at runtime.

**Q66. [Medium] (True/False)** Polymorphism can help satisfy the Open/Closed Principle.

**Answer:** True — New subtypes extend behavior without modifying existing code.

**Q67. [Medium]** In Python, method resolution order (MRO) uses which algorithm?

- A) DFS
- B) C3 linearization
- C) BFS
- D) Random

**Answer:** B — Python resolves multiple inheritance via C3 linearization.

**Q68. [Medium]** What does `super()` do in a subclass method?

- A) Deletes the parent
- B) Invokes the parent class implementation
- C) Creates a new object
- D) Makes a method static

**Answer:** B — It delegates to the superclass's version of a method/constructor.

**Q69. [Medium]** Which is an example of compile-time (static) binding?

- A) Virtual method call
- B) Overloaded method resolution
- C) Interface dispatch
- D) Abstract method call

**Answer:** B — Overload resolution happens at compile time.

**Q70. [Medium] (One word)** An object that cannot change state after creation is called **\_\_**.

**Answer:** Immutable — Its state is fixed after construction.

**Q71. [Medium]** Which promotes immutability?

- A) Public mutable fields
- B) Final/readonly fields and no setters
- C) Global variables
- D) Static mutable state

**Answer:** B — Read-only fields and defensive copies enforce immutability.

**Q72. [Medium]** The Template Method pattern:

- A) Defines skeleton of an algorithm, deferring steps to subclasses
- B) Guarantees a single instance
- C) Notifies observers
- D) Wraps objects

**Answer:** A — The base defines the algorithm outline; subclasses fill steps.

**Q73. [Medium] (True/False)** Aggregation implies ownership where parts cannot exist without the whole.

**Answer:** False — That describes composition; aggregation allows independent lifecycles.

**Q74. [Medium]** Which keyword prevents a class from being subclassed in Java?

- A) static
- B) final
- C) abstract
- D) sealed (pre-17)

**Answer:** B — A `final` class cannot be extended.

**Q75. [Medium]** Encapsulation is violated most by:

- A) Private fields with validated setters
- B) Public mutable fields exposed directly
- C) Immutable objects
- D) Getters returning copies

**Answer:** B — Exposing raw mutable state breaks controlled access.

---

## Section C — Hard (Q76–Q100)

**Q76. [Hard]** In C++, calling a virtual method from within a constructor:

- A) Uses the derived override
- B) Uses the version for the type under construction (base)
- C) Causes undefined behavior always
- D) Is not allowed

**Answer:** B — During base construction, the object is not yet the derived type, so base's version runs.

**Q77. [Hard]** In C++, a base class destructor should be `virtual` when:

- A) Never
- B) The class is used polymorphically (deleted via base pointer)
- C) It has no members
- D) It is abstract only

**Answer:** B — A virtual destructor ensures the derived destructor runs on `delete base_ptr`.

**Q78. [Hard]** Given Java:

```java
class A { A(){ print(); } void print(){ System.out.println("A"); } }
class B extends A { int x = 5; void print(){ System.out.println(x); } }
new B();
```

Output?

- A) 5
- B) 0
- C) A
- D) Compile error

**Answer:** B — `A`'s constructor calls the overridden `print()`, but `B.x` is still 0 (not yet initialized).

**Q79. [Hard]** The Fragile Base Class problem refers to:

- A) Base changes unexpectedly breaking derived classes
- B) A slow constructor
- C) A missing interface
- D) A memory leak

**Answer:** A — Seemingly safe base modifications can break subclasses relying on internals.

**Q80. [Hard] (True/False)** In Java, overriding methods may reduce the visibility of the overridden method.

**Answer:** False — Overrides cannot be more restrictive than the parent's access level.

**Q81. [Hard]** Object slicing in C++ occurs when:

- A) A derived object is assigned to a base object by value
- B) Using pointers
- C) Using references
- D) Using virtual functions

**Answer:** A — Copying a derived into a base-by-value loses the derived portion.

**Q82. [Hard]** Which SOLID principle does a "God object" most violate?

- A) Liskov Substitution
- B) Single Responsibility
- C) Interface Segregation
- D) Dependency Inversion

**Answer:** B — A God object handles too many responsibilities.

**Q83. [Hard]** Covariance and contravariance most directly relate to:

- A) Memory layout
- B) Type compatibility of generics/parameters and returns
- C) Garbage collection
- D) Thread scheduling

**Answer:** B — They define subtyping direction for parameters/returns and generics.

**Q84. [Hard]** In the Curiously Recurring Template Pattern (CRTP), a class:

- A) Inherits from a template instantiated with itself
- B) Uses runtime polymorphism
- C) Is always abstract
- D) Avoids templates

**Answer:** A — `class D : public Base<D>` enables static polymorphism.

**Q85. [Hard] (True/False)** Dynamic dispatch in C++ typically incurs a vtable pointer indirection cost.

**Answer:** True — Virtual calls resolve through a per-object vptr to the vtable.

**Q86. [Hard]** Which is TRUE about `equals()`/`hashCode()` contract in Java?

- A) Equal objects may have different hash codes
- B) Equal objects must have equal hash codes
- C) hashCode must be unique
- D) equals must use ==

**Answer:** B — Consistency requires equal objects to share the same hash code.

**Q87. [Hard]** A mixin is best described as:

- A) A class providing methods to be reused across unrelated classes
- B) A singleton
- C) A destructor
- D) A private field

**Answer:** A — Mixins add reusable behavior without forming a strict IS-A hierarchy.

**Q88. [Hard]** Double dispatch is commonly implemented via:

- A) The Visitor pattern
- B) The Singleton pattern
- C) The Adapter pattern
- D) The Proxy pattern

**Answer:** A — Visitor achieves dispatch on two runtime types.

**Q89. [Hard]** In Java generics, `List<String>` is NOT a subtype of `List<Object>` because generics are:

- A) Covariant
- B) Invariant (by default)
- C) Contravariant
- D) Bivariant

**Answer:** B — Java generics are invariant unless wildcards are used.

**Q90. [Hard] (One word)** The wildcard `? extends T` in Java provides **\_\_** (co/contra)-variance.

**Answer:** Covariance — `? extends T` is a covariant (producer) bound.

**Q91. [Hard]** `? super T` in Java is used for:

- A) Producing values (read)
- B) Consuming values (write) — contravariance
- C) Invariance
- D) Type erasure removal

**Answer:** B — PECS: "Producer Extends, Consumer Super."

**Q92. [Hard] (True/False)** Type erasure in Java means generic type parameters are unavailable at runtime.

**Answer:** True — Generics are erased to raw types after compilation.

**Q93. [Hard]** Which statement about abstract classes vs interfaces (modern Java) is correct?

- A) Interfaces cannot have any implemented methods
- B) Interfaces can have default and static methods; abstract classes can hold state
- C) Abstract classes cannot have constructors
- D) A class can extend multiple abstract classes

**Answer:** B — Since Java 8 interfaces allow default/static methods; abstract classes keep fields/constructors.

**Q94. [Hard]** The Bridge pattern's core intent is to:

- A) Decouple an abstraction from its implementation so both vary independently
- B) Ensure single instance
- C) Add responsibilities dynamically
- D) Traverse collections

**Answer:** A — Bridge separates abstraction and implementation hierarchies.

**Q95. [Hard]** In multiple inheritance (C++), virtual (diamond) base classes ensure:

- A) Two separate base subobjects
- B) A single shared base subobject
- C) No base subobject
- D) Static base only

**Answer:** B — `virtual` inheritance yields one shared base instance.

**Q96. [Hard] (True/False)** Composition can simulate multiple inheritance without the diamond problem.

**Answer:** True — Delegating to contained objects avoids ambiguous shared bases.

**Q97. [Hard]** Which is a drawback of deep inheritance hierarchies?

- A) Improved flexibility
- B) Increased fragility and tight coupling
- C) Better performance always
- D) Reduced code size guaranteed

**Answer:** B — Deep hierarchies become brittle and hard to change.

**Q98. [Hard]** The "tell, don't ask" principle encourages:

- A) Querying object state then acting externally
- B) Telling objects to perform behavior (encapsulated logic)
- C) Public fields
- D) Global state

**Answer:** B — Behavior should live with the data it operates on.

**Q99. [Hard]** Which best captures the Law of Demeter?

- A) A method should talk only to immediate friends (avoid long call chains)
- B) Always use inheritance
- C) Minimize class count
- D) Prefer static methods

**Answer:** A — It reduces coupling by limiting knowledge of object internals (`a.getB().getC().doX()` is a smell).

**Q100. [Hard]** In dynamic dispatch, the method invoked is determined by:

- A) The reference's static (declared) type
- B) The object's actual runtime type
- C) The method's return type
- D) The package name

**Answer:** B — Runtime polymorphism dispatches on the object's actual type.
