import {
  ArrowLeft,
  Atom,
  BookOpenCheck,
  Boxes,
  Braces,
  Check,
  ChevronLeft,
  ChevronRight,
  Clock3,
  Code2,
  Coffee,
  Cpu,
  Database,
  History,
  Layers3,
  Loader2,
  LockKeyhole,
  Network,
  Play,
  ShieldCheck,
  Sparkles,
  Trophy,
  X,
} from 'lucide-react'
import { useCallback, useEffect, useRef, useState } from 'react'
import { ApiError, api } from '../lib/api'
import type {
  AssessmentAttempt,
  AssessmentAttemptSummary,
  AssessmentQuestion,
  AssessmentTopic,
  CurrentUser,
} from '../types/api'
import { ConfirmDialog } from './ConfirmDialog'
import { QuestionMarkdown } from './QuestionMarkdown'

interface PracticeTestsProps {
  user: CurrentUser | null
  onLogin: () => void
}

const topicVisuals = {
  oops: { short: 'OOP', icon: Boxes, accent: 'green' },
  java: { short: 'Java', icon: Coffee, accent: 'green' },
  'spring-boot': { short: 'Spring', icon: Layers3, accent: 'green' },
  react: { short: 'React', icon: Atom, accent: 'green' },
  cpp: { short: 'C++', icon: Braces, accent: 'green' },
  python: { short: 'Python', icon: Code2, accent: 'green' },
  'computer-networks': { short: 'Networks', icon: Network, accent: 'green' },
  mysql: { short: 'MySQL', icon: Database, accent: 'green' },
  'operating-systems': { short: 'OS', icon: Cpu, accent: 'green' },
} as const

const signedOutTopics = [
  ['oops', 'Object-Oriented Programming'],
  ['java', 'Java'],
  ['spring-boot', 'Spring Boot'],
  ['react', 'React'],
  ['cpp', 'C++'],
  ['python', 'Python'],
  ['computer-networks', 'Computer Networks'],
  ['mysql', 'MySQL'],
  ['operating-systems', 'Operating Systems'],
] as const

function formatTimer(seconds: number) {
  const minutes = Math.floor(seconds / 60)
  const remainder = seconds % 60
  return `${String(minutes).padStart(2, '0')}:${String(remainder).padStart(2, '0')}`
}

function formatAttemptDate(value: string | null) {
  if (!value) return 'In progress'
  return new Intl.DateTimeFormat(undefined, {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
  }).format(new Date(value))
}

function difficultyLabel(value: AssessmentQuestion['difficulty']) {
  return value.charAt(0) + value.slice(1).toLowerCase()
}

