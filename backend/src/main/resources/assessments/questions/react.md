# React — 100 Interview MCQs

> Format: Each question has a difficulty tag `[Easy] / [Medium] / [Hard]`, options (or True/False, or one-word), the correct answer, and a one-line justification. Focuses on modern React (function components + hooks).

---

## Section A — Easy (Q1–Q35)

**Q1. [Easy]** React is primarily a:

- A) Backend framework
- B) JavaScript library for building UIs
- C) Database
- D) CSS framework

**Answer:** B — React is a library for building user interfaces.

**Q2. [Easy]** JSX is:

- A) A database query language
- B) A syntax extension that looks like HTML in JS
- C) A CSS preprocessor
- D) A testing tool

**Answer:** B — JSX lets you write markup within JavaScript.

**Q3. [Easy] (One word)** React uses a Virtual **\_\_** to optimize UI updates.

**Answer:** DOM — The Virtual DOM diffs changes efficiently.

**Q4. [Easy]** Which hook manages state in a function component?

- A) useEffect
- B) useState
- C) useRef
- D) useMemo

**Answer:** B — `useState` adds local state.

**Q5. [Easy]** Which hook handles side effects?

- A) useState
- B) useEffect
- C) useContext
- D) useReducer

**Answer:** B — `useEffect` runs side effects after render.

**Q6. [Easy] (True/False)** Props are read-only in React.

**Answer:** True — Components must not mutate their props.

**Q7. [Easy]** How do you pass data from parent to child?

- A) State
- B) Props
- C) Context only
- D) Refs

**Answer:** B — Data flows down via props.

**Q8. [Easy]** JSX expressions are wrapped in:

- A) Parentheses only
- B) Curly braces `{}`
- C) Square brackets
- D) Angle brackets

**Answer:** B — `{}` embeds JS expressions in JSX.

**Q9. [Easy]** Which attribute is used instead of `class` in JSX?

- A) class
- B) className
- C) classList
- D) css

**Answer:** B — JSX uses `className` (since `class` is reserved).

**Q10. [Easy] (One word)** A React **\_\_** is a reusable, self-contained piece of UI.

**Answer:** Component — Components compose the UI.

**Q11. [Easy]** A function component returns:

- A) A string
- B) JSX/React elements
- C) A promise
- D) HTML file

**Answer:** B — It returns React elements (JSX).

**Q12. [Easy] (True/False)** Component names must start with a capital letter.

**Answer:** True — Lowercase names are treated as DOM tags.

**Q13. [Easy]** The `key` prop is used to:

- A) Encrypt data
- B) Help React identify list items for efficient updates
- C) Style elements
- D) Route pages

**Answer:** B — Keys give stable identity to list elements.

**Q14. [Easy]** Which is the correct event handler syntax in JSX?

- A) onclick="handle()"
- B) onClick={handleClick}
- C) on-click=handleClick
- D) click={handleClick}

**Answer:** B — JSX uses camelCase and a function reference.

**Q15. [Easy] (One word)** To render a list you commonly use the array **\_\_** method.

**Answer:** map — `.map()` transforms arrays into elements.

**Q16. [Easy]** State updates in React are:

- A) Always synchronous
- B) Often batched and asynchronous
- C) Immediate DOM writes
- D) Blocking

**Answer:** B — React batches updates for performance.

**Q17. [Easy]** Which tool creates a new React project (modern)?

- A) create-react-app or Vite
- B) npm build
- C) react-init
- D) webpack only

**Answer:** A — CRA/Vite scaffold React apps.

**Q18. [Easy] (True/False)** You can return multiple elements using a Fragment `<>...</>`.

**Answer:** True — Fragments group children without extra DOM nodes.

**Q19. [Easy]** Conditional rendering can use:

- A) Ternary operators and `&&`
- B) SQL
- C) CSS media queries only
- D) HTML comments

**Answer:** A — JS expressions like ternary/`&&` conditionally render.

**Q20. [Easy]** Controlled components have their value driven by:

- A) The DOM
- B) React state
- C) Local storage
- D) Cookies

**Answer:** B — Controlled inputs bind value to state.

**Q21. [Easy] (One word)** The hook to reference a DOM node directly is use**\_\_**.

**Answer:** Ref — `useRef` accesses DOM/mutable values.

**Q22. [Easy]** Which is TRUE about `useState`'s setter?

- A) It mutates state directly
- B) It schedules a re-render with new state
- C) It's synchronous DOM update
- D) It clears state

**Answer:** B — Calling the setter triggers a re-render.

