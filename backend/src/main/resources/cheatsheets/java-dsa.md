# ☕ Java DSA Cheat Sheet

> A scan-friendly Java reference for coding interviews. Start with the decision tables, then use the smallest relevant snippet.

```java
import java.util.*;
import java.util.stream.Collectors;
```

---

## 🧭 Choose the right structure

| Need | Use | Typical cost |
| --- | --- | --- |
| Fixed-size indexed values | `int[]`, `char[]` | Access: O(1) |
| Resizable indexed values | `ArrayList<E>` | Access: O(1), append: amortized O(1) |
| Unique values, no order needed | `HashSet<E>` | Add/find/remove: average O(1) |
| Key → value lookup | `HashMap<K, V>` | Put/get/remove: average O(1) |
| Sorted keys or values | `TreeMap`, `TreeSet` | Operations: O(log n) |
| Stack or queue | `ArrayDeque<E>` | Add/remove at ends: O(1) |
| Repeated smallest/largest item | `PriorityQueue<E>` | Peek: O(1), add/remove: O(log n) |

> Interview default: start with `HashMap`, `HashSet`, `ArrayList`, or `ArrayDeque`. Choose a sorted structure only when the problem needs ordering, floor, or ceiling operations.

---

## 🔢 Numbers, types, and casting

### Primitive types you will use most

| Type | Size | Common use | Default field value | Wrapper |
| --- | --- | --- | --- | --- |
| `int` | 32-bit | Indices, counts, most values | `0` | `Integer` |
| `long` | 64-bit | Large sums, products, timestamps | `0L` | `Long` |
| `double` | 64-bit | Decimal calculations | `0.0` | `Double` |
| `char` | 16-bit | One UTF-16 code unit | `\u0000` | `Character` |
| `boolean` | JVM-dependent | Flags | `false` | `Boolean` |

```java
int count = 42;
long total = 10_000_000_000L; // L is required
double ratio = 3.14;
char letter = 'A';            // single quotes
boolean found = true;
```

Use `long` before an operation when the result might overflow `int`:

```java
long product = (long) a * b;
long sum = (long) a + b;

int intMax = Integer.MAX_VALUE;
long longMax = Long.MAX_VALUE;
```

### Parse and convert

```java
int n = Integer.parseInt("42");
long value = Long.parseLong("42");
double decimal = Double.parseDouble("3.14");

String text1 = String.valueOf(42);
String text2 = Integer.toString(42);
```

### Digits and letter indices

```java
int digit = '7' - '0';          // 7
char digitChar = (char) ('0' + 7); // '7'

int index = 'c' - 'a';          // 2
char letter = (char) ('a' + 2); // 'c'
```

### Division and casting

```java
int truncated = (int) 3.99; // 3
double wrong = 5 / 2;       // 2.0: integer division happened first
double correct = 5 / 2.0;   // 2.5
double alsoCorrect = (double) 5 / 2;
```

> `int` overflow is silent. Casting after an overflowing calculation is too late—cast an operand before the calculation.

---

## 🔡 Characters and math helpers

### Character checks

```java
Character.isDigit('5');          // true
Character.isLetter('a');         // true
Character.isLetterOrDigit('a');  // true
Character.isUpperCase('A');      // true
Character.toLowerCase('A');      // 'a'
Character.toUpperCase('a');      // 'A'
Character.getNumericValue('7');  // 7
```

### Math utilities

```java
Math.max(a, b);
Math.min(a, b);
Math.abs(-5);       // 5
Math.sqrt(16);      // 4.0
Math.floor(3.7);    // 3.0
Math.ceil(3.2);     // 4.0
Math.round(3.5);    // 4L
Math.pow(2, 10);    // 1024.0; always returns double
```

Safe midpoint for binary search:

```java
int mid = left + (right - left) / 2;
```

Avoid `(left + right) / 2`; `left + right` can overflow even when both values are valid integers.

---

## 📦 Arrays and lists

### One-dimensional arrays

Use an array when the size is known or fixed.

```java
int[] nums = {1, 2, 3, 4, 5};
int n = nums.length;

int[] dp = new int[n]; // int elements start at 0
Arrays.fill(dp, -1);

int[] copy = Arrays.copyOf(nums, nums.length);
```

Default array values are `0`, `0.0`, `false`, `\u0000`, or `null`, depending on the element type.

### Two-dimensional arrays

```java
int[][] grid = {
    {5, 9},
    {1, 2},
    {3, 7}
};

int rows = grid.length;
int cols = grid[0].length;

// Sort rows by column 1.
Arrays.sort(grid, (a, b) -> Integer.compare(a[1], b[1]));

// Sort by descending (column 1 - column 0).
Arrays.sort(grid, (a, b) ->
    Long.compare((long) b[1] - b[0], (long) a[1] - a[0])
);
```

> Prefer `Integer.compare(a, b)` over `a - b` in comparators because subtraction can overflow.

### `ArrayList`

Use an `ArrayList` when the collection must grow or shrink.