export function PracticeTests({ user, onLogin }: PracticeTestsProps) {
  const [topics, setTopics] = useState<AssessmentTopic[]>([])
  const [attempt, setAttempt] = useState<AssessmentAttempt | null>(null)
  const [currentIndex, setCurrentIndex] = useState(0)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [startTopic, setStartTopic] = useState<AssessmentTopic | null>(null)
  const [starting, setStarting] = useState(false)
  const [submitOpen, setSubmitOpen] = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [savingQuestionId, setSavingQuestionId] = useState<string | null>(null)
  const [remainingSeconds, setRemainingSeconds] = useState(0)
  const [historyTopic, setHistoryTopic] = useState<AssessmentTopic | null>(null)
  const [history, setHistory] = useState<AssessmentAttemptSummary[]>([])
  const [historyLoading, setHistoryLoading] = useState(false)
  const autoSubmittingRef = useRef<string | null>(null)

  const refreshTopics = useCallback(async () => {
    if (!user) return
    setLoading(true)
    try {
      setTopics(await api.getAssessmentTopics())
      setError(null)
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not load knowledge checks.')
    } finally {
      setLoading(false)
    }
  }, [user])

  useEffect(() => {
    window.scrollTo({ top: 0 })
    setAttempt(null)
    setHistoryTopic(null)
    setHistory([])
    if (user) void refreshTopics()
  }, [user, refreshTopics])

  useEffect(() => {
    if (!attempt || attempt.status !== 'IN_PROGRESS') return
    const onBeforeUnload = (event: BeforeUnloadEvent) => event.preventDefault()
    window.addEventListener('beforeunload', onBeforeUnload)
    return () => window.removeEventListener('beforeunload', onBeforeUnload)
  }, [attempt])

  useEffect(() => {
    if (!attempt || attempt.status !== 'IN_PROGRESS') return

    const tick = () => {
      const seconds = Math.max(0, Math.ceil((new Date(attempt.expiresAt).getTime() - Date.now()) / 1000))
      setRemainingSeconds(seconds)
      if (seconds === 0 && autoSubmittingRef.current !== attempt.id) {
        autoSubmittingRef.current = attempt.id
        setSubmitting(true)
        api.submitAssessment(attempt.id)
          .then((result) => {
            setAttempt(result)
            window.scrollTo({ top: 0 })
            return refreshTopics()
          })
          .catch(() => setError('Time is up, but we could not refresh your result. Please reload.'))
          .finally(() => setSubmitting(false))
      }
    }

    tick()
    const interval = window.setInterval(tick, 1000)
    return () => window.clearInterval(interval)
  }, [attempt, refreshTopics])

  const openAttempt = async (attemptId: string) => {
    setLoading(true)
    setError(null)
    try {
      const result = await api.getAssessmentAttempt(attemptId)
      setAttempt(result)
      const firstUnanswered = result.questions.findIndex((question) => question.selectedOption === null)
      setCurrentIndex(firstUnanswered >= 0 ? firstUnanswered : 0)
      window.scrollTo({ top: 0 })
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not open that knowledge check.')
    } finally {
      setLoading(false)
    }
  }

  const beginTest = async (topic: AssessmentTopic) => {
    setStarting(true)
    setError(null)
    try {
      const result = await api.startAssessment(topic.slug)
      setAttempt(result)
      setCurrentIndex(result.questions.findIndex((question) => question.selectedOption === null))
      setStartTopic(null)
      window.scrollTo({ top: 0 })
      await refreshTopics()
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not start this knowledge check.')
      setStartTopic(null)
    } finally {
      setStarting(false)
    }
  }

  const showHistory = async (topic: AssessmentTopic) => {
    setHistoryTopic(topic)
    setHistoryLoading(true)
    setError(null)
    try {
      setHistory(await api.getAssessmentHistory(topic.slug))
      window.setTimeout(() => document.querySelector('.assessment-history')?.scrollIntoView({ behavior: 'smooth' }), 0)
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not load your check history.')
    } finally {
      setHistoryLoading(false)
    }
  }

  const saveAnswer = async (question: AssessmentQuestion, selectedOption: number) => {
    if (!attempt || attempt.status !== 'IN_PROGRESS' || savingQuestionId) return
    const previousOption = question.selectedOption
    const previouslyAnswered = previousOption !== null
    setSavingQuestionId(question.id)
    setError(null)
    setAttempt((current) => current && ({
      ...current,
      answeredQuestions: current.answeredQuestions + (previouslyAnswered ? 0 : 1),
      questions: current.questions.map((item) =>
        item.id === question.id ? { ...item, selectedOption } : item),
    }))

    try {
      const saved = await api.saveAssessmentAnswer(attempt.id, question.id, selectedOption)
      setAttempt((current) => current && ({ ...current, answeredQuestions: saved.answeredQuestions }))
    } catch (caught) {
      if (caught instanceof ApiError && caught.status === 409) {
        await openAttempt(attempt.id)
      } else {
        setAttempt((current) => current && ({
          ...current,
          answeredQuestions: current.answeredQuestions - (previouslyAnswered ? 0 : 1),
          questions: current.questions.map((item) =>
            item.id === question.id ? { ...item, selectedOption: previousOption } : item),
        }))
        setError(caught instanceof ApiError ? caught.message : 'Could not save your answer.')
      }
    } finally {
      setSavingQuestionId(null)
    }
  }

  const submitTest = async () => {
    if (!attempt) return
    setSubmitting(true)
    setError(null)
    try {
      const result = await api.submitAssessment(attempt.id)
      setAttempt(result)
      setSubmitOpen(false)
      window.scrollTo({ top: 0 })
      await refreshTopics()
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not submit your knowledge check.')
    } finally {
      setSubmitting(false)
    }
  }

  if (!user) {
    return (
      <main className="assessment-page assessment-locked-page">
        <div className="shell assessment-locked-shell">
          <section className="assessment-locked-copy">
            <span className="eyebrow">Interview readiness</span>
            <div className="assessment-lock-icon"><LockKeyhole size={26} /></div>
            <h1>Practice under pressure.<br /><em>Learn without judgment.</em></h1>
            <p>
              Sign in to start timed knowledge checks and keep a private record of every answer,
              score, and explanation.
            </p>
            <button type="button" className="button" onClick={onLogin}>Sign in to check your knowledge</button>
          </section>
          <section className="assessment-locked-preview" aria-label="Available knowledge-check topics">
            <div className="locked-preview-head">
              <ShieldCheck size={18} />
              <div><strong>Your progress stays private</strong><span>Nine focused interview tracks</span></div>
            </div>
            <div className="locked-topic-list">
              {signedOutTopics.map(([slug, name]) => {
                const visual = topicVisuals[slug]
                const Icon = visual.icon
                return (
                  <div key={slug}>
                    <span className={`assessment-topic-icon ${visual.accent}`}><Icon size={17} /></span>
                    <strong>{name}</strong>
                    <small>100 questions</small>
                    <LockKeyhole size={13} />
                  </div>
                )
              })}
            </div>
          </section>
        </div>
      </main>
    )
  }

  if (attempt) {
    return attempt.status === 'IN_PROGRESS' ? (
      <TestRunner
        attempt={attempt}
        currentIndex={Math.max(currentIndex, 0)}
        remainingSeconds={remainingSeconds}
        savingQuestionId={savingQuestionId}
        submitting={submitting}
        error={error}
        onSelectQuestion={setCurrentIndex}
        onAnswer={saveAnswer}
        onSubmit={() => setSubmitOpen(true)}
        onExit={() => setAttempt(null)}
      >
        <ConfirmDialog
          open={submitOpen}
          title="Submit this knowledge check?"
          message={`${attempt.answeredQuestions} of ${attempt.totalQuestions} questions are answered. Unanswered questions will be marked incorrect.`}
          confirmLabel="Submit check"
          tone="primary"
          busy={submitting}
          onConfirm={submitTest}
          onCancel={() => setSubmitOpen(false)}
        />
      </TestRunner>
    ) : (
      <TestResult attempt={attempt} onBack={() => {
        setAttempt(null)
        void refreshTopics()
      }} />
    )
  }

  return (
    <main className="assessment-page">
      <div className="shell assessment-shell">
        <header className="assessment-hero">
          <div>
            <span className="eyebrow">Knowledge checks</span>
            <h1>Check what you know.<br /><em>Review what you miss.</em></h1>
            <p>Twenty focused questions. A fair 10–15 minute limit. Explanations that make every mistake useful.</p>
          </div>
          <div className="assessment-rules" aria-label="Test format">
            <span><strong>20</strong><small>questions</small></span>
            <span><strong>10–15</strong><small>minutes by topic</small></span>
            <span><strong>5</strong><small>unique checks</small></span>
          </div>
        </header>

        <section className="assessment-honesty-note">
          <ShieldCheck size={20} />
          <div>
            <strong>This space works best when you keep it honest.</strong>
            <p>These scores are only for you. Skip outside help during a check—the review is where learning happens.</p>
          </div>
        </section>

        {error && <div className="assessment-error" role="alert">{error}</div>}

        {loading && topics.length === 0 ? (
          <div className="assessment-loading"><Loader2 className="lc-spin" size={24} /> Loading your knowledge checks…</div>
        ) : (
          <>
            <div className="knowledge-check-actions">
              <a href="#mistakes"><BookOpenCheck size={16} /> Open Mistake Book</a>
              <span>Incorrect and unanswered questions are scheduled automatically.</span>
            </div>

            <section className="assessment-topic-grid" aria-label="Knowledge-check topics">
              {topics.map((topic) => {
              const visual = topicVisuals[topic.slug as keyof typeof topicVisuals] ?? topicVisuals.java
              const Icon = visual.icon
              const complete = topic.attemptsUsed >= topic.maxAttempts && !topic.activeAttemptId
              return (
                <article className="assessment-topic-card" key={topic.slug}>
                  <div className="topic-card-top">
                    <span className={`assessment-topic-icon ${visual.accent}`}><Icon size={21} /></span>
                    <span className="topic-question-count">{topic.totalQuestions} questions · {topic.durationMinutes} min</span>
                  </div>
                  <h2>{topic.name}</h2>
                  <p>Balanced across easy, medium, and hard interview questions.</p>
                  <div className="topic-attempt-dots" aria-label={`${topic.attemptsUsed} of ${topic.maxAttempts} checks used`}>
                    {Array.from({ length: topic.maxAttempts }, (_, index) => (
                      <i key={index} className={index < topic.attemptsUsed ? 'is-used' : ''} />
                    ))}
                    <span>{topic.attemptsUsed}/{topic.maxAttempts} checks</span>
                  </div>
                  <div className="topic-card-footer">
                    <span className="topic-best-score">
                      <Trophy size={14} />
                      {topic.bestScore === null ? 'No score yet' : `Best ${topic.bestScore}/20`}
                    </span>
                    <div>
                      {topic.attemptsUsed > 0 && (
                        <button type="button" className="assessment-link-button" onClick={() => void showHistory(topic)}>
                          <History size={14} /> History
                        </button>
                      )}
                      {!complete && (
                        <button
                          type="button"
                          className="assessment-start-button"
                          onClick={() => topic.activeAttemptId ? void beginTest(topic) : setStartTopic(topic)}
                        >
                          {topic.activeAttemptId ? 'Resume' : 'Start'} <Play size={13} fill="currentColor" />
                        </button>
                      )}
                    </div>
                  </div>
                </article>
              )
              })}
            </section>
          </>
        )}

        {historyTopic && (
          <section className="assessment-history">
            <header>
              <div><span className="eyebrow">Your record</span><h2>{historyTopic.name} check history</h2></div>
              <button type="button" aria-label="Close history" onClick={() => setHistoryTopic(null)}><X size={17} /></button>
            </header>
            {historyLoading ? (
              <div className="assessment-loading"><Loader2 className="lc-spin" size={20} /> Loading history…</div>
            ) : history.length === 0 ? (
              <p className="history-empty">Your completed checks will appear here.</p>
            ) : (
              <div className="history-list">
                {history.map((item) => (
                  <button type="button" key={item.id} onClick={() => void openAttempt(item.id)}>
                    <span className={`history-score ${item.status === 'IN_PROGRESS' ? 'is-active' : ''}`}>
                      {item.score === null ? <Clock3 size={18} /> : <><strong>{item.score}</strong><small>/20</small></>}
                    </span>
                    <span><strong>Check {item.attemptNumber}</strong><small>{formatAttemptDate(item.submittedAt ?? item.startedAt)}</small></span>
                    <span className="history-status">{item.status === 'IN_PROGRESS' ? 'Resume' : 'Review'} <ChevronRight size={15} /></span>
                  </button>
                ))}
              </div>
            )}
          </section>
        )}
      </div>

      <ConfirmDialog
        open={Boolean(startTopic)}
        title={`Start ${startTopic?.name ?? ''} knowledge check?`}
        message={`Please do not cheat—this check is for your own growth. You will have ${startTopic?.durationMinutes ?? 10} minutes for 20 questions. The timer cannot be paused, but every selected answer is saved automatically.`}
        confirmLabel="I’ll keep it honest"
        tone="primary"
        busy={starting}
        onConfirm={() => startTopic && void beginTest(startTopic)}
        onCancel={() => setStartTopic(null)}
      />
    </main>
  )
}

