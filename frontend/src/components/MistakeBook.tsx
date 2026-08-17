import { ArrowLeft, BookOpenCheck, Brain, CalendarClock, Check, ChevronRight, Loader2, LockKeyhole, RotateCcw } from 'lucide-react'
import { useCallback, useEffect, useMemo, useState } from 'react'
import { ApiError, api } from '../lib/api'
import type { CurrentUser, ReviewItem, ReviewRating, ReviewSummary } from '../types/api'
import { QuestionMarkdown } from './QuestionMarkdown'

interface MistakeBookProps {
  user: CurrentUser | null
  onLogin: () => void
}

const ratings: Array<{ value: ReviewRating; label: string; hint: string }> = [
  { value: 'FORGOT', label: 'Forgot', hint: 'Again tomorrow' },
  { value: 'DIFFICULT', label: 'Difficult', hint: 'Short interval' },
  { value: 'REMEMBERED', label: 'Remembered', hint: 'Normal interval' },
  { value: 'EASY', label: 'Easy', hint: 'Long interval' },
]

function formatDue(date: string, due: boolean) {
  if (due) return 'Due now'
  return `Next ${new Intl.DateTimeFormat('en', { month: 'short', day: 'numeric' }).format(new Date(date))}`
}

export function MistakeBook({ user, onLogin }: MistakeBookProps) {
  const [summary, setSummary] = useState<ReviewSummary | null>(null)
  const [items, setItems] = useState<ReviewItem[]>([])
  const [dueOnly, setDueOnly] = useState(true)
  const [revealed, setRevealed] = useState<Set<string>>(new Set())
  const [ratingId, setRatingId] = useState<string | null>(null)
  const [loading, setLoading] = useState(Boolean(user))
  const [error, setError] = useState<string | null>(null)

  const load = useCallback(async () => {
    if (!user) return
    setLoading(true)
    setError(null)
    try {
      const [nextSummary, nextItems] = await Promise.all([
        api.getReviewSummary(),
        api.getReviews(dueOnly),
      ])
      setSummary(nextSummary)
      setItems(nextItems)
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not load your Mistake Book.')
    } finally {
      setLoading(false)
    }
  }, [dueOnly, user])

  useEffect(() => {
    void load()
  }, [load])

  const dueProgress = useMemo(() => {
    if (!summary?.total) return 0
    return Math.round((summary.mastered / summary.total) * 100)
  }, [summary])

  const reveal = (id: string) => setRevealed((current) => new Set(current).add(id))

  const rate = async (item: ReviewItem, rating: ReviewRating) => {
    setRatingId(item.id)
    setError(null)
    try {
      const updated = await api.rateReview(item.id, rating)
      setItems((current) => dueOnly ? current.filter((entry) => entry.id !== item.id) : current.map((entry) => entry.id === item.id ? updated : entry))
      setSummary((current) => current && ({
        ...current,
        due: Math.max(0, current.due - (item.due ? 1 : 0)),
        mastered: current.mastered + (!item.mastered && updated.mastered ? 1 : 0),
      }))
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not schedule the next review.')
    } finally {
      setRatingId(null)
    }
  }

  if (!user) {
    return (
      <main className="mistake-page mistake-locked">
        <div className="shell mistake-locked-card">
          <span className="mistake-icon"><LockKeyhole size={28} /></span>
          <span className="eyebrow">Private revision</span>
          <h1>Your mistakes should work for you.</h1>
          <p>Sign in to collect missed knowledge-check questions and revisit them on a spaced schedule.</p>
          <button type="button" className="button" onClick={onLogin}>Sign in to open Mistake Book</button>
        </div>
      </main>
    )
  }

  return (
    <main className="mistake-page">
      <div className="shell mistake-shell">
        <a className="mistake-back" href="#practice"><ArrowLeft size={15} /> Knowledge checks</a>
        <header className="mistake-hero">
          <div>
            <span className="eyebrow">Scheduled revision</span>
            <h1>Mistake Book</h1>
            <p>Missed and unanswered questions return at useful intervals. Reveal the answer, rate your recall, and let the schedule adapt.</p>
          </div>
          <div className="mistake-stats">
            <span><strong>{summary?.due ?? 0}</strong><small>due now</small></span>
            <span><strong>{summary?.total ?? 0}</strong><small>saved gaps</small></span>
            <span><strong>{dueProgress}%</strong><small>mastered</small></span>
          </div>
        </header>

        <div className="mistake-toolbar">
          <div role="group" aria-label="Review filter">
            <button type="button" className={dueOnly ? 'is-active' : ''} onClick={() => setDueOnly(true)}>Due now</button>
            <button type="button" className={!dueOnly ? 'is-active' : ''} onClick={() => setDueOnly(false)}>All mistakes</button>
          </div>
          <span><CalendarClock size={15} /> Simple 1 · 3 · 7 · 14 · 30 day rhythm</span>
        </div>

        {error && <div className="assessment-error" role="alert">{error}</div>}
        {loading ? (
          <div className="mistake-empty"><Loader2 className="lc-spin" size={24} /> Loading your review queue…</div>
        ) : items.length === 0 ? (
          <section className="mistake-empty">
            <span><Check size={24} /></span>
            <h2>{dueOnly ? 'You are caught up.' : 'No mistakes saved yet.'}</h2>
            <p>{dueOnly ? 'Complete a knowledge check or come back when the next review is due.' : 'Incorrect and unanswered knowledge-check questions will appear here automatically.'}</p>
            <a href="#practice">Go to Knowledge Checks <ChevronRight size={15} /></a>
          </section>
        ) : (
          <section className="mistake-list" aria-label="Mistake review queue">
            {items.map((item, index) => {
              const isRevealed = revealed.has(item.id)
              return (
                <article className={`mistake-card ${isRevealed ? 'is-revealed' : ''}`} key={item.id}>
                  <header>
                    <span>{String(index + 1).padStart(2, '0')}</span>
                    <div><small>{item.topicName}</small><strong>{formatDue(item.dueAt, item.due)}</strong></div>
                    {item.mastered && <em><Brain size={13} /> Mastered</em>}
                  </header>
                  <div className="mistake-prompt"><QuestionMarkdown>{item.prompt}</QuestionMarkdown></div>
                  <div className="mistake-options">
                    {item.options.map((option, optionIndex) => (
                      <div
                        key={optionIndex}
                        className={`${isRevealed && optionIndex === item.correctOption ? 'is-correct' : ''} ${isRevealed && optionIndex === item.originalAnswer && optionIndex !== item.correctOption ? 'is-original-wrong' : ''}`}
                      >
                        <span>{String.fromCharCode(65 + optionIndex)}</span>
                        <div><QuestionMarkdown>{option}</QuestionMarkdown></div>
                        {isRevealed && optionIndex === item.correctOption && <strong><Check size={12} /> Correct</strong>}
                        {isRevealed && optionIndex === item.originalAnswer && optionIndex !== item.correctOption && <strong>Your answer</strong>}
                      </div>
                    ))}
                  </div>

                  {!isRevealed ? (
                    <button type="button" className="mistake-reveal" onClick={() => reveal(item.id)}>
                      <BookOpenCheck size={16} /> Reveal answer and explanation
                    </button>
                  ) : (
                    <div className="mistake-review-controls">
                      <div className="mistake-explanation"><BookOpenCheck size={17} /><p><strong>Why this is correct</strong>{item.justification}</p></div>
                      <div className="mistake-rating">
                        <span>How well did you remember it?</span>
                        <div>
                          {ratings.map((rating) => (
                            <button type="button" key={rating.value} disabled={ratingId === item.id} onClick={() => void rate(item, rating.value)}>
                              {ratingId === item.id ? <Loader2 className="lc-spin" size={13} /> : <RotateCcw size={13} />}
                              <strong>{rating.label}</strong><small>{rating.hint}</small>
                            </button>
                          ))}
                        </div>
                      </div>
                    </div>
                  )}
                </article>
              )
            })}
          </section>
        )}
      </div>
    </main>
  )
}
