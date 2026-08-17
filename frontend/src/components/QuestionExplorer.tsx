import {
  ArrowLeft,
  ArrowRight,
  Check,
  CheckCircle2,
  Circle,
  CircleDashed,
  ExternalLink,
  ListFilter,
  Search,
} from 'lucide-react'
import { useCallback, useEffect, useMemo, useState } from 'react'
import { useDebouncedValue } from '../hooks/useDebouncedValue'
import { api } from '../lib/api'
import { fallbackQuestions } from '../lib/fallbackQuestions'
import type { CurrentUser, Difficulty, PageResponse, Problem } from '../types/api'
import { ConfirmDialog } from './ConfirmDialog'
import { Dropdown } from './Dropdown'
import { LeetCodeCard } from './LeetCodeCard'

interface QuestionExplorerProps {
  user: CurrentUser | null
  onLogin: () => void
  pageSize?: number
}

const fallbackPage: PageResponse<Problem> = {
  content: fallbackQuestions,
  number: 0,
  size: fallbackQuestions.length,
  totalElements: 145,
  totalPages: 1,
  first: true,
  last: true,
}

const difficultyLabel: Record<Difficulty, string> = {
  EASY: 'Easy',
  MEDIUM: 'Medium',
  HARD: 'Hard',
}

const difficultyOptions = [
  { value: '', label: 'All difficulties' },
  { value: 'EASY', label: 'Easy' },
  { value: 'MEDIUM', label: 'Medium' },
  { value: 'HARD', label: 'Hard' },
]

const statusOrder = ['', 'SOLVED', 'UNSOLVED']
const statusMeta: Record<string, string> = {
  '': 'Status: all',
  SOLVED: 'Showing solved',
  UNSOLVED: 'Showing unsolved',
}