interface TestRunnerProps {
  attempt: AssessmentAttempt
  currentIndex: number
  remainingSeconds: number
  savingQuestionId: string | null
  submitting: boolean
  error: string | null
  onSelectQuestion: (index: number) => void
  onAnswer: (question: AssessmentQuestion, option: number) => void
  onSubmit: () => void
  onExit: () => void
  children: React.ReactNode
}

function TestRunner({
  attempt,
  currentIndex,
  remainingSeconds,
  savingQuestionId,
  submitting,
  error,
  onSelectQuestion,
  onAnswer,
  onSubmit,
  onExit,
  children,
}: TestRunnerProps) {
  const question = attempt.questions[currentIndex]
  const urgent = remainingSeconds <= 120

  return (
    <main className="assessment-test-page">
      <div className="assessment-test-bar">
        <div className="shell">
          <button type="button" className="test-exit" onClick={onExit}>
            <ArrowLeft size={16} />
            <span className="test-exit-full">Save & exit</span>
            <span className="test-exit-short">Exit</span>
          </button>
          <div className="test-bar-title"><strong>{attempt.topicName}</strong><span>Check {attempt.attemptNumber} · Question {currentIndex + 1}/20</span></div>
          <div className={`test-timer ${urgent ? 'is-urgent' : ''}`} aria-live="polite">
            <Clock3 size={17} /> <strong>{formatTimer(remainingSeconds)}</strong>
          </div>
          <button type="button" className="test-submit" onClick={onSubmit} disabled={submitting}>Submit check</button>
        </div>
      </div>

      <div className="shell assessment-test-layout">
        <aside className="question-navigator" aria-label="Question navigator">
          <div><strong>Questions</strong><span>{attempt.answeredQuestions}/20 answered</span></div>
          <nav>
            {attempt.questions.map((item, index) => (
              <button
                key={item.id}
                type="button"
                className={`${index === currentIndex ? 'is-current' : ''} ${item.selectedOption !== null ? 'is-answered' : ''}`}
                aria-label={`Question ${index + 1}${item.selectedOption !== null ? ', answered' : ''}`}
                aria-current={index === currentIndex ? 'step' : undefined}
                onClick={() => onSelectQuestion(index)}
              >
                {index + 1}
              </button>
            ))}
          </nav>
          <div className="question-legend"><span><i /> Unanswered</span><span><i className="answered" /> Answered</span></div>
        </aside>

        <section className="assessment-question-panel">
          {error && <div className="assessment-error" role="alert">{error}</div>}
          <div className="question-kicker">
            <span>Question {currentIndex + 1}</span>
          </div>
          <div className="assessment-question-copy"><QuestionMarkdown>{question.prompt}</QuestionMarkdown></div>
          <div className="assessment-options" role="radiogroup" aria-label={`Answers for question ${currentIndex + 1}`}>
            {question.options.map((option, index) => (
              <button
                type="button"
                role="radio"
                aria-checked={question.selectedOption === index}
                className={question.selectedOption === index ? 'is-selected' : ''}
                key={index}
                disabled={savingQuestionId === question.id || submitting}
                onClick={() => onAnswer(question, index)}
              >
                <span className="option-letter">{String.fromCharCode(65 + index)}</span>
                <span className="option-copy"><QuestionMarkdown>{option}</QuestionMarkdown></span>
                {question.selectedOption === index && <Check className="option-check" size={17} />}
              </button>
            ))}
          </div>
          <div className="question-panel-footer">
            <span>{savingQuestionId === question.id ? <><Loader2 className="lc-spin" size={13} /> Saving…</> : 'Answers save automatically'}</span>
            <div>
              <button type="button" onClick={() => onSelectQuestion(Math.max(0, currentIndex - 1))} disabled={currentIndex === 0}>
                <ChevronLeft size={16} /> Previous
              </button>
              {currentIndex < attempt.questions.length - 1 ? (
                <button type="button" className="is-primary" onClick={() => onSelectQuestion(currentIndex + 1)}>
                  Next <ChevronRight size={16} />
                </button>
              ) : (
                <button type="button" className="is-primary" onClick={onSubmit}>Review & submit</button>
              )}
            </div>
          </div>
        </section>
      </div>
      {children}
    </main>
  )
}