**Q23. [Easy]** Default export vs named export affects:

- A) Runtime speed
- B) Import syntax
- C) State
- D) Styling

**Answer:** B — Named imports require braces; default doesn't.

**Q24. [Easy] (True/False)** React follows a one-way (unidirectional) data flow.

**Answer:** True — Data flows top-down from parent to child.

**Q25. [Easy]** Which hook shares global-ish data without prop drilling?

- A) useState
- B) useContext
- C) useRef
- D) useEffect

**Answer:** B — `useContext` consumes context values.

**Q26. [Easy]** What triggers a component re-render?

- A) Any variable change
- B) State or props changes (or parent re-render)
- C) CSS changes
- D) Console logs

**Answer:** B — Re-renders happen on state/props updates.

**Q27. [Easy] (One word)** A pure UI function with no state is often called a **\_\_** component.

**Answer:** Presentational — (a.k.a. stateless/dumb component).

**Q28. [Easy]** How do you prevent a form's default submit behavior?

- A) return false
- B) event.preventDefault()
- C) stopRender()
- D) event.cancel()

**Answer:** B — `preventDefault()` stops default browser action.

**Q29. [Easy]** Which library is commonly used for routing?

- A) Redux
- B) React Router
- C) Axios
- D) Jest

**Answer:** B — React Router handles client-side navigation.

**Q30. [Easy] (True/False)** Hooks can only be called at the top level of a component or custom hook.

**Answer:** True — The Rules of Hooks forbid conditional/nested calls.

**Q31. [Easy]** Which hook is used for complex state logic with actions?

- A) useState
- B) useReducer
- C) useRef
- D) useMemo

**Answer:** B — `useReducer` manages state via a reducer function.

**Q32. [Easy]** What does the dependency array in `useEffect` control?

- A) Component style
- B) When the effect re-runs
- C) The return value
- D) The key

**Answer:** B — The effect re-runs when listed dependencies change.

**Q33. [Easy] (One word)** Passing data through many layers unnecessarily is called prop **\_\_**.

**Answer:** Drilling — Prop drilling; often solved with context.

**Q34. [Easy]** An empty dependency array `[]` in `useEffect` means the effect runs:

- A) On every render
- B) Only once after mount
- C) Never
- D) On unmount only

**Answer:** B — It runs once after initial mount.

**Q35. [Easy]** Which returns the previous rendered UI structure for diffing?

- A) Real DOM
- B) Virtual DOM
- C) Shadow DOM
- D) CSSOM

**Answer:** B — React diffs against the Virtual DOM.

---

## Section B — Medium (Q36–Q75)

**Q36. [Medium]** Output/behavior:

```jsx
const [count, setCount] = useState(0);
const handle = () => {
  setCount(count + 1);
  setCount(count + 1);
};
```

After one click, count becomes:

- A) 2
- B) 1
- C) 0
- D) 3

**Answer:** B — Both calls use the stale `count` (0), so it becomes 1; use functional updates for 2.

**Q37. [Medium]** The correct fix for Q36 to increment by 2 is:

- A) setCount(count + 2)
- B) setCount(prev => prev + 1) twice
- C) useEffect
- D) useRef

**Answer:** B — Functional updates apply sequentially on the latest state.

**Q38. [Medium]** `useEffect` cleanup function runs:

- A) Before every re-run and on unmount
- B) Only on mount
- C) Never
- D) On every render start

**Answer:** A — Cleanup runs before the next effect and at unmount.

**Q39. [Medium] (True/False)** `useMemo` memoizes a computed value; `useCallback` memoizes a function.

**Answer:** True — `useMemo` caches values; `useCallback` caches function identity.

**Q40. [Medium]** `useCallback(fn, deps)` is equivalent to:

- A) useMemo(() => fn, deps)
- B) useEffect(fn, deps)
- C) useRef(fn)
- D) useState(fn)

**Answer:** A — `useCallback` is `useMemo` returning the function itself.

**Q41. [Medium]** Why use `key` with a stable unique id instead of array index?

- A) Indexes are faster
- B) Index keys cause bugs on reorder/insert/delete
- C) Keys are optional
- D) Ids break rendering

**Answer:** B — Index keys misidentify items when the list changes.

**Q42. [Medium]** `React.memo` is used to:

- A) Memoize expensive values
- B) Prevent re-render of a component if props are unchanged
- C) Cache API calls
- D) Store global state

**Answer:** B — It performs shallow prop comparison to skip re-renders.

