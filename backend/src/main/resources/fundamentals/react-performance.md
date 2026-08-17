# 🚀 Performance & Optimization

> Understanding when React re-renders and how to keep your app fast — without over-optimizing.

## 🧠 What & Why

React is fast by default, but as apps grow you can hit slowdowns from **unnecessary re-renders** — components re-running when their output didn't actually change. Understanding *why* a component re-renders is the first step to fixing performance issues.

A component re-renders when its **state changes**, its **props change**, or its **parent re-renders**. That last one surprises people: by default, when a parent renders, all its children render too. Usually that's cheap and fine. When it isn't, React gives you tools — `memo`, `useMemo`, `useCallback` — to skip redundant work.

The most important rule, though, is **don't optimize prematurely**. Most re-renders are harmless. Measure first (React DevTools Profiler), then optimize the specific hot spot. Sprinkling memoization everywhere adds complexity and can even slow things down.

## 🔑 Key Concepts

- **Re-render** — React re-running a component function to produce new UI.
- **`React.memo`** — Wraps a component so it skips re-rendering when its props are unchanged.
- **`useMemo`** — Caches an expensive computed value between renders.
- **`useCallback`** — Caches a function's identity so memoized children don't re-render.
- **Keys** — Stable list identifiers that prevent needless remounting and preserve state.
- **Code-splitting / lazy** — Loading parts of the app on demand to shrink the initial bundle.
- **Premature optimization** — Adding memoization before measuring a real problem.

## 💻 Example

```tsx
import { memo, useCallback, useMemo, useState } from 'react'

// Wrapped in memo: only re-renders if `items` or `onPick` change identity.
const List = memo(function List({
  items, onPick,
}: { items: string[]; onPick: (item: string) => void }) {
  console.log('List rendered')
  return (
    <ul>
      {items.map((item) => (
        <li key={item} onClick={() => onPick(item)}>{item}</li>
      ))}
    </ul>
  )
})

export default function Page() {
  const [count, setCount] = useState(0)
  const [items] = useState(['Arrays', 'Trees', 'Graphs'])

  // useMemo: avoid recomputing a derived value on every render.
  const sorted = useMemo(() => [...items].sort(), [items])

  // useCallback: keep the same function identity so `List` (memo) can skip re-render.
  const handlePick = useCallback((item: string) => console.log(item), [])

  return (
    <div>
      {/* Clicking this re-renders Page, but List stays put thanks to memo + useCallback */}
      <button onClick={() => setCount((c) => c + 1)}>Clicked {count}</button>
      <List items={sorted} onPick={handlePick} />
    </div>
  )
}
```

Code-splitting with `lazy` + `Suspense`:

```tsx
import { lazy, Suspense } from 'react'
const Heavy = lazy(() => import('./Heavy')) // loaded only when rendered

function App() {
  return (
    <Suspense fallback={<p>Loading…</p>}>
      <Heavy />
    </Suspense>
  )
}
```

## 🧰 What Each Tool Fixes

| Tool | Problem it solves |
| --- | --- |
| `React.memo` | Child re-renders when its props didn't change |
| `useMemo` | Recomputing an expensive value every render |
| `useCallback` | New function identity breaking `memo`/effect deps |
| Stable `key` | List items remounting or losing state on reorder |
| `lazy` + `Suspense` | Large initial bundle slowing first load |

## ⚠️ Common Pitfalls

- Memoizing everything by default — it adds cost and complexity for no measured benefit.
- `React.memo` with a new inline object/function prop each render — memo can't help; identity changes anyway.
- Using array index as `key`, causing state to attach to the wrong row on reorder.
- Optimizing before profiling — you may "fix" something that was never slow.
- Huge components that do too much; splitting them often helps more than memoization.

## ❓ FAQs

### When does a React component re-render?
A component re-renders when its own state changes, when the props it receives change, or when its parent re-renders (which by default cascades to all children). Context value changes also re-render consumers. Re-rendering doesn't always mean touching the DOM — React still diffs the output and only applies real changes, so many re-renders are cheap.

### What does React.memo do, and when is it useful?
`React.memo` wraps a component so it skips re-rendering when its props are shallowly equal to the previous render. It's useful for components that render often with the same props, especially expensive ones. It only helps if the props are stable — passing a new inline object or function each time defeats it, which is why it's often paired with `useMemo`/`useCallback`.

### How are useMemo and useCallback different in the performance context?
`useMemo` caches a computed *value* to avoid recalculating it every render; `useCallback` caches a *function reference* so it stays stable across renders. Stable references matter when passing callbacks to `React.memo` children or into effect dependency arrays, preventing needless re-renders or effect re-runs. Both take a dependency array controlling when the cache refreshes.

### Why is choosing the right key important for performance?
Keys let React match list items between renders. With stable, unique keys, React updates items in place and preserves their state. With unstable keys (like array indices on a reorderable list), React may destroy and recreate DOM nodes, losing input focus or component state and doing extra work. Good keys are correctness *and* performance wins.

### Isn't optimizing early a good habit?
No — premature optimization usually hurts. Memoization adds code, dependency arrays to maintain, and its own runtime cost, so blanket-applying it can slow apps and obscure logic. React is fast by default; profile with the React DevTools Profiler to find real bottlenecks, then optimize those specific spots. Optimize based on measurements, not guesses.