function TestResult({ attempt, onBack }: { attempt: AssessmentAttempt; onBack: () => void }) {
  const score = attempt.score ?? 0
  const correct = attempt.questions.filter((question) => question.correct).length
  const unanswered = attempt.questions.filter((question) => question.selectedOption === null).length
  const incorrect = attempt.totalQuestions - correct - unanswered
  const percentage = Math.round((score / attempt.totalQuestions) * 100)

  return (
    <main className="assessment-result-page">
      <div className="shell assessment-result-shell">
        <button type="button" className="result-back" onClick={onBack}><ArrowLeft size={16} /> All knowledge checks</button>
        <section className="result-hero">
          <div className="result-score-orbit" style={{ '--score': `${percentage * 3.6}deg` } as React.CSSProperties}>
            <div><strong>{score}</strong><span>/20</span><small>{percentage}%</small></div>
          </div>
          <div>
            <span className="eyebrow">Check {attempt.attemptNumber} complete</span>
            <h1>{score >= 16 ? 'Strong work.' : score >= 11 ? 'A useful baseline.' : 'Now you know what to review.'}</h1>
            <p>{attempt.topicName} · {attempt.status === 'TIMED_OUT' ? 'Time expired and saved answers were submitted.' : 'Submitted successfully.'}</p>
            <div className="result-breakdown">
              <span className="correct"><Check size={14} /> {correct} correct</span>
              <span className="incorrect"><X size={14} /> {incorrect} incorrect</span>
              <span><Clock3 size={14} /> {unanswered} unanswered</span>
            </div>
          </div>
          <div className="result-note"><Sparkles size={18} /><p><strong>Review beats repetition.</strong> Read every explanation, especially when you guessed correctly.</p></div>
        </section>

        <section className="result-review">
          <header><span className="eyebrow">Answer review</span><h2>Every question, explained</h2></header>
          <div className="review-question-list">
            {attempt.questions.map((question) => (
              <article className={`review-question ${question.correct ? 'is-correct' : 'is-incorrect'}`} key={question.id}>
                <div className="review-question-head">
                  <span className="review-result-icon">{question.correct ? <Check size={16} /> : <X size={16} />}</span>
                  <span>Question {question.position}</span>
                  <span className={`difficulty ${question.difficulty.toLowerCase()}`}>{difficultyLabel(question.difficulty)}</span>
                </div>
                <div className="review-prompt"><QuestionMarkdown>{question.prompt}</QuestionMarkdown></div>
                <div className="review-options">
                  {question.options.map((option, index) => {
                    const isCorrectOption = index === question.correctOption
                    const isWrongSelection = index === question.selectedOption && !isCorrectOption
                    return (
                      <div className={`${isCorrectOption ? 'is-correct-option' : ''} ${isWrongSelection ? 'is-wrong-option' : ''}`} key={index}>
                        <span className="option-letter">{String.fromCharCode(65 + index)}</span>
                        <span className="option-copy"><QuestionMarkdown>{option}</QuestionMarkdown></span>
                        {isCorrectOption && <span className="option-result-label"><Check size={13} /> Correct</span>}
                        {isWrongSelection && <span className="option-result-label"><X size={13} /> Your answer</span>}
                      </div>
                    )
                  })}
                </div>
                {question.selectedOption === null && <p className="review-unanswered">You did not answer this question.</p>}
                <div className="review-explanation">
                  <BookOpenCheck size={17} />
                  <div><strong>Why this is correct</strong><p>{question.justification}</p></div>
                </div>
              </article>
            ))}
          </div>
        </section>
      </div>
    </main>
  )
}
