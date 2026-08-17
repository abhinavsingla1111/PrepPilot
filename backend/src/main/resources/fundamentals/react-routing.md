# 🧭 Client-Side Routing

> Turning URLs into UI without full page reloads — and building breadcrumbs from the route.

## 🧠 What & Why

A traditional website asks the server for a brand-new HTML page every time you click a link. A **single-page application (SPA)** loads once, then swaps content in place as the URL changes. **Client-side routing** is what maps each URL to the components that should render — no full reload, no flash of white screen.

Libraries like **react-router-dom** provide the pieces: a **router** that watches the URL, **routes** that match paths to components, **links** that change the URL without reloading, and **params** that capture dynamic parts of the path (like an id).

Because the URL is now structured data, you can also *derive* UI from it — the classic example being **breadcrumbs**, which show the user where they are in the app's hierarchy.

## 🔑 Key Concepts

- **Router** — Watches the browser URL and renders the matching route.
- **Route** — A mapping from a URL pattern (`/questions/:id`) to a component.
- **Link** — Navigates by changing the URL client-side instead of reloading.
- **Params** — Dynamic segments of the path, read with a hook like `useParams`.
- **Nested routes** — Routes inside routes, sharing a parent layout via an outlet.
- **Breadcrumbs** — A trail derived from the current path showing the navigation hierarchy.

## 💻 Example

Basic routes and navigation with `react-router-dom`:

```tsx
import { BrowserRouter, Routes, Route, Link, useParams } from 'react-router-dom'

function QuestionDetail() {
  const { id } = useParams() // reads :id from the URL
  return <h1>Question {id}</h1>
}

export default function App() {
  return (
    <BrowserRouter>
      <nav>
        <Link to="/">Home</Link>
        <Link to="/questions/42">Question 42</Link>
      </nav>

      <Routes>
        <Route path="/" element={<Home />} />
        {/* :id is a dynamic param */}
        <Route path="/questions/:id" element={<QuestionDetail />} />
      </Routes>
    </BrowserRouter>
  )
}
```

## 🍞 Building Breadcrumbs from the Route

Split the current pathname into segments and turn each into a link:

```tsx
import { Link, useLocation } from 'react-router-dom'

function Breadcrumbs() {
  const { pathname } = useLocation() // e.g. "/questions/arrays/42"
  const parts = pathname.split('/').filter(Boolean) // ["questions","arrays","42"]

  return (
    <nav aria-label="breadcrumb">
      <Link to="/">Home</Link>
      {parts.map((part, i) => {
        // Rebuild the URL up to this segment.
        const to = '/' + parts.slice(0, i + 1).join('/')
        return (
          <span key={to}>
            {' / '}
            <Link to={to}>{part}</Link>
          </span>
        )
      })}
    </nav>
  )
}
// Renders: Home / questions / arrays / 42
```

## 🧱 Nested Routes

| Concept | What it does |
| --- | --- |
| Parent route | Provides a shared layout (header, sidebar) |
| `<Outlet />` | Placeholder where child routes render |
| Child route | Renders inside the parent's outlet |
| Index route | The default child shown at the parent's path |

## ⚠️ Common Pitfalls

- Using a plain `<a href>` for internal navigation — it triggers a full reload; use `<Link>`.
- Forgetting to configure the server to serve `index.html` for all routes (SPA fallback), causing 404s on refresh.
- Reading params from `window.location` manually instead of `useParams`.
- Building breadcrumb URLs incorrectly by not accumulating segments up to the current index.
- Deeply nesting routes without a shared layout/outlet, duplicating chrome.

## ❓ FAQs

### What is client-side routing and how does it differ from server-side routing?
Client-side routing keeps the app on a single loaded page and swaps components as the URL changes, using the History API — no full page reload. Server-side routing asks the server for a new HTML document per URL. Client-side routing feels faster and preserves in-memory state, but requires the server to serve the app's `index.html` for unknown paths so deep links and refreshes work.

### How do I read a dynamic parameter from the URL?
Define the route with a colon segment, e.g. `path="/questions/:id"`, then call `useParams()` inside the component to get `{ id }`. The param is always a string, so convert it if you need a number. This is how detail pages load the right record based on the URL.

### Why should I use Link instead of a normal anchor tag?
A plain `<a href>` causes the browser to make a full request and reload the whole app, discarding React state and re-downloading assets. `<Link>` intercepts the click, updates the URL via the History API, and lets the router render the new route in place — fast, stateful navigation. Use anchors only for external links.

### How do you build breadcrumbs from the current route?
Take the current pathname, split it on `/`, and drop empty segments. Then map each segment to a link whose target is the path accumulated *up to and including* that segment (`parts.slice(0, i + 1).join('/')`). Prepend a Home link. This produces a clickable trail that mirrors the URL hierarchy and updates automatically as the route changes.

### Why does refreshing a deep route sometimes 404?
With client-side routing, only the app knows about routes like `/questions/42`; the server may not have a file there. On refresh the browser asks the server directly, which returns 404 unless it's configured to serve `index.html` for all paths (an SPA fallback). Configuring that fallback lets the router handle the URL after the app loads.
