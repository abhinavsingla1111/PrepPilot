# 🧩 Components & JSX

> The building blocks of React and the HTML-like syntax you use to describe them.

## 🧠 What & Why

A **component** is just a JavaScript/TypeScript function that returns some UI. Modern React uses **function components** almost exclusively — you write a function, it returns markup, and React renders it. Components let you split a complex screen into small, named, reusable pieces.

That markup is written in **JSX** (JavaScript XML). JSX looks like HTML but it's actually syntactic sugar that compiles to function calls. It lets you mix markup and logic in one place, which is a big part of what makes React feel productive.

Components receive inputs through **props** — think of them as function arguments. Props are read-only: a component should never change the props it receives. This keeps data flow predictable and components easy to reuse.

## 🔑 Key Concepts

- **Function component** — A function that returns JSX. Its name must start with a capital letter.
- **JSX** — HTML-like syntax that compiles to `React.createElement(...)` calls.
- **Props** — Read-only inputs passed to a component, like function parameters.
- **children** — A special prop holding whatever you nest between a component's tags.
- **Conditional rendering** — Showing different UI based on a condition using `&&` or a ternary.
- **Keys** — Unique identifiers React uses to track items when rendering lists.
- **Fragment** — A wrapper (`<>...</>`) that groups elements without adding a DOM node.

## 💻 Example

```tsx
type CardProps = {
  title: string
  featured?: boolean       // optional prop
  children: React.ReactNode // whatever is nested inside <Card>...</Card>
}

function Card({ title, featured = false, children }: CardProps) {
  return (
    <section className={featured ? 'card featured' : 'card'}>
      {/* Conditional rendering: only show the badge when featured */}
      {featured && <span className="badge">⭐ Featured</span>}
      <h2>{title}</h2>
      {children}
    </section>
  )
}

const topics = ['Arrays', 'Trees', 'Graphs']

export default function Topics() {
  return (
    <>
      <Card title="Welcome" featured>
        <p>Pick a topic to begin.</p>
      </Card>

      {/* Rendering a list: each item needs a stable, unique key */}
      <ul>
        {topics.map((topic) => (
          <li key={topic}>{topic}</li>
        ))}
      </ul>
    </>
  )
}
```

## 📏 JSX Rules You Must Know

| Rule | Why | Example |
| --- | --- | --- |
| Return one root element | JSX must have a single parent | Wrap in `<div>` or a fragment `<>` |
| Close every tag | JSX is stricter than HTML | `<img />`, `<br />` |
| Use `className`, not `class` | `class` is a reserved JS word | `<div className="box">` |
| Use camelCase attributes | JSX maps to JS properties | `onClick`, `tabIndex` |
| Wrap JS in `{ }` | To embed expressions | `{user.name}`, `{2 + 2}` |
| Components start uppercase | Lowercase = HTML tag | `<Card />` vs `<div />` |

## 🔀 Conditional Rendering Patterns

```tsx
function Status({ loading, count }: { loading: boolean; count: number }) {
  // Ternary for either/or
  return loading
    ? <p>Loading…</p>
    // && for "render only if true" (careful with 0 — see pitfalls)
    : <p>{count > 0 ? `${count} results` : 'No results'}</p>
}
```

## ⚠️ Common Pitfalls

- Returning multiple elements without a wrapper — use a fragment `<>...</>`.
- Using array **index as a key** for lists that reorder or change — prefer a stable unique id.
- `{count && <Thing />}` renders a literal `0` when `count` is `0`; use `count > 0 && ...`.
- Writing `class` instead of `className`, or `for` instead of `htmlFor`.
- Mutating props inside a component — props are read-only.

## ❓ FAQs

### What is JSX and how does it work under the hood?
JSX is a syntax extension that looks like HTML but compiles down to `React.createElement(type, props, ...children)` calls (or the modern automatic runtime). The build tool (Vite/Babel/tsc) transforms it before it runs in the browser. So `<h1>Hi</h1>` becomes a function call that returns a plain JavaScript object describing the element.

### What is the difference between props and children?
Props are named inputs you pass like attributes: `<Card title="Hi" />`. `children` is a special prop containing whatever you nest *between* the opening and closing tags: `<Card>this is children</Card>`. `children` lets a component wrap arbitrary content, which is how layout and container components work.

### Why do list items need a key?
Keys give React a stable identity for each list item so it can match items between renders and update, reorder, or remove them efficiently instead of rebuilding everything. Without stable keys, React may reuse the wrong DOM nodes, causing subtle bugs with input state or animations. Use a unique id, not the array index, when items can change order.

### What is a Fragment and when do you use it?
A Fragment (`<>...</>` or `<React.Fragment>`) groups multiple elements without adding an extra wrapper node to the DOM. You use it when a component must return several siblings but you don't want an unnecessary `<div>` cluttering the markup or breaking CSS layouts like flex/grid.

### Why must component names start with a capital letter?
JSX uses capitalization to decide between a built-in HTML element and your component. A lowercase tag like `<button>` is treated as a DOM element, while an uppercase tag like `<Button>` is treated as a reference to your component variable. Lowercase custom components silently render as unknown HTML tags.
