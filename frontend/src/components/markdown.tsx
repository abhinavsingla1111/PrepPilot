import { useEffect, useId, useRef, useState, type ComponentPropsWithoutRef } from 'react'
import ReactMarkdown from 'react-markdown'
import remarkGfm from 'remark-gfm'
import rehypeHighlight from 'rehype-highlight'
import { AlertTriangle, Check, Copy } from 'lucide-react'
import { isColorScheme, type ColorScheme } from '../lib/colorScheme'

let mermaidLoader: Promise<typeof import('mermaid').default> | null = null

function loadMermaid() {
  if (!mermaidLoader) {
    mermaidLoader = import('mermaid').then((module) => module.default)
  }
  return mermaidLoader
}

function readAppliedColorScheme(): ColorScheme {
  const applied = document.documentElement.dataset.colorScheme ?? null
  return isColorScheme(applied) ? applied : 'dark'
}

function useAppliedColorScheme() {
  const [scheme, setScheme] = useState<ColorScheme>(readAppliedColorScheme)

  useEffect(() => {
    const root = document.documentElement
    const observer = new MutationObserver(() => setScheme(readAppliedColorScheme()))
    observer.observe(root, { attributes: true, attributeFilter: ['data-color-scheme'] })
    return () => observer.disconnect()
  }, [])

  return scheme
}

function configureMermaid(mermaid: typeof import('mermaid').default) {
  const styles = getComputedStyle(document.documentElement)
  const token = (name: string) => styles.getPropertyValue(name).trim()

  const background = token('--surface-inset')
  const panel = token('--panel')
  const panelRaised = token('--panel-raised')
  const text = token('--text')
  const muted = token('--muted')
  const accent = token('--accent')
  const accentBright = token('--accent-bright')
  const lineStrong = token('--line-strong')

  mermaid.initialize({
    startOnLoad: false,
    securityLevel: 'strict',
    theme: 'base',
    themeVariables: {
      fontFamily: 'Inter, ui-sans-serif, sans-serif',
      background,
      primaryColor: panel,
      primaryBorderColor: accent,
      primaryTextColor: text,
      secondaryColor: panelRaised,
      secondaryBorderColor: accent,
      secondaryTextColor: text,
      tertiaryColor: background,
      tertiaryBorderColor: lineStrong,
      tertiaryTextColor: text,
      lineColor: accentBright,
      textColor: text,
      nodeBorder: accent,
      edgeLabelBackground: background,
      clusterBkg: background,
      clusterBorder: lineStrong,
      titleColor: text,
      actorBkg: panel,
      actorBorder: accent,
      actorTextColor: text,
      actorLineColor: muted,
      signalColor: accentBright,
      signalTextColor: text,
      labelBoxBkgColor: background,
      labelBoxBorderColor: lineStrong,
      labelTextColor: text,
      loopTextColor: text,
      noteBkgColor: panelRaised,
      noteBorderColor: accent,
      noteTextColor: text,
      activationBkgColor: panelRaised,
      activationBorderColor: accent,
    },
  })
}

function Mermaid({ chart }: { chart: string }) {
  const [svg, setSvg] = useState('')
  const [failed, setFailed] = useState(false)
  const reactId = useId()
  const colorScheme = useAppliedColorScheme()

  useEffect(() => {
    let active = true
    const id = `mmd-${reactId.replace(/[^a-zA-Z0-9_-]/g, '')}`
    const cleanTemporaryNodes = () => {
      document.getElementById(id)?.remove()
      document.getElementById(`d${id}`)?.remove()
    }
    setSvg('')
    setFailed(false)
    loadMermaid()
      .then(async (mermaid) => {
        configureMermaid(mermaid)
        const valid = await mermaid.parse(chart, { suppressErrors: true })
        if (!valid) throw new Error('Invalid Mermaid diagram')
        return mermaid.render(id, chart)
      })
      .then(({ svg }) => {
        if (active) setSvg(svg)
      })
      .catch(() => {
        cleanTemporaryNodes()
        if (active) setFailed(true)
      })
    return () => {
      active = false
      cleanTemporaryNodes()
    }
  }, [chart, colorScheme, reactId])

  if (failed) {
    return (
      <div className="cs-diagram-error" role="status">
        <AlertTriangle size={18} aria-hidden="true" />
        <div><strong>Diagram temporarily unavailable</strong><span>The explanation below is still available while this visual is corrected.</span></div>
      </div>
    )
  }
  return <div className="cs-diagram" dangerouslySetInnerHTML={{ __html: svg }} />
}

const OPEN_BRACKETS = '([{'
const CLOSE_BRACKETS = ')]}'

// VS Code-style bracket-pair colorization: walk the highlighted code and wrap
// each bracket in a depth-coloured span so nesting is visible like in an IDE.
function colorizeBrackets(root: HTMLElement) {
  const walker = document.createTreeWalker(root, NodeFilter.SHOW_TEXT)
  const textNodes: Text[] = []
  let current = walker.nextNode()
  while (current) {
    textNodes.push(current as Text)
    current = walker.nextNode()
  }

  let depth = 0
  for (const node of textNodes) {
    // Don't colour brackets that live inside strings or comments.
    if (node.parentElement?.closest('.hljs-string, .hljs-comment, .hljs-quote')) continue
    const text = node.nodeValue ?? ''
    if (!/[()[\]{}]/.test(text)) continue

    const fragment = document.createDocumentFragment()
    let buffer = ''
    const flush = () => {
      if (buffer) {
        fragment.appendChild(document.createTextNode(buffer))
        buffer = ''
      }
    }

    for (const char of text) {
      if (OPEN_BRACKETS.includes(char)) {
        flush()
        const span = document.createElement('span')
        span.className = `tok-bracket tok-bracket-${depth % 3}`
        span.textContent = char
        fragment.appendChild(span)
        depth += 1
      } else if (CLOSE_BRACKETS.includes(char)) {
        flush()
        depth = Math.max(0, depth - 1)
        const span = document.createElement('span')
        span.className = `tok-bracket tok-bracket-${depth % 3}`
        span.textContent = char
        fragment.appendChild(span)
      } else {
        buffer += char
      }
    }
    flush()
    node.parentNode?.replaceChild(fragment, node)
  }
}