```java
List<Integer> numbers = new ArrayList<>();

numbers.add(10);          // append
numbers.add(0, 5);        // insert at index 0
int first = numbers.get(0);
numbers.set(0, 7);        // replace
numbers.remove(0);        // remove by index
numbers.remove(Integer.valueOf(10)); // remove the value 10

int size = numbers.size();
boolean empty = numbers.isEmpty();
```

> For `List<Integer>`, `remove(1)` removes the item at index `1`. Use `remove(Integer.valueOf(1))` to remove the value `1`.

---

## 📝 Strings and `StringBuilder`

Strings are immutable. Use `StringBuilder` when repeatedly changing or building text.

```java
String s = "abcd";

int n = s.length();
char ch = s.charAt(1);          // 'b'
String part = s.substring(1, 3); // "bc"; end index is exclusive
boolean same = s.equals("abcd");
char[] chars = s.toCharArray();
```

```java
StringBuilder sb = new StringBuilder();
sb.append("abc");
sb.append('d');
sb.setCharAt(0, 'A');
sb.deleteCharAt(sb.length() - 1);
sb.reverse();

String result = sb.toString();
sb.setLength(0); // clear
```

Common string operations:

```java
String[] parts = "a,b,c".split(",");
String joined = String.join("-", parts); // "a-b-c"
```

---

## ↔️ Stack, queue, and deque

Use `ArrayDeque` for both stacks and queues. It is the preferred interview choice over the legacy `Stack` class and usually over `LinkedList`.

### Stack: last in, first out

```java
Deque<Integer> stack = new ArrayDeque<>();

stack.push(10);       // add to top
stack.push(20);
int top = stack.peek();
int removed = stack.pop();
boolean empty = stack.isEmpty();
```

### Queue: first in, first out

```java
Queue<Integer> queue = new ArrayDeque<>();

queue.offer(10);      // add at back
queue.offer(20);
int front = queue.peek();
int removed = queue.poll();
```

Breadth-first search level pattern:

```java
while (!queue.isEmpty()) {
    int levelSize = queue.size();

    for (int i = 0; i < levelSize; i++) {
        int current = queue.poll();
        // Process current and offer its unvisited neighbors.
    }
}
```

### Deque: work at both ends

```java
Deque<Integer> deque = new ArrayDeque<>();

deque.offerFirst(1);
deque.offerLast(2);
deque.peekFirst();
deque.peekLast();
deque.pollFirst();
deque.pollLast();
```

`offer`, `poll`, and `peek` return a status or `null` instead of throwing when the operation cannot be completed.

---

## ⛰️ Priority queue (heap)

Use a heap when you repeatedly need the smallest or largest available item.

```java
// Min-heap: smallest value first.
PriorityQueue<Integer> minHeap = new PriorityQueue<>();

// Max-heap: largest value first.
PriorityQueue<Integer> maxHeap =
    new PriorityQueue<>(Comparator.reverseOrder());

minHeap.offer(5); // O(log n)
minHeap.offer(2);
int smallest = minHeap.peek(); // O(1)
int removed = minHeap.poll();  // O(log n)
```

Custom ordering:

```java
// Each item is {node, distance}; shortest distance first.
PriorityQueue<int[]> pq = new PriorityQueue<>(
    (a, b) -> Integer.compare(a[1], b[1])
);
```

> A `PriorityQueue` only guarantees that the top item has the highest priority. Iterating over it does not produce fully sorted order.

---

## 🔍 Sets

### Choose a set

| Type | Ordering | Typical cost | Use when |
| --- | --- | --- | --- |
| `HashSet` | None | Average O(1) | You only need uniqueness or membership |
| `LinkedHashSet` | Insertion order | Average O(1) | Iteration order must match insertion order |
| `TreeSet` | Sorted | O(log n) | You need sorted values, floor, or ceiling |

### Essential operations

```java
Set<Integer> seen = new HashSet<>();

seen.add(1);       // false if 1 was already present
seen.contains(1);
seen.remove(1);
seen.size();
seen.isEmpty();
```

Useful `TreeSet` operations:

```java
TreeSet<Integer> sorted = new TreeSet<>();
sorted.addAll(List.of(2, 5, 8));

sorted.first();    // 2
sorted.last();     // 8
sorted.floor(6);   // 5: largest value <= 6
sorted.ceiling(6); // 8: smallest value >= 6
```

---

## 🗺️ Maps — the interview essentials

A map stores one value for each unique key:

```text
word -> frequency    node -> neighbors    number -> index
```

### 1. Choose the map

| Type | Key order | Typical cost | Choose it when |
| --- | --- | --- | --- |
| `HashMap` | No guaranteed order | Average O(1) | Default choice for lookup/counting |
| `LinkedHashMap` | Insertion order | Average O(1) | Output must follow insertion order |
| `TreeMap` | Sorted by key | O(log n) | You need sorted keys, floor, or ceiling |

### 2. Learn six operations

