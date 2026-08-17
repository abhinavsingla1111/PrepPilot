# ⚡ C++ DSA Cheat Sheet

> Quick reference for coding interviews. Skim the tables, copy the snippets.

---

## 🔢 Primitive Data Types

| Type        | Size   | Range (typical)                     |
| ----------- | ------ | ----------------------------------- |
| `int`       | 32-bit | -2,147,483,648 … 2,147,483,647      |
| `long long` | 64-bit | ±9.2 × 10¹⁸                          |
| `char`      | 8-bit  | -128 … 127 (or 0 … 255 `unsigned`)  |
| `bool`      | 1-byte | `true` / `false`                    |
| `float`     | 32-bit | ~7 decimal digits                   |
| `double`    | 64-bit | ~15–16 decimal digits               |

```cpp
long long big = 10000000000LL;   // needs LL
double d = 3.14;
char c = 'A';
bool ok = true;
```

**Limits** (`#include <climits>`, `<cfloat>`)
```cpp
INT_MAX      // 2147483647
INT_MIN      // -2147483648
LLONG_MAX    // 9223372036854775807
INT_MAX + 1  // OVERFLOW -> use long long
```

---

## 🔄 Casting & Conversion

```cpp
int x = (int) 3.99;          // 3 (truncates)
int x = static_cast<int>(d);

// string <-> number
int n = stoi("42");
long long m = stoll("42");
double f = stod("3.14");
string s = to_string(42);

// char <-> int
int digit = '7' - '0';       // 7
char ch = '0' + 7;           // '7'
int idx = 'c' - 'a';         // 2
```

> ⚠️ **Integer division truncates**: `5 / 2 == 2`. Use `(double)5 / 2` for `2.5`.
> ⚠️ **Overflow is silent** — use `long long` for products/sums that may exceed 2·10⁹.

---

## 🔡 Strings (`std::string`)

```cpp
#include <string>
string s = "abcd";
s.size();  s.length();       // 4
s[0];  s.back();  s.front();
s.substr(1, 2);              // "bc"  (start, len)
s += "ef";                   // append
s.push_back('x');
s.find("bc");                // index or string::npos
reverse(s.begin(), s.end());
sort(s.begin(), s.end());

// build efficiently
string out;
out.reserve(n);
out += 'a';

// to char array / iterate
for (char ch : s) { ... }
```

---

## 📋 Arrays & Vectors

```cpp
#include <vector>
vector<int> v = {1, 2, 3};
v.push_back(4);              // O(1) amortized
v.pop_back();
v.size();  v.empty();
v[0];  v.back();  v.front();
v.insert(v.begin(), 0);      // O(n)
v.erase(v.begin() + 1);      // O(n)

vector<int> dp(n, -1);       // size n, filled -1
vector<vector<int>> grid(r, vector<int>(c, 0));   // 2D

// iterate
for (int x : v) { ... }
for (int& x : v) x *= 2;     // by reference to modify

// C-style array
int arr[5] = {0};
int n = sizeof(arr) / sizeof(arr[0]);
```

---

## 🔗 pair & tuple

```cpp
pair<int, int> p = {3, 4};
p.first;  p.second;
auto [a, b] = p;             // structured binding (C++17)

tuple<int, string, int> t = {1, "x", 2};
auto [id, name, val] = t;

vector<pair<int,int>> edges;
edges.push_back({1, 2});
```

---

## 🗺️ Maps

```cpp
#include <map>
#include <unordered_map>

unordered_map<int, int> m;   // hash map, avg O(1)
map<int, int> ordered;        // sorted by key, O(log n)

m[1] = 10;                    // insert / update
m[2]++;                       // default 0 then ++
m.count(1);                   // 1 if present
m.find(1) != m.end();         // exists check
m.erase(1);
m.size();

for (auto& [k, v] : m) { ... }

// frequency count
unordered_map<char, int> freq;
for (char c : s) freq[c]++;
```

---

## 🎯 Sets

```cpp
#include <set>
#include <unordered_set>

unordered_set<int> seen;     // avg O(1)
set<int> sorted;              // sorted, O(log n)
multiset<int> ms;            // allows duplicates

seen.insert(1);
seen.count(1);               // 0 or 1
seen.erase(1);

// set extras (sorted)
*sorted.begin();             // smallest
*sorted.rbegin();            // largest
sorted.lower_bound(5);       // first >= 5
sorted.upper_bound(5);       // first > 5
```

---

## 🧱 Stack, Queue, Deque

```cpp
#include <stack>
#include <queue>
#include <deque>

stack<int> st;
st.push(1); st.top(); st.pop(); st.empty();

queue<int> q;
q.push(1); q.front(); q.pop();

deque<int> dq;               // both ends O(1)
dq.push_front(0); dq.push_back(1);
dq.pop_front(); dq.pop_back();
dq.front(); dq.back();
```

---

## ⛰️ priority_queue (Heap)

```cpp
#include <queue>

// MAX-heap (largest on top) — DEFAULT
priority_queue<int> maxHeap;

// MIN-heap
priority_queue<int, vector<int>, greater<int>> minHeap;

pq.push(5);      // add        O(log n)
pq.top();        // peek        O(1)
pq.pop();        // remove top  O(log n)
pq.size();

// custom comparator (min-heap by pair.second)
auto cmp = [](auto& a, auto& b){ return a.second > b.second; };
priority_queue<pair<int,int>, vector<pair<int,int>>, decltype(cmp)> pq(cmp);
```

---

## ⚖️ Sorting & Comparators

```cpp
#include <algorithm>
sort(v.begin(), v.end());                 // ascending
sort(v.begin(), v.end(), greater<int>()); // descending

// custom
sort(v.begin(), v.end(), [](int a, int b){
    return a > b;            // return true if a should come first
});

// sort pairs by second, tie-break by first
sort(v.begin(), v.end(), [](auto& a, auto& b){
    if (a.second != b.second) return a.second < b.second;
    return a.first < b.first;
});
```

---

## 🧮 Handy STL Algorithms

```cpp
#include <algorithm>
#include <numeric>

max(a, b);  min(a, b);
*max_element(v.begin(), v.end());
*min_element(v.begin(), v.end());
accumulate(v.begin(), v.end(), 0);        // sum (0 = long long? use 0LL)
count(v.begin(), v.end(), x);
reverse(v.begin(), v.end());
lower_bound(v.begin(), v.end(), x);       // first >= x (sorted)
upper_bound(v.begin(), v.end(), x);       // first > x
fill(v.begin(), v.end(), 0);
__gcd(12, 8);                             // 4
abs(-5);  pow(2, 10);                     // pow returns double
```

---

## ⚠️ Important — Easy to Forget

- **Integer overflow is silent** — `int a = 1e9; a * a` overflows. Use `long long` or cast: `(long long)a * a`.
- **Integer division truncates** — `5 / 2 == 2`; cast to `double` for `2.5`.
- **Safe mid in binary search** — `int mid = lo + (hi - lo) / 2;` avoids overflow.
- **`v[i]` has no bounds check** — out-of-range is undefined behavior; use `v.at(i)` to throw.
- **Iterate by reference to modify** — `for (int& x : v)`; plain `for (int x : v)` copies.
- **`map` vs `unordered_map`** — `map` is sorted `O(log n)`; `unordered_map` is `O(1)` average, no order.
- **`priority_queue` default is a MAX-heap** — use `greater<>` for a min-heap.
- **`endl` flushes** every time (slow in loops) — prefer `'\n'`.
- **Fast I/O** — `ios_base::sync_with_stdio(false); cin.tie(NULL);` for large inputs.
