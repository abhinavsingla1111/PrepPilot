import { useEffect, useMemo, useRef, useState } from 'react'
import { Search, X } from 'lucide-react'
import { api } from '../lib/api'
import type { Cheatsheet } from '../types/api'
import { Markdown } from './markdown'

interface Section {
  id: string
  title: string
  body: string
}

function slugify(input: string): string {
  return input
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/(^-|-$)/g, '')
}

function parse(markdown: string): { intro: string; sections: Section[] } {
  const parts = markdown.split(/\n(?=## )/)
  const intro = parts.length && !parts[0].startsWith('## ') ? (parts.shift() ?? '') : ''
  const sections = parts.map((chunk) => {
    const newline = chunk.indexOf('\n')
    const title = (newline === -1 ? chunk : chunk.slice(0, newline))
      .replace(/^##\s*/, '')
      .replace(/:$/, '')
      .trim()
    const body = (newline === -1 ? '' : chunk.slice(newline + 1)).replace(/\n+---\s*$/, '')
    return { id: slugify(title), title, body }
  })
  return { intro, sections }
}

const LANGUAGES = [
  { id: 'java', label: 'Java' },
  { id: 'python', label: 'Python' },
  { id: 'cpp', label: 'C++' },
]

interface CheatSheetProps {
  topic?: 'dsa' | 'oop'
}

export function CheatSheet({ topic = 'dsa' }: CheatSheetProps) {
  const [language, setLanguage] = useState('java')
  const [data, setData] = useState<Cheatsheet | null>(null)
  const [failed, setFailed] = useState(false)
  const [query, setQuery] = useState('')
  const [activeId, setActiveId] = useState('')
  const suppressObserver = useRef(false)
  const suppressTimer = useRef<ReturnType<typeof setTimeout> | undefined>(undefined)

  const kindLabel = topic === 'oop' ? 'OOP concepts' : 'cheat sheet'
  const routeHash = topic === 'oop' ? 'oop' : 'cheatsheet'

  useEffect(() => {
    window.scrollTo({ top: 0 })
    setData(null)
    setFailed(false)
    const request = topic === 'oop' ? api.getOop(language) : api.getCheatsheet(language)
    request.then(setData).catch(() => setFailed(true))
  }, [language, topic])

  const parsed = useMemo(() => (data ? parse(data.content) : null), [data])

  const sections = useMemo(() => {
    if (!parsed) return []
    const q = query.trim().toLowerCase()
    if (!q) return parsed.sections
    return parsed.sections.filter(
      (section) => section.title.toLowerCase().includes(q) || section.body.toLowerCase().includes(q),
    )
  }, [parsed, query])

  useEffect(() => {
    if (!sections.length) return
    const observer = new IntersectionObserver(
      (entries) => {
        if (suppressObserver.current) return
        const visible = entries
          .filter((entry) => entry.isIntersecting)
          .sort((a, b) => a.boundingClientRect.top - b.boundingClientRect.top)
        if (visible[0]) setActiveId(visible[0].target.id)
      },
      { rootMargin: '-100px 0px -68% 0px', threshold: 0 },
    )
    sections.forEach((section) => {
      const element = document.getElementById(section.id)
      if (element) observer.observe(element)
    })
    return () => observer.disconnect()
  }, [sections])

  useEffect(() => {
    return () => {
      if (suppressTimer.current) clearTimeout(suppressTimer.current)
    }
  }, [])

  const scrollToSection = (id: string) => {
    // Highlight the clicked section immediately and pause the observer while the
    // smooth scroll runs, so its lagging final entry can't select the section above.
    setActiveId(id)
    suppressObserver.current = true
    if (suppressTimer.current) clearTimeout(suppressTimer.current)
    suppressTimer.current = setTimeout(() => {
      suppressObserver.current = false
    }, 700)
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }

  if (failed) {
    return (
      <main className="cheatsheet" data-section={`cheatsheet-${language}`}>
        <div className="shell">
          <p className="cs-status">Couldn&apos;t load the {kindLabel}. Please try again later.</p>
        </div>
      </main>
    )
  }

  if (!parsed) {
    return (
      <main className="cheatsheet" data-section={`cheatsheet-${language}`}>
        <div className="shell">
          <p className="cs-status">Loading {kindLabel}…</p>
        </div>
      </main>
    )
  }

  return (
    <main className="cheatsheet" data-section={`cheatsheet-${language}`}>
      <div className="shell cs-layout">
        <aside className="cs-sidebar">
          <div className="cs-langs" role="tablist" aria-label={`${kindLabel} language`}>
            {LANGUAGES.map((lang) => (
              <button
                key={lang.id}
                type="button"
                role="tab"
                aria-selected={language === lang.id}
                className={`cs-lang ${language === lang.id ? 'is-active' : ''}`}
                onClick={() => {
                  setQuery('')
                  setLanguage(lang.id)
                }}
              >
                {lang.label}
              </button>
            ))}
          </div>
          <div className="cs-search">
            <Search size={16} aria-hidden="true" />
            <input
              type="search"
              value={query}
              onChange={(event) => setQuery(event.target.value)}
              placeholder={`Search the ${kindLabel}…`}
              aria-label={`Search the ${kindLabel}`}
            />
            {query && (
              <button type="button" onClick={() => setQuery('')} aria-label="Clear search">
                <X size={14} aria-hidden="true" />
              </button>
            )}
          </div>
          <nav className="cs-toc" aria-label={`${kindLabel} sections`}>
            {sections.map((section) => (
              <a
                key={section.id}
                href={`#${routeHash}`}
                className={activeId === section.id ? 'is-active' : ''}
                onClick={(event) => {
                  event.preventDefault()
                  scrollToSection(section.id)
                }}
              >
                {section.title}
              </a>
            ))}
            {!sections.length && <p className="cs-empty">No matches.</p>}
          </nav>
        </aside>

        <div className="cs-content">
          <header className="cs-intro">
            <Markdown>{parsed.intro}</Markdown>
          </header>

          {sections.map((section) => (
            <section key={section.id} id={section.id} className="cs-section">
              <h2>{section.title}</h2>
              <Markdown>{section.body}</Markdown>
            </section>
          ))}

          {!sections.length && (
            <p className="cs-empty">No sections match &ldquo;{query}&rdquo;.</p>
          )}
        </div>
      </div>
    </main>
  )
}
