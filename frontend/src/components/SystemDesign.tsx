import { useEffect, useMemo, useRef, useState } from 'react'
import { ChevronRight } from 'lucide-react'
import { api } from '../lib/api'
import type { Cheatsheet } from '../types/api'
import { Markdown } from './markdown'

interface DocItem {
  id: string
  title: string
}

interface Section {
  key: string
  label: string
  blurb: string
  defaultDoc: string
  items?: DocItem[]
}

const SECTIONS: Section[] = [
  {
    key: 'overview',
    label: 'Overview',
    blurb: 'Start here',
    defaultDoc: 'overview',
  },
  {
    key: 'hld',
    label: 'High-Level Design',
    blurb: 'Architecture of large systems',
    defaultDoc: 'hld-overview',
    items: [
      { id: 'hld-overview', title: 'Overview' },
      { id: 'hld-url-shortener', title: 'URL Shortener (TinyURL)' },
      { id: 'hld-rate-limiter', title: 'Rate Limiter' },
      { id: 'hld-news-feed', title: 'News Feed / Timeline' },
      { id: 'hld-chat-system', title: 'Chat System (WhatsApp)' },
      { id: 'hld-video-streaming', title: 'Video Streaming (YouTube)' },
      { id: 'hld-notification-system', title: 'Notification System' },
      { id: 'hld-web-crawler', title: 'Web Crawler' },
      { id: 'hld-distributed-cache', title: 'Distributed Cache' },
      { id: 'hld-ride-sharing', title: 'Ride Sharing (Uber)' },
      { id: 'hld-payment-system', title: 'Payment System' },
    ],
  },
  {
    key: 'lld',
    label: 'Low-Level Design',
    blurb: 'Classes, objects & patterns',
    defaultDoc: 'lld-overview',
    items: [
      { id: 'lld-overview', title: 'Overview' },
      { id: 'lld-parking-lot', title: 'Parking Lot' },
      { id: 'lld-elevator', title: 'Elevator System' },
      { id: 'lld-tic-tac-toe', title: 'Tic-Tac-Toe' },
      { id: 'lld-vending-machine', title: 'Vending Machine' },
      { id: 'lld-lru-cache', title: 'LRU Cache' },
      { id: 'lld-splitwise', title: 'Splitwise' },
      { id: 'lld-library', title: 'Library Management' },
      { id: 'lld-snake-ladder', title: 'Snake & Ladder' },
      { id: 'lld-logging', title: 'Logging Framework' },
      { id: 'lld-movie-booking', title: 'Movie Ticket Booking' },
    ],
  },
]

const DOC_TITLES: Record<string, string> = SECTIONS.reduce((acc, section) => {
  acc[section.defaultDoc] = section.label
  section.items?.forEach((item) => {
    acc[item.id] = item.title
  })
  return acc
}, {} as Record<string, string>)

function sectionForDoc(docId: string): string {
  const match = SECTIONS.find(
    (section) => section.defaultDoc === docId || section.items?.some((item) => item.id === docId),
  )
  return match?.key ?? 'overview'
}

export function SystemDesign() {
  const [docId, setDocId] = useState('overview')
  const [openKey, setOpenKey] = useState<string>(() => sectionForDoc('overview'))
  const [data, setData] = useState<Cheatsheet | null>(null)
  const [failed, setFailed] = useState(false)
  const activeItemRef = useRef<HTMLAnchorElement>(null)

  const activeSection = useMemo(() => sectionForDoc(docId), [docId])

  useEffect(() => {
    const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches
    window.scrollTo({ top: 0, behavior: reduceMotion ? 'auto' : 'smooth' })
    setData(null)
    setFailed(false)
    api.getSystemDesign(docId).then(setData).catch(() => setFailed(true))
    window.requestAnimationFrame(() => activeItemRef.current?.scrollIntoView({ block: 'nearest' }))
  }, [docId])

  const selectSection = (section: Section) => {
    // Clicking an already-open section that has a sub-list collapses it.
    if (openKey === section.key && section.items) {
      setOpenKey('')
      return
    }
    setOpenKey(section.key)
    // Only jump to the section's overview if we're not already reading inside it.
    if (activeSection !== section.key && section.defaultDoc !== docId) {
      setDocId(section.defaultDoc)
    }
  }

  const selectDoc = (id: string) => {
    if (id !== docId) setDocId(id)
  }

  return (
    <main className="cheatsheet" data-section={`system-${activeSection}`}>
      <div className="shell cs-layout">
        <aside className="cs-sidebar sd-sidebar">
          {SECTIONS.map((section) => {
            const expanded = openKey === section.key
            const isActive = activeSection === section.key
            return (
              <div className={`sd-section ${expanded ? 'is-open' : ''}`} key={section.key}>
                <button
                  type="button"
                  className={`sd-section-head ${isActive ? 'is-active' : ''}`}
                  aria-expanded={section.items ? expanded : undefined}
                  onClick={() => selectSection(section)}
                >
                  <span className="sd-section-text">
                    <span className="sd-section-label">{section.label}</span>
                    <span className="sd-section-blurb">{section.blurb}</span>
                  </span>
                  {section.items && <ChevronRight size={15} className="sd-caret" aria-hidden="true" />}
                </button>

                {expanded && section.items && (
                  <nav className="sd-nav" aria-label={section.label}>
                    {section.items.map((item) => (
                      <a
                        key={item.id}
                        ref={item.id === docId ? activeItemRef : undefined}
                        href="#systemdesign"
                        className={item.id === docId ? 'is-active' : ''}
                        onClick={(event) => {
                          event.preventDefault()
                          selectDoc(item.id)
                        }}
                      >
                        {item.title}
                      </a>
                    ))}
                  </nav>
                )}
              </div>
            )
          })}
        </aside>

        <div className="cs-content">
          {failed ? (
            <p className="cs-status">Couldn&apos;t load this document. Please try again later.</p>
          ) : !data ? (
            <p className="cs-status">Loading {DOC_TITLES[docId] ?? 'document'}…</p>
          ) : (
            <article className="cs-section sd-article">
              <Markdown systemDesignFaq>{data.content}</Markdown>
            </article>
          )}
        </div>
      </div>
    </main>
  )
}