export function QuestionExplorer({ user, onLogin, pageSize = 10 }: QuestionExplorerProps) {
  const [data, setData] = useState<PageResponse<Problem>>(fallbackPage)
  const [topics, setTopics] = useState<string[]>([])
  const [query, setQuery] = useState('')
  const [difficulty, setDifficulty] = useState('')
  const [topic, setTopic] = useState('')
  const [status, setStatus] = useState('')
  const [page, setPage] = useState(0)
  const [loading, setLoading] = useState(true)
  const [offline, setOffline] = useState(false)
  const [solvedIds, setSolvedIds] = useState<Set<number>>(new Set())
  const [updatingId, setUpdatingId] = useState<number | null>(null)
  const [confirmProblem, setConfirmProblem] = useState<Problem | null>(null)
  const debouncedQuery = useDebouncedValue(query)

  const loadProgress = useCallback(() => {
    api.getProgress()
      .then((items) => setSolvedIds(new Set(items.filter((item) => item.status === 'SOLVED').map((item) => item.problemId))))
      .catch(() => setSolvedIds(new Set()))
  }, [])

  useEffect(() => {
    api.getTopics().then(setTopics).catch(() => setTopics([
      'Arrays & Strings', 'Hashing', 'Linked List', 'Stacks & Queues', 'Trees & Graphs', 'Heap', 'Greedy', 'Dynamic Programming',
    ]))
  }, [])

  useEffect(() => {
    const controller = new AbortController()
    const params = new URLSearchParams({ page: String(page), size: String(pageSize) })
    if (debouncedQuery) params.set('query', debouncedQuery)
    if (difficulty) params.set('difficulty', difficulty)
    if (topic) params.set('topic', topic)
    if (status && user) params.set('status', status)

    setLoading(true)
    api.getProblems(params)
      .then((response) => {
        if (!controller.signal.aborted) {
          setData(response)
          setOffline(false)
        }
      })
      .catch(() => {
        if (!controller.signal.aborted) {
          setData(fallbackPage)
          setOffline(true)
        }
      })
      .finally(() => {
        if (!controller.signal.aborted) setLoading(false)
      })

    return () => controller.abort()
  }, [debouncedQuery, difficulty, topic, status, page, pageSize, user])

  useEffect(() => {
    if (!user) {
      setSolvedIds(new Set())
      return
    }
    loadProgress()
  }, [user, loadProgress])

  const visibleRange = useMemo(() => {
    if (data.totalElements === 0) return '0'
    const start = data.number * data.size + 1
    const end = Math.min(start + data.content.length - 1, data.totalElements)
    return `${start}–${end}`
  }, [data])

  const changeFilter = (setter: (value: string) => void, value: string) => {
    setter(value)
    setPage(0)
  }

  const cycleStatus = () => {
    const next = statusOrder[(statusOrder.indexOf(status) + 1) % statusOrder.length]
    changeFilter(setStatus, next)
  }

  const performSolve = async (problemId: number, makeSolved: boolean) => {
    setUpdatingId(problemId)
    setSolvedIds((current) => {
      const next = new Set(current)
      if (makeSolved) next.add(problemId)
      else next.delete(problemId)
      return next
    })
    try {
      await api.setProgress(problemId, makeSolved ? 'SOLVED' : 'NOT_STARTED')
    } catch {
      setSolvedIds((current) => {
        const next = new Set(current)
        if (makeSolved) next.delete(problemId)
        else next.add(problemId)
        return next
      })
    } finally {
      setUpdatingId(null)
    }
  }

  const requestToggle = (problem: Problem) => {
    if (!user) {
      onLogin()
      return
    }
    if (solvedIds.has(problem.id)) {
      performSolve(problem.id, false)
    } else {
      setConfirmProblem(problem)
    }
  }

  return (
    <section className="questions-section" id="questions">
      <div className="shell">
        <div className="section-heading">
          <div>
            <span className="eyebrow">The question library</span>
            <h2>A focused path through DSA.</h2>
          </div>
          <p>Curated, categorized, and linked directly to LeetCode. Start anywhere; PrepPilot keeps the bigger picture visible.</p>
        </div>

        <LeetCodeCard user={user} onProgressChanged={loadProgress} />

        <div className="explorer-panel">
          <div className="explorer-topline">
            <div className="library-legend">
              <span><i className="legend-dot easy-dot" /> Easy</span>
              <span><i className="legend-dot medium-dot" /> Medium</span>
              <span><i className="legend-dot hard-dot" /> Hard</span>
            </div>
          </div>

          <div className="filter-bar">
            {user && (
              <button
                type="button"
                className={`status-toggle ${status ? 'is-active' : ''}`}
                onClick={cycleStatus}
                data-tip={statusMeta[status]}
                aria-label={statusMeta[status]}
              >
                {status === 'SOLVED' ? <CheckCircle2 size={17} /> : status === 'UNSOLVED' ? <CircleDashed size={17} /> : <ListFilter size={16} />}
              </button>
            )}
            <label className="search-field">
              <Search size={17} aria-hidden="true" />
              <span className="sr-only">Search questions</span>
              <input
                type="search"
                placeholder="Search a question or pattern…"
                value={query}
                onChange={(event) => changeFilter(setQuery, event.target.value)}
              />
              <kbd>⌘ K</kbd>
            </label>
            <Dropdown
              ariaLabel="Filter by difficulty"
              value={difficulty}
              onChange={(value) => changeFilter(setDifficulty, value)}
              options={difficultyOptions}
            />
            <Dropdown
              ariaLabel="Filter by topic"
              className="dropdown-topic"
              value={topic}
              onChange={(value) => changeFilter(setTopic, value)}
              options={[{ value: '', label: 'All patterns' }, ...topics.map((item) => ({ value: item, label: item }))]}
            />
          </div>

          {offline && (
            <div className="offline-note">Showing a preview set. Start the API to search all 145 questions and save progress.</div>
          )}

          <div className={`question-table ${loading ? 'is-loading' : ''}`} aria-live="polite">
            <div className="question-row question-head">
              <span>Status</span><span>Question</span><span>Pattern</span><span>Difficulty</span>
            </div>
            {loading ? (
              Array.from({ length: 6 }).map((_, index) => (
                <div className="question-row skeleton-row" key={index} aria-hidden="true">
                  <span /><span /><span /><span />
                </div>
              ))
            ) : data.content.length ? (
              data.content.map((problem, index) => {
                const solved = solvedIds.has(problem.id)
                const number = data.number * data.size + index + 1
                const openProblem = () => window.open(problem.url, '_blank', 'noopener,noreferrer')
                return (
                  <article
                    className="question-row"
                    key={problem.id}
                    role="link"
                    tabIndex={0}
                    onClick={openProblem}
                    onKeyDown={(event) => {
                      if (event.key === 'Enter') openProblem()
                    }}
                  >
                    <span className="status-cell" data-label="Status">
                      <button
                        type="button"
                        className={`solve-toggle ${solved ? 'is-solved' : ''}`}
                        onClick={(event) => {
                          event.stopPropagation()
                          requestToggle(problem)
                        }}
                        disabled={updatingId === problem.id}
                        data-tip={solved ? 'Mark not done' : 'Mark done'}
                        aria-label={solved ? `Mark ${problem.title} not done` : `Mark ${problem.title} done`}
                      >
                        {solved ? <Check size={14} /> : <Circle size={13} />}
                      </button>
                    </span>
                    <span className="question-name" data-label="Question">
                      <span className="problem-number">{number}.</span>
                      <span className="question-title">
                        {problem.title} <ExternalLink size={13} aria-hidden="true" />
                      </span>
                    </span>
                    <span className="topic-cell" data-label="Pattern">{problem.topic}</span>
                    <span data-label="Difficulty"><span className={`difficulty ${problem.difficulty.toLowerCase()}`}>{difficultyLabel[problem.difficulty]}</span></span>
                  </article>
                )
              })
            ) : (
              <div className="empty-state">
                <Search size={22} />
                <strong>No questions found</strong>
                <span>Try a broader search or clear a filter.</span>
              </div>
            )}
          </div>

          <div className="table-footer">
            <span>Showing {visibleRange} of {data.totalElements}</span>
            <div className="pagination">
              <button type="button" disabled={data.first || offline} onClick={() => setPage((current) => Math.max(0, current - 1))} aria-label="Previous page">
                <ArrowLeft size={15} />
              </button>
              <span>Page <strong>{data.totalPages ? data.number + 1 : 0}</strong> of {data.totalPages}</span>
              <button type="button" disabled={data.last || offline} onClick={() => setPage((current) => current + 1)} aria-label="Next page">
                <ArrowRight size={15} />
              </button>
            </div>
          </div>
        </div>
      </div>

      <ConfirmDialog
        open={confirmProblem !== null}
        tone="primary"
        title="Mark as done?"
        message={confirmProblem ? `Mark “${confirmProblem.title}” as solved? You can undo this anytime.` : ''}
        confirmLabel="Mark done"
        onConfirm={() => {
          if (confirmProblem) performSolve(confirmProblem.id, true)
          setConfirmProblem(null)
        }}
        onCancel={() => setConfirmProblem(null)}
      />
    </section>
  )
}