```java
Map<Integer, String> map = new HashMap<>();

map.put(1, "one");                     // insert or replace
String value = map.get(1);              // null when missing
String safe = map.getOrDefault(2, "unknown");
boolean exists = map.containsKey(1);
map.remove(1);
int size = map.size();
```

`getOrDefault` returns a fallback but does not insert it into the map.

### 3. Remember three patterns

#### Pattern A: frequency count

```java
Map<Integer, Integer> frequency = new HashMap<>();

for (int number : nums) {
    frequency.merge(number, 1, Integer::sum);
}
```

#### Pattern B: grouping or adjacency list

```java
Map<Integer, List<Integer>> graph = new HashMap<>();

for (int[] edge : edges) {
    int from = edge[0];
    int to = edge[1];

    graph.computeIfAbsent(from, key -> new ArrayList<>()).add(to);
}
```

#### Pattern C: value to index lookup

```java
Map<Integer, Integer> indexByValue = new HashMap<>();

for (int i = 0; i < nums.length; i++) {
    int needed = target - nums[i];

    if (indexByValue.containsKey(needed)) {
        return new int[] {indexByValue.get(needed), i};
    }

    indexByValue.put(nums[i], i);
}
```

### 4. Iterate

```java
for (Map.Entry<Integer, String> entry : map.entrySet()) {
    int key = entry.getKey();
    String value = entry.getValue();
}
```

Use `keySet()` when only keys are needed and `values()` when only values are needed.

### 5. Remember

- Keys are unique; putting the same key again replaces its value.
- Map keys and values use objects such as `Integer`, not primitives such as `int`.
- `containsValue` is O(n); maps are optimized for key lookup.
- Unboxing a missing `Integer` value causes `NullPointerException`.
- Do not structurally modify a map inside a for-each loop.

> For most interview questions, these six operations plus `merge` and `computeIfAbsent` are enough.

---

## ⚖️ Sorting and comparators

### Arrays and lists

```java
int[] nums = {3, 1, 2};
Arrays.sort(nums); // ascending

Integer[] boxed = {3, 1, 2};
Arrays.sort(boxed, Comparator.reverseOrder());

List<Integer> values = new ArrayList<>(List.of(3, 1, 2));
values.sort(Comparator.naturalOrder());
values.sort(Comparator.reverseOrder());
```

### Sort by fields

```java
people.sort(Comparator.comparingInt(person -> person.age));

Arrays.sort(intervals,
    Comparator.comparingInt((int[] interval) -> interval[0])
              .thenComparingInt(interval -> interval[1])
);
```

Descending numeric comparator:

```java
values.sort((a, b) -> Integer.compare(b, a));
```

> Comparators must be consistent. Use `Integer.compare`, `Long.compare`, or `Comparator.comparing...` instead of subtracting values.

---

## 🔁 Common conversions

```java
// int[] -> List<Integer>
List<Integer> list = Arrays.stream(nums)
    .boxed()
    .collect(Collectors.toList());

// List<Integer> -> int[]
int[] array = list.stream()
    .mapToInt(Integer::intValue)
    .toArray();

// char[] <-> String
char[] chars = text.toCharArray();
String rebuilt = new String(chars);

// Array -> modifiable list
List<Integer> mutable = new ArrayList<>(Arrays.asList(1, 2, 3));
```

Useful collection helpers:

```java
Collections.max(list);
Collections.min(list);
Collections.reverse(list);
Collections.swap(list, i, j);
Collections.frequency(list, value);

Arrays.fill(array, -1);
Arrays.copyOf(array, newLength);
```

> `Arrays.asList(...)` has a fixed size, and `List.of(...)` is immutable. Wrap either in `new ArrayList<>(...)` when additions or removals are needed.

---

## ⚠️ Easy-to-forget interview rules

| Rule | Remember |
| --- | --- |
| Equality | Use `.equals()` for objects and strings; `==` compares object references |
| Null-safe equality | Use `Objects.equals(a, b)` when either value may be `null` |
| Array length | `array.length` |
| String length | `string.length()` |
| Collection size | `list.size()`, `set.size()`, `map.size()` |
| Integer division | `5 / 2` is `2`; use `5 / 2.0` for `2.5` |
| Overflow | Promote to `long` before addition or multiplication |
| Midpoint | `left + (right - left) / 2` |
| Comparator | Use `Integer.compare(a, b)`, not `a - b` |
| Mutation during iteration | Use an `Iterator` for structural removal |
| Null unboxing | A missing `Integer` unboxed to `int` throws `NullPointerException` |

```java
String a = "hello";
String b = new String("hello");

a == b;             // false: different references
a.equals(b);        // true: same content
Objects.equals(a, b); // true and null-safe
```

### Final interview checklist

Before submitting, quickly ask:

1. Can a sum, product, or comparator overflow?
2. Is an index inside the valid range?
3. Can this lookup return `null`?
4. Am I modifying a collection while iterating?
5. Did I choose O(1) lookup when repeated searches are required?
6. Did I state the time and space complexity?