function extractCode(node: unknown): { className: string; text: string } | null {
  if (node && typeof node === 'object' && 'props' in node) {
    const props = (node as { props?: { className?: string; children?: unknown } }).props ?? {}
    const rawChildren = props.children
    const text = typeof rawChildren === 'string'
      ? rawChildren
      : Array.isArray(rawChildren)
        ? rawChildren.join('')
        : String(rawChildren ?? '')
    return { className: props.className ?? '', text }
  }
  return null
}

function CodeBlock(props: ComponentPropsWithoutRef<'pre'>) {
  const { children } = props
  const preRef = useRef<HTMLPreElement>(null)
  const [copied, setCopied] = useState(false)

  const info = extractCode(children)
  const isMermaid = Boolean(info && info.className.includes('language-mermaid'))

  useEffect(() => {
    if (isMermaid) return
    const code = preRef.current?.querySelector('code')
    if (!code || code.dataset.bracketized === 'true') return
    colorizeBrackets(code as HTMLElement)
    code.dataset.bracketized = 'true'
  }, [children, isMermaid])

  if (info && isMermaid) {
    return <Mermaid chart={info.text.trim()} />
  }

  const onCopy = async () => {
    const text = preRef.current?.innerText ?? ''
    try {
      await navigator.clipboard.writeText(text)
      setCopied(true)
      window.setTimeout(() => setCopied(false), 1400)
    } catch {
      /* clipboard unavailable */
    }
  }

  return (
    <div className="cs-code">
      <button type="button" className="cs-copy" onClick={onCopy} aria-label="Copy code">
        {copied ? <Check size={13} aria-hidden="true" /> : <Copy size={13} aria-hidden="true" />}
        <span>{copied ? 'Copied' : 'Copy'}</span>
      </button>
      <pre ref={preRef}>{children}</pre>
    </div>
  )
}

const markdownComponents = { pre: CodeBlock }
const rehypePlugins = [[rehypeHighlight, { detect: true, ignoreMissing: true, plainText: ['mermaid'] }]] as never

interface FaqEntry {
  question: string
  answer: string
}

interface FaqSection {
  content: string
  entries: FaqEntry[]
}

function extractFaqSection(markdown: string): FaqSection | null {
  const faqHeading = /^##\s+(?:❓\s*)?FAQs?\s*$/im.exec(markdown)
  if (!faqHeading || faqHeading.index === undefined) return null

  const content = markdown.slice(0, faqHeading.index).trimEnd()
  const faqBody = markdown.slice(faqHeading.index + faqHeading[0].length)
  const headings = Array.from(faqBody.matchAll(/^###\s+(.+?)\s*$/gm))
  if (headings.length === 0) return null

  const entries = headings.map((heading, index) => {
    const start = (heading.index ?? 0) + heading[0].length
    const end = headings[index + 1]?.index ?? faqBody.length
    return {
      question: heading[1].trim(),
      answer: faqBody.slice(start, end).trim(),
    }
  }).filter((entry) => entry.question.length > 0 && entry.answer.length > 0)

  return entries.length > 0 ? { content, entries } : null
}

function FaqAccordion({ entries }: { entries: FaqEntry[] }) {
  return (
    <section className="sd-faq" aria-labelledby="sd-faq-title">
      <header className="sd-faq-header">
        <span className="sd-faq-eyebrow">Interview follow-ups</span>
        <h2 id="sd-faq-title">FAQs</h2>
        <p>Open a question to review the reasoning, trade-off, and concise interview answer.</p>
      </header>

      <div className="sd-faq-list">
        {entries.map((entry, index) => (
          <details className="sd-faq-item" key={entry.question} open={index === 0}>
            <summary>
              <span className="sd-faq-mark" aria-hidden="true">Q</span>
              <span className="sd-faq-question">{entry.question}</span>
              <span className="sd-faq-toggle" aria-hidden="true" />
            </summary>
            <div className="sd-faq-answer">
              <span className="sd-faq-answer-mark" aria-hidden="true">A</span>
              <div className="sd-faq-answer-copy">
                <Markdown>{entry.answer}</Markdown>
              </div>
            </div>
          </details>
        ))}
      </div>
    </section>
  )
}

export function Markdown({ children, systemDesignFaq = false }: { children: string; systemDesignFaq?: boolean }) {
  if (systemDesignFaq) {
    const faq = extractFaqSection(children)
    if (faq) {
      return (
        <>
          <Markdown>{faq.content}</Markdown>
          <FaqAccordion entries={faq.entries} />
        </>
      )
    }
  }

  return (
    <ReactMarkdown remarkPlugins={[remarkGfm]} rehypePlugins={rehypePlugins} components={markdownComponents}>
      {children}
    </ReactMarkdown>
  )
}
