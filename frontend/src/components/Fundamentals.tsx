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
    key: 'java',
    label: 'Java',
    blurb: 'The language, from scratch',
    defaultDoc: 'java-overview',
    items: [
      { id: 'java-overview', title: 'Overview' },
      { id: 'java-basics', title: 'Syntax & Basics' },
      { id: 'java-collections', title: 'Collections Framework' },
      { id: 'java-strings', title: 'Strings' },
      { id: 'java-exceptions', title: 'Exceptions' },
      { id: 'java-generics', title: 'Generics' },
      { id: 'java-streams', title: 'Streams & Lambdas' },
      { id: 'java-concurrency', title: 'Concurrency & Threads' },
      { id: 'java-jvm-memory', title: 'JVM & Memory' },
    ],
  },
  {
    key: 'spring',
    label: 'Spring Boot',
    blurb: 'The framework & its magic',
    defaultDoc: 'spring-overview',
    items: [
      { id: 'spring-overview', title: 'Overview' },
      { id: 'spring-ioc-di', title: 'IoC & Dependency Injection' },
      { id: 'spring-beans', title: 'Beans & Lifecycle' },
      { id: 'spring-annotations', title: 'Core Annotations' },
      { id: 'spring-rest', title: 'REST Controllers' },
      { id: 'spring-data-jpa', title: 'Data & JPA' },
      { id: 'spring-config-profiles', title: 'Config & Profiles' },
      { id: 'spring-exception-handling', title: 'Exception Handling' },
      { id: 'spring-security', title: 'Security Basics' },
      { id: 'spring-testing', title: 'Testing' },
    ],
  },
  {
    key: 'react',
    label: 'React',
    blurb: 'Modern UI with hooks',
    defaultDoc: 'react-overview',
    items: [
      { id: 'react-overview', title: 'Overview' },
      { id: 'react-components-jsx', title: 'Components & JSX' },
      { id: 'react-usestate', title: 'State with useState' },
      { id: 'react-useeffect', title: 'Effects with useEffect' },
      { id: 'react-hooks', title: 'Other Hooks' },
      { id: 'react-api-calls', title: 'Calling APIs' },
      { id: 'react-routing', title: 'Routing & Breadcrumbs' },
      { id: 'react-forms', title: 'Forms & Events' },
      { id: 'react-performance', title: 'Performance' },
    ],
  },
  {
    key: 'python',
    label: 'Python',
    blurb: 'Language & FastAPI',
    defaultDoc: 'python-overview',
    items: [
      { id: 'python-overview', title: 'Overview' },
      { id: 'python-basics', title: 'Syntax & Basics' },
      { id: 'python-data-structures', title: 'Data Structures' },
      { id: 'python-functions', title: 'Functions & Decorators' },
      { id: 'python-oop', title: 'Classes & OOP' },
      { id: 'python-app-structure', title: 'Building an App' },
      { id: 'python-fastapi', title: 'FastAPI' },
      { id: 'python-async', title: 'Async & Concurrency' },
    ],
  },
  {
    key: 'database',
    label: 'Database: SQL / NoSQL',
    blurb: 'Queries, joins & data models',
    defaultDoc: 'db-overview',
    items: [
      { id: 'db-overview', title: 'Overview: SQL vs NoSQL' },
      { id: 'db-sql-basics', title: 'SQL Basics (SELECT)' },
      { id: 'db-sql-filtering', title: 'Filtering & Operators' },
      { id: 'db-sql-joins', title: 'Joins' },
      { id: 'db-sql-aggregation', title: 'Grouping & Aggregation' },
      { id: 'db-sql-functions', title: 'Built-in Functions' },
      { id: 'db-sql-ddl-constraints', title: 'Tables & Constraints' },
      { id: 'db-sql-dml', title: 'Insert, Update, Delete' },
      { id: 'db-sql-subqueries', title: 'Subqueries' },
      { id: 'db-sql-advanced', title: 'Advanced & Complex Queries' },
      { id: 'db-nosql', title: 'NoSQL Explained' },
      { id: 'db-sql-vs-mysql', title: 'SQL vs MySQL' },
    ],
  },
  {
    key: 'operating-systems',
    label: 'Operating Systems',
    blurb: 'Processes, memory & I/O',
    defaultDoc: 'os-overview',
    items: [
      { id: 'os-overview', title: 'The Big Picture' },
      { id: 'os-processes-threads', title: 'Processes & Threads' },
      { id: 'os-cpu-scheduling', title: 'CPU Scheduling' },
      { id: 'os-synchronization', title: 'Synchronization' },
      { id: 'os-deadlocks', title: 'Deadlocks & Liveness' },
      { id: 'os-memory-management', title: 'Memory Management' },
      { id: 'os-virtual-memory', title: 'Virtual Memory & Paging' },
      { id: 'os-filesystems-io', title: 'Filesystems & I/O' },
      { id: 'os-ipc', title: 'Inter-Process Communication' },
      { id: 'os-security-revision', title: 'Security & Revision' },
    ],
  },
  {
    key: 'networks',
    label: 'Computer Networks',
    blurb: 'Protocols, packets & the web',
    defaultDoc: 'network-overview',
    items: [
      { id: 'network-overview', title: 'The Big Picture' },
      { id: 'network-data-link', title: 'Ethernet, MAC & Switching' },
      { id: 'network-ip-subnetting', title: 'IP, CIDR & Subnetting' },
      { id: 'network-routing', title: 'Routing & Internet Paths' },
      { id: 'network-transport', title: 'TCP, UDP & QUIC' },
      { id: 'network-dns-web', title: 'DNS, HTTP & the Web' },
      { id: 'network-application-protocols', title: 'Application Protocols' },
      { id: 'network-security', title: 'TLS & Network Security' },
      { id: 'network-performance', title: 'Performance & Reliability' },
      { id: 'network-troubleshooting-revision', title: 'Troubleshooting & Revision' },
    ],
  },
  {
    key: 'oop',
    label: 'OOP Concepts',
    blurb: 'The four pillars & SOLID',
    defaultDoc: 'oop-overview',
    items: [
      { id: 'oop-overview', title: 'Overview' },
      { id: 'oop-encapsulation', title: 'Encapsulation' },
      { id: 'oop-abstraction', title: 'Abstraction' },
      { id: 'oop-inheritance', title: 'Inheritance' },
      { id: 'oop-polymorphism', title: 'Polymorphism' },
      { id: 'oop-solid', title: 'SOLID Principles' },
      { id: 'oop-design-patterns', title: 'Design Patterns' },
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

interface FundamentalsProps {
  initialDoc?: string
}

export function Fundamentals({ initialDoc = 'overview' }: FundamentalsProps) {
  const [docId, setDocId] = useState(initialDoc)
  const [openKey, setOpenKey] = useState<string>(() => sectionForDoc(initialDoc))
  const [data, setData] = useState<Cheatsheet | null>(null)
  const [failed, setFailed] = useState(false)
  const activeItemRef = useRef<HTMLAnchorElement>(null)

  const activeSection = useMemo(() => sectionForDoc(docId), [docId])

  useEffect(() => {
    const reduceMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches
    window.scrollTo({ top: 0, behavior: reduceMotion ? 'auto' : 'smooth' })
    setData(null)
    setFailed(false)
    api.getFundamentals(docId).then(setData).catch(() => setFailed(true))
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
    <main className="cheatsheet" data-section={activeSection}>
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
                        href="#fundamentals"
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
