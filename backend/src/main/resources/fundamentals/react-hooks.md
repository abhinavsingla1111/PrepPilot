# 🪝 Core Hooks & Custom Hooks

> The other essential hooks beyond state and effects — and how to build your own.

## 🧠 What & Why

**Hooks** are functions that let function components tap into React features like state, context, and lifecycle. Beyond `useState` and `useEffect`, React ships a handful of hooks that solve specific problems: sharing data without prop-drilling, referencing DOM nodes, caching expensive work, and managing complex state.

The real superpower is **custom hooks** — you can extract any reusable stateful logic into your own `useSomething` function. Custom hooks let you share behavior between components without repeating code, keeping components small and focused.

All hooks follow the same **Rules of Hooks**, and understanding those rules is a common interview topic.

## 🔑 Key Concepts

- **`useContext`** — Reads a shared value from a Context, avoiding prop-drilling.
- **`useRef`** — Holds a mutable value (or DOM node) that persists across renders without causing re-renders.
- **`useMemo`** — Caches the *result* of an expensive calculation between renders.
- **`useCallback`** — Caches a *function* so its identity stays stable between renders.
- **`useReducer`** — Manages complex state via a reducer function, like a mini Redux.
- **Custom hook** — A function starting with `use` that calls other hooks to package reusable logic.

## 💻 Example

A custom hook — the same pattern PrepPilot uses for debounced search:

```tsx
import { useEffect, useRef, useState } from 'react'

// Returns `value` but only after it has stopped changing for `delay` ms.
function useDebouncedValue<T>(value: T, delay = 300): T {
  const [debounced, setDebounced] = useState(value)

  useEffect(() => {
    const id = window.setTimeout(() => setDebounced(value), delay)
    return () => window.clearTimeout(id) // cleanup previous timer
  }, [value, delay])

  return debounced
}

// Using useRef to focus an input without re-rendering.
function SearchBar() {
  const inputRef = useRef<HTMLInputElement>(null)
  const [text, setText] = useState('')
  const debounced = useDebouncedValue(text, 300)

  useEffect(() => {
    inputRef.current?.focus() // touch the DOM node directly
  }, [])

  return (
    <div>
      <input ref={inputRef} value={text} onChange={(e) => setText(e.target.value)} />
      <p>Searching for: {debounced}</p>
    </div>
  )
}
```

`useReducer` for structured state transitions:

```tsx
type Action = { type: 'inc' } | { type: 'reset' }
function reducer(state: number, action: Action) {
  switch (action.type) {
    case 'inc': return state + 1
    case 'reset': return 0
  }
}
// const [count, dispatch] = useReducer(reducer, 0)
// dispatch({ type: 'inc' })
```

## ⚖️ useMemo vs useCallback

| | `useMemo` | `useCallback` |
| --- | --- | --- |
| Caches | The **result** of a function | The **function itself** |
| Returns | A computed value | A memoized callback |
| Typical use | Expensive calculations | Stable props to memoized children |
| Equivalent | `useMemo(() => fn, deps)` | `useMemo(() => () => ..., deps)` for the fn |

## 📜 Rules of Hooks

- **Only call hooks at the top level** — never inside loops, conditions, or nested functions. React relies on call order.
- **Only call hooks from React functions** — components or other custom hooks, not plain functions.
- **Name custom hooks `useX`** — so tooling and the linter can enforce the rules.

## ⚠️ Common Pitfalls

- Calling a hook conditionally (`if (x) useState(...)`) — breaks the call-order guarantee.
- Overusing `useMemo`/`useCallback` — memoization has its own cost; only use it when it measurably helps.
- Storing render-affecting data in `useRef` and expecting the UI to update — refs don't trigger re-renders.
- Forgetting dependencies in `useMemo`/`useCallback`, causing stale values or functions.

## ❓ FAQs

### What are the Rules of Hooks and why do they exist?
You must call hooks at the top level of a component or custom hook, in the same order every render, and only from React functions. React tracks hook state by call order, so conditionally or conditionally skipping a hook would misalign that internal list and corrupt state. The ESLint `react-hooks` plugin enforces these rules automatically.

### What is the difference between useMemo and useCallback?
`useMemo` caches the *value* returned by a function; `useCallback` caches the *function reference* itself. Use `useMemo` to avoid recomputing something expensive, and `useCallback` to keep a function's identity stable so it doesn't cause memoized children (or effect deps) to change every render. `useCallback(fn, deps)` is essentially `useMemo(() => fn, deps)`.

### When should I use useRef?
Use `useRef` for values that must persist across renders but should *not* trigger a re-render when they change — such as a DOM node reference, a timer id, or a "previous value" tracker. Unlike state, updating `ref.current` is silent. If changing the value should update the UI, use state instead.

### What is a custom hook and why build one?
A custom hook is a function whose name starts with `use` and which calls other hooks to encapsulate reusable stateful logic (e.g. `useDebouncedValue`, `useFetch`, `useLocalStorage`). It lets you share behavior across components without duplicating code or resorting to render props/HOCs, while keeping each component focused on rendering.

### When would you reach for useReducer over useState?
Use `useReducer` when state is complex — multiple related fields, or the next state depends on the previous one through many action types. Centralizing transitions in a reducer makes updates predictable and testable, and lets you dispatch descriptive actions instead of scattering `setState` calls. For simple, independent values, `useState` is lighter and clearer.