**Q43. [Medium] (One word)** A function that returns JSX and encapsulates reusable stateful logic (starting with "use") is a custom **\_\_**.

**Answer:** Hook — Custom hooks reuse stateful logic.

**Q44. [Medium]** Which statement about lifting state up is correct?

- A) Move state down to children
- B) Move shared state to the closest common ancestor
- C) Duplicate state everywhere
- D) Use global variables

**Answer:** B — Shared state lives in the common parent.

**Q45. [Medium]** The `children` prop represents:

- A) Child component state
- B) Nested JSX passed between component tags
- C) Event handlers
- D) Refs

**Answer:** B — `children` is the content between opening/closing tags.

**Q46. [Medium] (True/False)** Calling a hook inside an `if` block violates the Rules of Hooks.

**Answer:** True — Hooks must run in the same order every render.

**Q47. [Medium]** Controlled vs uncontrolled input: an uncontrolled input reads value via:

- A) state
- B) a ref (DOM)
- C) context
- D) props only

**Answer:** B — Uncontrolled inputs use refs to read the DOM value.

**Q48. [Medium]** Which causes an infinite loop?

```jsx
useEffect(() => {
  setState(x + 1);
});
```

- A) Nothing wrong
- B) Missing dependency array causes it to run every render → loop
- C) Too few components
- D) Wrong key

**Answer:** B — No deps array + state update re-triggers the effect endlessly.

**Q49. [Medium]** `useContext` re-renders consumers when:

- A) Any component renders
- B) The context provider's value changes
- C) Props change only
- D) Never

**Answer:** B — Consumers update when the provided value changes.

**Q50. [Medium] (One word)** To avoid re-creating objects/functions each render for context value, wrap them in use**\_\_**.

**Answer:** Memo — `useMemo`/`useCallback` stabilize the provided value.

**Q51. [Medium]** Error Boundaries catch errors in:

- A) Event handlers
- B) Rendering, lifecycle, and constructors of children
- C) Async code
- D) Server code

**Answer:** B — They catch render-phase errors in the tree below.

**Q52. [Medium] (True/False)** Error Boundaries can be written as function components today.

**Answer:** False — They still require class components (`componentDidCatch`/`getDerivedStateFromError`).

**Q53. [Medium]** `useLayoutEffect` differs from `useEffect` because it:

- A) Runs asynchronously after paint
- B) Runs synchronously after DOM mutations, before paint
- C) Never runs
- D) Runs on the server

**Answer:** B — It fires before the browser paints to avoid flicker.

**Q54. [Medium]** Which is the recommended way to update an object in state?

- A) Mutate it directly then setState
- B) Create a new object with the spread operator
- C) Use push
- D) Assign in place

**Answer:** B — Immutable updates via `{...prev, key: val}`.

**Q55. [Medium]** Lazy loading a component uses:

- A) React.lazy + Suspense
- B) useEffect
- C) useRef
- D) memo

**Answer:** A — `React.lazy` with `<Suspense>` enables code splitting.

**Q56. [Medium] (One word)** `<Suspense fallback={...}>` shows the fallback while the child is **\_\_**.

**Answer:** Loading — It renders fallback until content is ready.

**Q57. [Medium]** Reconciliation is:

- A) Merging git branches
- B) React's diffing algorithm to update the DOM efficiently
- C) State management
- D) CSS layout

**Answer:** B — Reconciliation compares trees to apply minimal updates.

**Q58. [Medium]** Why do keys help reconciliation?

- A) They style items
- B) They let React match elements across renders
- C) They cache network calls
- D) They sort lists

**Answer:** B — Keys provide identity for efficient diffing.

**Q59. [Medium] (True/False)** State updates may be batched even inside promises/timeouts in React 18.

**Answer:** True — React 18 introduced automatic batching everywhere.

**Q60. [Medium]** Which hook returns a mutable value that persists without causing re-render?

- A) useState
- B) useRef
- C) useMemo
- D) useEffect

**Answer:** B — `useRef().current` persists across renders without re-rendering.

**Q61. [Medium]** Passing a function down and calling it in a child to update parent state is called:

- A) Callback props / lifting state up
- B) Prop drilling only
- C) Context
- D) Redux

**Answer:** A — Children invoke parent-provided callbacks.

**Q62. [Medium]** The main purpose of Redux/Zustand/Context is:

- A) Styling
- B) Managing shared application state
- C) Routing
- D) Testing

**Answer:** B — They centralize/share state across components.

**Q63. [Medium] (One word)** In Redux, functions that specify how state changes in response to actions are called **\_\_**.

