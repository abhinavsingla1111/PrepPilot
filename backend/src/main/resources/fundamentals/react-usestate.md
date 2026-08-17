# 🎛️ useState & Component State

> How components remember things and re-render when that data changes.

## 🧠 What & Why

**State** is data that a component owns and can change over time — a counter value, whether a modal is open, the text in a search box. When state changes, React re-renders the component so the screen stays in sync with the data.

You add state with the **`useState` hook**. It gives you two things: the current value and a function to update it. Calling that updater tells React "this value changed, please re-render." You never reassign the variable directly — React wouldn't know about the change.

The golden rule of state is **immutability**: you always create a *new* value rather than mutating the existing one. React compares the old and new values by reference to decide whether to re-render, so mutating in place can make updates silently fail.

## 🔑 Key Concepts

- **`useState(initial)`** — Returns `[value, setValue]`. Declares one piece of state.
- **State vs props** — State is owned and changeable by the component; props come from a parent and are read-only.
- **Functional update** — `setCount(c => c + 1)` computes the next value from the previous one safely.
- **Immutability** — Never mutate state; create a new object/array so React detects the change.
- **Batching** — Multiple `setState` calls in one event are grouped into a single re-render.
- **Lifting state up** — Moving shared state to the closest common parent so siblings can share it.

## 💻 Example

```tsx
import { useState } from 'react'

export default function Counter() {
  // `count` is the current value; `setCount` updates it.
  const [count, setCount] = useState(0)

  // Functional update: safe even if called multiple times in a row.
  const increment = () => setCount((c) => c + 1)

  return (
    <div>
      <p>Count: {count}</p>
      <button onClick={increment}>+1</button>
      {/* Reset by setting a brand-new value */}
      <button onClick={() => setCount(0)}>Reset</button>
    </div>
  )
}
```

Updating objects and arrays **immutably**:

```tsx
const [user, setUser] = useState({ name: 'Ada', likes: 0 })

// ❌ Wrong: mutates the existing object; React may not re-render.
// user.likes++

// ✅ Right: spread into a new object with the changed field.
setUser((prev) => ({ ...prev, likes: prev.likes + 1 }))

const [tags, setTags] = useState<string[]>([])
// ✅ Add to an array by creating a new array.
setTags((prev) => [...prev, 'react'])
```

## ⚖️ State vs Props

| | State | Props |
| --- | --- | --- |
| Who owns it | The component itself | The parent component |
| Mutable? | Yes, via the setter | No, read-only |
| Triggers re-render | Yes, when changed | Yes, when parent passes new values |
| Typical use | Toggles, inputs, counters | Configuration, data passed down |

## ⬆️ Lifting State Up

When two sibling components need the same data, move the state to their common parent and pass it down as props:

```tsx
function Parent() {
  const [query, setQuery] = useState('')
  return (
    <>
      <SearchBox value={query} onChange={setQuery} />
      <Results query={query} />
    </>
  )
}
```

## ⚠️ Common Pitfalls

- Mutating state directly (`arr.push(x)`, `obj.key = y`) instead of creating a new value.
- Using the current value in rapid successive updates (`setCount(count + 1)` twice) — use the functional form.
- Expecting state to update synchronously; the new value is only available on the next render.
- Storing derived data in state that could just be computed during render.
- Putting state too low, then needing it in a sibling — lift it up instead.

## ❓ FAQs

### Why can't I mutate state directly?
React decides whether to re-render by comparing the previous and next state by reference (`Object.is`). If you mutate the same object or array in place, the reference doesn't change, so React may skip the re-render and your UI won't update. Always create a new object/array (e.g. with the spread operator) so the reference changes.

### When should I use the functional update form?
Use `setValue(prev => ...)` whenever the next value depends on the previous one, especially if you might call the setter multiple times in the same event or inside async code. Because state updates are batched and asynchronous, reading the plain `count` variable can give you a stale value; the functional form always receives the latest state.

### Is setState synchronous or asynchronous?
It's effectively asynchronous. Calling the setter schedules a re-render; it does not change the local variable immediately. Within the same event handler you'll still see the old value. The updated value appears on the next render. React also *batches* multiple updates in one event into a single render for performance.

### What does "lifting state up" mean?
When multiple components need to read or update the same data, you move that state into their nearest common ancestor and pass it down via props (plus callbacks to update it). This keeps a single source of truth and lets siblings stay in sync, instead of duplicating state in each component and trying to synchronize copies.

### What is batching in React?
Batching means React groups several state updates that happen in the same event (or, in React 18+, in promises/timeouts too) into one re-render instead of rendering after each `setState`. This improves performance and prevents flickering. It's also why the local state variable doesn't change mid-handler — React applies all updates, then renders once.
