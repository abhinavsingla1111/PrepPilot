# 📝 Forms & User Input

> Capturing what users type and making React the source of truth for form data.

## 🧠 What & Why

Forms are how users give data to your app — search boxes, login fields, settings. In plain HTML the DOM holds the input's value. In React you usually want *React state* to hold it instead, so the value is predictable and you can validate, format, or disable the submit button based on it.

This gives two styles: **controlled** inputs, where React state is the single source of truth and the input always reflects it, and **uncontrolled** inputs, where the DOM keeps the value and you read it only when needed (via a ref). Controlled is the common, recommended default.

Handling forms comes down to a few events: `onChange` (each keystroke updates state) and `onSubmit` (validate and do something), plus `preventDefault()` to stop the browser's default full-page reload.

## 🔑 Key Concepts

- **Controlled input** — `value` comes from state and `onChange` updates it; React owns the data.
- **Uncontrolled input** — The DOM holds the value; you read it via a `ref` when submitting.
- **`onChange`** — Fires on each edit; used to keep state in sync with the field.
- **`onSubmit`** — Fires when the form is submitted; where you validate and act.
- **`preventDefault()`** — Stops the browser from reloading the page on submit.
- **Validation** — Checking values and surfacing errors before accepting the form.

## 💻 Example

A small controlled form with validation:

```tsx
import { useState } from 'react'

export default function SignupForm() {
  const [email, setEmail] = useState('')
  const [error, setError] = useState<string | null>(null)

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault() // stop the default page reload
    if (!email.includes('@')) {
      setError('Please enter a valid email.')
      return
    }
    setError(null)
    console.log('Submitting', email)
  }

  return (
    <form onSubmit={handleSubmit}>
      <label htmlFor="email">Email</label>
      <input
        id="email"
        type="email"
        value={email}                              // controlled: value from state
        onChange={(e) => setEmail(e.target.value)} // update state on every keystroke
      />
      {error && <p role="alert">⚠️ {error}</p>}
      {/* Disable submit until there's input */}
      <button type="submit" disabled={email.length === 0}>Sign up</button>
    </form>
  )
}
```

An **uncontrolled** input for comparison:

```tsx
import { useRef } from 'react'

function UncontrolledSearch() {
  const inputRef = useRef<HTMLInputElement>(null)
  const onSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log(inputRef.current?.value) // read only when needed
  }
  return (
    <form onSubmit={onSubmit}>
      <input ref={inputRef} defaultValue="" />
      <button>Search</button>
    </form>
  )
}
```

## ⚖️ Controlled vs Uncontrolled

| | Controlled | Uncontrolled |
| --- | --- | --- |
| Source of truth | React state | The DOM |
| Value read via | `value` + `onChange` | A `ref` |
| Initial value | `value={state}` | `defaultValue` |
| Live validation | Easy | Harder |
| Best for | Most forms | Simple/rare reads, file inputs |

## ⚠️ Common Pitfalls

- Setting `value` without an `onChange` makes the input read-only; React warns about this.
- Forgetting `e.preventDefault()` in `onSubmit`, causing a full page reload.
- Using `value={undefined}` then a string later — switches controlled/uncontrolled and warns.
- Storing each field in its own state when an object or `useReducer` would be cleaner for big forms.
- Validating only on submit when inline feedback would help the user.

## ❓ FAQs

### What is the difference between controlled and uncontrolled components?
A controlled component keeps its value in React state and updates via `onChange`, so React is the single source of truth and the input always mirrors state. An uncontrolled component lets the DOM hold the value, and you read it with a `ref` (using `defaultValue` for the initial value). Controlled is preferred for validation and dynamic behavior; uncontrolled is handy for simple cases and file inputs.

### Why do I need to call preventDefault on submit?
By default, submitting an HTML form makes the browser send a request and reload the page, which throws away your React app's state. Calling `e.preventDefault()` in the `onSubmit` handler stops that default behavior so you can handle the submission in JavaScript — validate, call an API, and update state — without a reload.

### How do I handle many inputs without dozens of useState calls?
Store the fields in a single state object and update it immutably with a generic handler that keys off the input's `name`: `setForm(f => ({ ...f, [e.target.name]: e.target.value }))`. For complex forms with validation and many transitions, `useReducer` or a form library (React Hook Form, Formik) keeps things organized.

### When should I validate — on change or on submit?
Both have a place. Validating on submit is simplest and avoids nagging users mid-typing. Validating on change (or on blur) gives immediate feedback for a smoother UX, but should be gentle — often you only show errors after the user leaves a field or tries to submit. Many forms combine: light on-blur checks plus a full check on submit.

### Why does React warn about switching between controlled and uncontrolled?
The warning appears when an input's `value` starts as `undefined`/`null` (uncontrolled) and later becomes a defined string (controlled), or vice versa. React can't manage the input consistently across that switch. Fix it by always providing a defined value — initialize state to an empty string `''` rather than `undefined`.