**Answer:** Reducers — Pure functions `(state, action) => newState`.

**Q64. [Medium]** A React component re-renders unnecessarily due to a new function prop each render; fix with:

- A) useCallback + React.memo
- B) useState
- C) inline arrow always
- D) useEffect

**Answer:** A — Stabilize the function and memoize the child.

**Q65. [Medium]** `dangerouslySetInnerHTML` is used to:

- A) Set inline styles
- B) Inject raw HTML (XSS risk)
- C) Add event handlers
- D) Route pages

**Answer:** B — It renders raw HTML and can expose XSS.

**Q66. [Medium] (True/False)** In React 18, `StrictMode` intentionally double-invokes some functions in development.

**Answer:** True — It double-invokes to surface side-effect bugs (dev only).

**Q67. [Medium]** Which is the correct dependency for an effect using `props.id`?

- A) []
- B) [props.id]
- C) [props]
- D) No array

**Answer:** B — List `props.id` so the effect re-runs when it changes.

**Q68. [Medium]** Portals (`ReactDOM.createPortal`) are used to:

- A) Render children into a different DOM node (e.g., modal)
- B) Route between pages
- C) Fetch data
- D) Memoize values

**Answer:** A — Portals render outside the parent DOM hierarchy.

**Q69. [Medium] (One word)** A component that only manages logic/state and delegates UI is a **\_\_** component (a.k.a. smart).

**Answer:** Container — Container components hold logic/state.

**Q70. [Medium]** Which is TRUE about `setState` in React 18 inside an event handler?

- A) Synchronous re-render
- B) Batched, then a single re-render
- C) Two separate renders always
- D) No render

**Answer:** B — Multiple updates batch into one render.

**Q71. [Medium]** The correct way to conditionally add a class:

- A) className={isActive ? 'active' : ''}
- B) class="active?"
- C) style-class=active
- D) className=isActive

**Answer:** A — Use a JS expression for conditional classes.

**Q72. [Medium] (True/False)** `useEffect` runs after the browser has painted the screen.

**Answer:** True — It runs asynchronously after paint (unlike `useLayoutEffect`).

**Q73. [Medium]** Fetching data in a component is best placed in:

- A) Render body directly
- B) useEffect (or a data library like React Query)
- C) The constructor
- D) A CSS file

**Answer:** B — Side-effectful fetching belongs in effects/data hooks.

**Q74. [Medium]** Which prevents a stale closure capturing old state in an interval?

- A) Use functional updates or a ref
- B) Add more state
- C) Remove deps
- D) Use inline styles

**Answer:** A — Functional updates/refs read the latest value.

**Q75. [Medium]** `key` changes on a component cause React to:

- A) Update in place
- B) Unmount and remount (reset state)
- C) Ignore it
- D) Throw an error

**Answer:** B — A changed key remounts the component, resetting state.

---

## Section C — Hard (Q76–Q100)

**Q76. [Hard]** In React 18 concurrent rendering, `useTransition` is used to:

- A) Animate CSS
- B) Mark state updates as non-urgent (low priority)
- C) Fetch data
- D) Route pages

**Answer:** B — It defers non-urgent updates to keep UI responsive.

**Q77. [Hard] (One word)** The hook that returns a deferred version of a value for concurrent rendering is use**\_\_**Value.

**Answer:** Deferred — `useDeferredValue` defers a value update.

**Q78. [Hard]** Why can reading `ref.current` during render be unsafe?

- A) It's always undefined
- B) Refs mutate outside render flow; reading them during render isn't guaranteed consistent
- C) Refs cause re-renders
- D) It throws

**Answer:** B — Refs are for imperative/mutable values outside render logic.

**Q79. [Hard]** `getDerivedStateFromError` vs `componentDidCatch`:

- A) Both render fallback
- B) The former updates state to render fallback; the latter logs error info
- C) They are identical
- D) Neither catches errors

**Answer:** B — One derives fallback state, the other performs side effects/logging.

**Q80. [Hard] (True/False)** In React 18, effects in StrictMode mount, unmount, then mount again in development to test resilience.

**Answer:** True — This surfaces missing cleanup logic.

**Q81. [Hard]** What problem does `useId` solve?

- A) Global state
- B) Generating stable unique IDs consistent across server/client (SSR hydration)
- C) Routing
- D) Memoization

**Answer:** B — It avoids hydration mismatches for accessibility IDs.

**Q82. [Hard]** Hydration mismatch warnings occur when:

- A) CSS differs
- B) Server-rendered HTML differs from the client's first render
- C) Props are missing
- D) Keys are wrong

**Answer:** B — SSR markup must match the initial client render.

**Q83. [Hard] (One word)** The React feature enabling server-only components that don't ship JS to the client is React Server **\_\_**.

**Answer:** Components — React Server Components (RSC).

**Q84. [Hard]** Why might `React.memo` still re-render despite unchanged-looking props?

- A) It never re-renders
- B) A new object/array/function reference is passed each render (shallow compare fails)
- C) Keys changed
- D) State is global

**Answer:** B — Referential inequality of props defeats shallow comparison.

**Q85. [Hard]** `flushSync` from react-dom is used to:

- A) Batch updates
- B) Force synchronous DOM update, opting out of batching
- C) Fetch data
- D) Memoize

**Answer:** B — It applies updates synchronously when needed.

**Q86. [Hard] (True/False)** Context value changes cause all consumers to re-render even if they only use part of the value.

**Answer:** True — Any provider value change re-renders all consumers (split contexts to mitigate).

**Q87. [Hard]** The "stale closure" in `useEffect` happens because:

- A) Effects don't run
- B) The effect captures variables from the render it was created in
- C) React is buggy
- D) Deps are ignored

**Answer:** B — Closures capture values from their creating render.

**Q88. [Hard]** Which correctly cancels a fetch to avoid setting state after unmount?

- A) Ignore it
- B) Use an AbortController and cleanup in useEffect
- C) Use setTimeout
- D) Use a ref only

**Answer:** B — Abort in the cleanup to prevent updates on unmounted components.

**Q89. [Hard] (One word)** Rendering a very large list efficiently by only rendering visible rows is called **\_\_** (windowing).

**Answer:** Virtualization — Libraries like react-window virtualize lists.

**Q90. [Hard]** Why is mutating state directly problematic?

- A) It's slower
- B) React relies on reference changes to detect updates; mutation can skip re-renders
- C) It throws always
- D) It changes keys

**Answer:** B — Same reference means React may not re-render.

**Q91. [Hard]** In `useReducer`, dispatching an action:

- A) Directly sets state
- B) Passes the action to the reducer to compute the next state
- C) Re-runs effects only
- D) Mutates props

**Answer:** B — The reducer computes new state from `(state, action)`.

**Q92. [Hard] (True/False)** `useImperativeHandle` customizes the ref value exposed by a component using `forwardRef`.

**Answer:** True — It exposes an imperative API to parents.

**Q93. [Hard]** Why can defining a component inside another component cause bugs?

- A) It's illegal syntax
- B) A new component type is created each render, remounting the subtree and losing state
- C) It improves performance
- D) It memoizes automatically

**Answer:** B — The inner component's identity changes every render.

**Q94. [Hard]** The React fiber architecture enables:

- A) Only class components
- B) Interruptible, prioritized rendering work
- C) CSS-in-JS
- D) Server routing

**Answer:** B — Fiber allows incremental, prioritized reconciliation.

**Q95. [Hard] (One word)** The phase where React computes changes without touching the DOM is the **\_\_** phase.

**Answer:** Render — Followed by the commit phase that mutates the DOM.

**Q96. [Hard]** Which optimizes context re-renders for large apps?

- A) One giant context
- B) Splitting into multiple focused contexts or using selectors
- C) Removing memoization
- D) Inline objects

**Answer:** B — Smaller contexts/selectors limit re-render scope.

**Q97. [Hard] (True/False)** In React 18, `ReactDOM.render` is replaced by `ReactDOM.createRoot` for concurrent features.

**Answer:** True — `createRoot` enables concurrent rendering.

**Q98. [Hard]** A component reads `count` in a `setInterval` set once on mount and always sees 0. Root cause:

- A) React bug
- B) Stale closure from empty deps capturing initial state
- C) Wrong key
- D) Missing memo

**Answer:** B — The interval closure captured the initial `count`.

**Q99. [Hard]** Which is TRUE about `Suspense` for data fetching?

- A) It only works with images
- B) It lets components "wait" for async resources and show fallback UI
- C) It replaces useState
- D) It's server-only

**Answer:** B — Suspense coordinates loading states for async data.

**Q100. [Hard]** Why prefer immutable state updates for performance with `React.memo`/`PureComponent`?

- A) They use less memory
- B) Shallow equality checks correctly detect changes via new references
- C) They avoid keys
- D) They disable rendering

**Answer:** B — New references make shallow comparisons reliable.
