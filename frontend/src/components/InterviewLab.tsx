import { AlertCircle, ArrowLeft, Braces, Check, CheckCircle2, ChevronRight, Clock3, Code2, FileCheck2, History, Loader2, LockKeyhole, Play, RotateCcw, Save, ServerCog, ShieldCheck, Sparkles, Target, TerminalSquare, X, XCircle } from 'lucide-react'
import { useCallback, useEffect, useMemo, useState } from 'react'
import { ApiError, api } from '../lib/api'
import type { CodingExecution, CodingInterviewSession, CodingInterviewSubmission, CodingInterviewSummary, CodingLanguage, CodingRunnerStatus, CurrentUser } from '../types/api'
import { QuestionMarkdown } from './QuestionMarkdown'
import { CodeEditor } from './CodeEditor'

interface InterviewLabProps {
  user: CurrentUser | null
  onLogin: () => void
}

const languages: Array<{ value: CodingLanguage; label: string }> = [
  { value: 'JAVA', label: 'Java' },
  { value: 'PYTHON', label: 'Python' },
  { value: 'CPP', label: 'C++' },
]

const rubricFields: Array<{ key: keyof Pick<CodingInterviewSubmission, 'clarification' | 'approach' | 'correctness'>; label: string; hint: string }> = [
  { key: 'clarification', label: 'Clarification', hint: 'I identified assumptions, inputs and edge conditions.' },
  { key: 'approach', label: 'Approach', hint: 'I chose and justified an algorithm that fits the constraints.' },
  { key: 'correctness', label: 'Correctness', hint: 'My implementation handles the required and boundary cases.' },
]

const terminalStatuses = new Set(['PASSED', 'FAILED', 'ERROR'])

function formatTimer(seconds: number) {
  const safe = Math.max(0, seconds)
  return `${String(Math.floor(safe / 60)).padStart(2, '0')}:${String(safe % 60).padStart(2, '0')}`
}

function formatDate(value: string) {
  return new Intl.DateTimeFormat('en', { month: 'short', day: 'numeric', year: 'numeric', hour: 'numeric', minute: '2-digit' }).format(new Date(value))
}

function friendlyLanguage(language: CodingLanguage) {
  return languages.find((item) => item.value === language)?.label ?? language
}

export function InterviewLab({ user, onLogin }: InterviewLabProps) {
  const [language, setLanguage] = useState<CodingLanguage>('JAVA')
  const [history, setHistory] = useState<CodingInterviewSummary[]>([])
  const [session, setSession] = useState<CodingInterviewSession | null>(null)
  const [loading, setLoading] = useState(Boolean(user))
  const [starting, setStarting] = useState(false)
  const [startConfirmationOpen, setStartConfirmationOpen] = useState(false)
  const [startAcknowledged, setStartAcknowledged] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const loadHistory = useCallback(async () => {
    if (!user) return
    setLoading(true)
    setError(null)
    try {
      setHistory(await api.getCodingInterviewHistory())
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not load coding interview history.')
    } finally {
      setLoading(false)
    }
  }, [user])

  useEffect(() => { void loadHistory() }, [loadHistory])

  useEffect(() => {
    if (!startConfirmationOpen) return

    const previousOverflow = document.body.style.overflow
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape' && !starting) {
        setStartConfirmationOpen(false)
        setStartAcknowledged(false)
      }
    }

    document.body.style.overflow = 'hidden'
    window.addEventListener('keydown', handleKeyDown)
    return () => {
      document.body.style.overflow = previousOverflow
      window.removeEventListener('keydown', handleKeyDown)
    }
  }, [startConfirmationOpen, starting])

  const start = async () => {
    if (!startAcknowledged || starting) return

    setStarting(true)
    setError(null)
    try {
      const createdSession = await api.startCodingInterview(language)
      setStartConfirmationOpen(false)
      setStartAcknowledged(false)
      setSession(createdSession)
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not start a coding interview.')
    } finally {
      setStarting(false)
    }
  }

  const requestStart = () => {
    setError(null)
    setStartAcknowledged(false)
    setStartConfirmationOpen(true)
  }

  const cancelStart = () => {
    if (starting) return
    setStartConfirmationOpen(false)
    setStartAcknowledged(false)
  }

  const open = async (id: string) => {
    setLoading(true)
    setError(null)
    try {
      setSession(await api.getCodingInterview(id))
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not open this coding interview.')
    } finally {
      setLoading(false)
    }
  }

  if (!user) {
    return (
      <main className="lab-page lab-locked">
        <div className="shell lab-locked-layout">
          <section>
            <span className="eyebrow">Interview Lab</span>
            <h1>Practise a real coding round, end to end.</h1>
            <p>Get a detailed interview problem, a language-specific executable starter, visible and hidden tests, a timer, and a private review.</p>
            <button type="button" className="button" onClick={onLogin}>Sign in to enter the lab</button>
          </section>
          <div className="lab-locked-card">
            <span><LockKeyhole size={22} /></span>
            <ol><li><strong>01</strong>Understand and plan</li><li><strong>02</strong>Code and run tests</li><li><strong>03</strong>Review and improve</li></ol>
            <small><ShieldCheck size={14} /> Your source and results stay private to your account.</small>
          </div>
        </div>
      </main>
    )
  }

  if (session) {
    const close = () => { setSession(null); void loadHistory() }
    return session.status === 'IN_PROGRESS'
      ? <CodingRunner session={session} onSession={setSession} onExit={close} />
      : <CodingReview session={session} onBack={close} />
  }

  return (
    <main className="lab-page">
      <div className="shell lab-shell">
        <header className="lab-hero">
          <div>
            <span className="eyebrow">Interview Lab · Coding</span>
            <h1>One focused round.<br /><em>Real code. Verified score.</em></h1>
            <p>A random problem from 20 curated interview patterns, complete with constraints, examples, executable boilerplate, hidden tests and saved results.</p>
          </div>
          <div className="lab-format">
            <span><Clock3 size={19} /><strong>45 minutes</strong><small>server timed</small></span>
            <span><Target size={19} /><strong>20 prompts</strong><small>unused first</small></span>
            <span><FileCheck2 size={19} /><strong>Verified scoring</strong><small>hidden tests included</small></span>
          </div>
        </header>

        {error && <div className="assessment-error" role="alert">{error}</div>}

        <section className="lab-start-card">
          <div>
            <span className="lab-start-icon"><Code2 size={23} /></span>
            <div><small>Start a coding interview</small><h2>Choose your strongest language.</h2><p>Your selected language opens with problem-specific, runnable boilerplate.</p></div>
          </div>
          <div className="lab-language-picker" role="radiogroup" aria-label="Coding language">
            {languages.map((item) => (
              <button type="button" role="radio" aria-checked={language === item.value} className={language === item.value ? 'is-active' : ''} key={item.value} onClick={() => setLanguage(item.value)}>
                {language === item.value && <Check size={13} />}{item.label}
              </button>
            ))}
          </div>
          <button type="button" className="lab-start-button" onClick={requestStart}>
            <Play size={15} fill="currentColor" /> Begin focused round
          </button>
        </section>

        <section className="lab-history">
          <header><div><span className="eyebrow">Saved sessions</span><h2>Your coding interview history</h2></div><History size={20} /></header>
          {loading ? <div className="lab-history-empty"><Loader2 className="lc-spin" size={20} /> Loading sessions…</div>
            : history.length === 0 ? <div className="lab-history-empty"><Braces size={22} /><p>Your rounds and verified test scores will appear here.</p></div>
              : <div className="lab-history-list">{history.map((item) => (
                <button type="button" key={item.id} onClick={() => void open(item.id)}>
                  <span className={`lab-history-status ${item.status.toLowerCase()}`} aria-label={item.status === 'IN_PROGRESS' ? 'Interview in progress' : item.bestPassedTests === null ? 'No verified score' : `${item.bestPassedTests} of ${item.totalTests} tests passed`}>
                    {item.status === 'IN_PROGRESS'
                      ? <Clock3 size={16} />
                      : <strong>{item.bestPassedTests ?? '—'}<small>/{item.totalTests ?? '—'}</small></strong>}
                  </span>
                  <span><strong>{item.title}</strong><small>{item.topic} · {friendlyLanguage(item.language)} · {formatDate(item.submittedAt ?? item.startedAt)}</small></span>
                  <em>{item.status === 'IN_PROGRESS' ? 'Resume' : 'Review'} <ChevronRight size={15} /></em>
                </button>
              ))}</div>}
        </section>
      </div>
      {startConfirmationOpen && (
        <StartInterviewDialog
          language={language}
          acknowledged={startAcknowledged}
          starting={starting}
          error={error}
          onAcknowledged={setStartAcknowledged}
          onCancel={cancelStart}
          onStart={() => void start()}
        />
      )}
    </main>
  )
}

function StartInterviewDialog({
  language,
  acknowledged,
  starting,
  error,
  onAcknowledged,
  onCancel,
  onStart,
}: {
  language: CodingLanguage
  acknowledged: boolean
  starting: boolean
  error: string | null
  onAcknowledged: (acknowledged: boolean) => void
  onCancel: () => void
  onStart: () => void
}) {
  return (
    <div className="lab-start-backdrop" role="presentation">
      <section
        className="lab-start-dialog"
        role="dialog"
        aria-modal="true"
        aria-labelledby="start-interview-title"
        aria-describedby="start-interview-description"
        data-testid="interview-start-confirmation"
      >
        <header>
          <div>
            <span className="eyebrow">Before the timer starts</span>
            <h2 id="start-interview-title">Ready for an uninterrupted round?</h2>
          </div>
          <button type="button" aria-label="Close start confirmation" disabled={starting} onClick={onCancel}><X size={18} /></button>
        </header>

        <p id="start-interview-description">
          Your problem is assigned only after you confirm. Review the format now—once created, the interview clock starts immediately.
        </p>

        <div className="lab-start-summary" aria-label="Selected round">
          <span><Code2 size={17} /><small>Language</small><strong>{friendlyLanguage(language)}</strong></span>
          <span><Clock3 size={17} /><small>Duration</small><strong>45 minutes</strong></span>
        </div>

        <div className="lab-start-details">
          <article><Clock3 size={18} /><div><strong>Server-timed session</strong><p>The 45-minute timer begins when the round is created. It cannot be paused or restarted.</p></div></article>
          <article><Target size={18} /><div><strong>Random interview problem</strong><p>An unused curated prompt is selected when available, with examples, constraints, and hidden tests.</p></div></article>
          <article><Save size={18} /><div><strong>Autosaved workspace</strong><p>Your code and reasoning notes are saved as you work. Test runs depend on the configured runner quota.</p></div></article>
          <article><ShieldCheck size={18} /><div><strong>Private review</strong><p>Your submission, verified score, self-assessment, and reflection remain available in your account history.</p></div></article>
        </div>

        <label className={`lab-start-acknowledgement ${acknowledged ? 'is-acknowledged' : ''}`}>
          <input
            type="checkbox"
            checked={acknowledged}
            disabled={starting}
            autoFocus
            onChange={(event) => onAcknowledged(event.target.checked)}
          />
          <span><strong>I understand and I’m ready to begin.</strong><small>The timer starts immediately and cannot be paused.</small></span>
        </label>

        {error && <div className="assessment-error" role="alert">{error}</div>}

        <footer>
          <button type="button" className="lab-start-cancel" disabled={starting} onClick={onCancel}>Not yet</button>
          <button type="button" className="lab-start-confirm" disabled={!acknowledged || starting} onClick={onStart} data-testid="confirm-interview-start">
            {starting ? <Loader2 className="lc-spin" size={15} /> : <Play size={15} fill="currentColor" />}
            {starting ? 'Preparing your round…' : 'Start 45-minute round'}
          </button>
        </footer>
      </section>
    </div>
  )
}

function CodingRunner({ session: initial, onSession, onExit }: { session: CodingInterviewSession; onSession: (session: CodingInterviewSession) => void; onExit: () => void }) {
  const [session, setLocalSession] = useState(initial)
  const [drafts, setDrafts] = useState<Partial<Record<CodingLanguage, string>>>({ [initial.language]: initial.solutionCode })
  const [remaining, setRemaining] = useState(() => Math.max(0, Math.ceil((new Date(initial.expiresAt).getTime() - Date.now()) / 1000)))
  const [saving, setSaving] = useState(false)
  const [saved, setSaved] = useState(true)
  const [rubricOpen, setRubricOpen] = useState(false)
  const [submitting, setSubmitting] = useState(false)
  const [runner, setRunner] = useState<CodingRunnerStatus | null>(null)
  const [latestRun, setLatestRun] = useState<CodingExecution | null>(null)
  const [running, setRunning] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [rubric, setRubric] = useState({ clarification: 3, approach: 3, correctness: 3, reflection: '' })

  useEffect(() => {
    void Promise.all([api.getCodingRunnerStatus(), api.getCodingRuns(initial.id)]).then(([status, runs]) => {
      setRunner(status); setLatestRun(runs[0] ?? null)
    }).catch(() => setRunner({ enabled: false, emailResults: false, dailyLimit: 0, remainingToday: 0 }))
  }, [initial.id])

  useEffect(() => {
    const timer = window.setInterval(() => {
      const next = Math.max(0, Math.ceil((new Date(session.expiresAt).getTime() - Date.now()) / 1000))
      setRemaining(next)
      if (next === 0) setRubricOpen(true)
    }, 1000)
    return () => window.clearInterval(timer)
  }, [session.expiresAt])

  useEffect(() => {
    if (!latestRun || terminalStatuses.has(latestRun.status)) { setRunning(false); return }
    setRunning(true)
    const timer = window.setTimeout(async () => {
      try { setLatestRun(await api.getCodingRun(latestRun.id)) }
      catch (caught) { setRunning(false); setError(caught instanceof ApiError ? caught.message : 'Could not refresh the code run.') }
    }, 1400)
    return () => window.clearTimeout(timer)
  }, [latestRun])

  useEffect(() => {
    setSaved(false)
    const timer = window.setTimeout(async () => {
      setSaving(true)
      try {
        const updated = await api.saveCodingInterviewDraft(session.id, {
          language: session.language, solutionCode: session.solutionCode,
          approachNotes: session.approachNotes, complexityAnalysis: session.complexityAnalysis,
        })
        setSaved(true); onSession(updated)
      } catch (caught) {
        if (caught instanceof ApiError && caught.status === 409) setRubricOpen(true)
        else setError(caught instanceof ApiError ? caught.message : 'Autosave failed. Your text remains in this browser.')
      } finally { setSaving(false) }
    }, 900)
    return () => window.clearTimeout(timer)
  }, [session.id, session.language, session.solutionCode, session.approachNotes, session.complexityAnalysis, onSession])

  const switchLanguage = (next: CodingLanguage) => {
    if (next === session.language) return
    setDrafts((current) => ({ ...current, [session.language]: session.solutionCode }))
    setLocalSession((current) => ({
      ...current,
      language: next,
      solutionCode: drafts[next] ?? current.prompt.starterCode[next],
    }))
  }

  const runCode = async () => {
    setRunning(true); setError(null)
    try {
      const result = await api.runCodingInterview(session.id, session.language, session.solutionCode)
      setLatestRun(result)
      setRunner((current) => current ? { ...current, remainingToday: Math.max(0, current.remainingToday - 1) } : current)
    } catch (caught) {
      setRunning(false); setError(caught instanceof ApiError ? caught.message : 'Could not start this code run.')
    }
  }

  const runButtonLabel = !running
    ? 'Run tests'
    : latestRun?.status === 'QUEUED'
      ? 'Queued securely…'
      : 'Running securely…'

  const resetStarter = () => {
    const starterCode = session.prompt.starterCode[session.language]
    if (session.solutionCode === starterCode) return
    if (!window.confirm(`Replace your current ${friendlyLanguage(session.language)} code with the original starter?`)) return
    setDrafts((current) => ({ ...current, [session.language]: starterCode }))
    setLocalSession((current) => ({ ...current, solutionCode: starterCode }))
  }

  const submit = async () => {
    setSubmitting(true); setError(null)
    try {
      onSession(await api.submitCodingInterview(session.id, {
        language: session.language, solutionCode: session.solutionCode,
        approachNotes: session.approachNotes, complexityAnalysis: session.complexityAnalysis, ...rubric,
      }))
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not finish this coding interview.')
    } finally { setSubmitting(false) }
  }

  return (
    <main className="coding-room">
      <div className="coding-room-bar">
        <button type="button" onClick={onExit}><ArrowLeft size={15} /> Save & exit</button>
        <div><strong>{session.prompt.title}</strong><span>{session.prompt.topic} · {friendlyLanguage(session.language)}</span></div>
        <span className={remaining <= 300 ? 'is-urgent' : ''}><Clock3 size={16} /> {formatTimer(remaining)}</span>
        <button type="button" className="is-primary" onClick={() => setRubricOpen(true)}>Finish & reflect</button>
      </div>
      {error && <div className="coding-room-error" role="alert">{error}</div>}
      <div className="coding-room-layout">
        <aside className="coding-prompt-panel">
          <span className="eyebrow">Interview problem</span>
          <div className="coding-prompt-meta"><span>{session.prompt.difficulty}</span><span>{session.prompt.topic}</span></div>
          <h1>{session.prompt.title}</h1>
          <div className="coding-prompt-copy"><QuestionMarkdown>{session.prompt.prompt}</QuestionMarkdown></div>
          <section><strong>Constraints</strong><pre>{session.prompt.constraints}</pre></section>
          <section><strong>Input format</strong><pre>{session.prompt.inputFormat}</pre></section>
          <section><strong>Output format</strong><pre>{session.prompt.outputFormat}</pre></section>
          <section><strong>Examples</strong><pre>{session.prompt.examples}</pre></section>
          <div className="coding-guidance"><Sparkles size={16} /><p><strong>Interview signal</strong>Explain assumptions and a baseline before optimizing. Then use the visible examples to validate the implementation.</p></div>
        </aside>

        <section className="coding-workspace">
          <div className="coding-editor-head">
            <div><Code2 size={16} /><strong>Runnable solution</strong></div>
            <div className="coding-language-tabs" role="radiogroup" aria-label="Editor language">
              {languages.map((item) => <button type="button" role="radio" aria-checked={session.language === item.value} className={session.language === item.value ? 'is-active' : ''} key={item.value} onClick={() => switchLanguage(item.value)}>{item.label}</button>)}
            </div>
            <button type="button" className="coding-reset-starter" disabled={session.solutionCode === session.prompt.starterCode[session.language]} onClick={resetStarter} title="Restore the formatted starter code">
              <RotateCcw size={12} /> Reset starter
            </button>
            <span>{saving ? <><Loader2 className="lc-spin" size={12} /> Saving</> : saved ? <><Check size={12} /> Saved</> : <><Save size={12} /> Unsaved</>}</span>
          </div>
          <CodeEditor
            language={session.language}
            value={session.solutionCode}
            onChange={(solutionCode) => setLocalSession((current) => ({ ...current, solutionCode }))}
          />

          <div className="code-run-toolbar">
            <div><TerminalSquare size={17} /><span><strong>{session.prompt.totalTests} tests</strong><small>{session.prompt.sampleTests.length} visible · the rest hidden</small></span></div>
            <button type="button" disabled={!runner?.enabled || running || runner.remainingToday < 1} onClick={() => void runCode()}>
              {running ? <Loader2 className="lc-spin" size={14} /> : <Play size={14} fill="currentColor" />} {runButtonLabel}
            </button>
          </div>
          {!runner?.enabled && <div className="runner-unavailable"><ServerCog size={17} /><span><strong>Secure runner is not configured on this deployment.</strong><small>Your problem-specific boilerplate and autosave still work. Start the self-hosted Podman worker to enable verified runs.</small></span></div>}
          {runner?.enabled && <div className="runner-quota"><ShieldCheck size={13} /> {runner.remainingToday} of {runner.dailyLimit} runs remain in this rolling day{runner.emailResults ? ' · results are also emailed' : ''}.</div>}
          {latestRun && <ExecutionConsole run={latestRun} />}

          <div className="coding-notes-grid">
            <label><span><Target size={14} /> Approach and assumptions</span><textarea maxLength={5000} value={session.approachNotes} onChange={(event) => setLocalSession((current) => ({ ...current, approachNotes: event.target.value }))} placeholder="Clarifying questions, baseline, chosen data structures…" /></label>
            <label><span><Braces size={14} /> Complexity</span><textarea maxLength={2000} value={session.complexityAnalysis} onChange={(event) => setLocalSession((current) => ({ ...current, complexityAnalysis: event.target.value }))} placeholder="Time, space and why…" /></label>
          </div>
        </section>
      </div>

      {rubricOpen && <RubricDialog remaining={remaining} rubric={rubric} setRubric={setRubric} submitting={submitting} error={error} onClose={() => setRubricOpen(false)} onSubmit={() => void submit()} />}
    </main>
  )
}

function ExecutionConsole({ run }: { run: CodingExecution }) {
  const pending = !terminalStatuses.has(run.status)
  const queued = run.status === 'QUEUED'
  const queuedForMilliseconds = queued ? Math.max(0, Date.now() - new Date(run.createdAt).getTime()) : 0
  const waitingForWorker = queuedForMilliseconds >= 10_000
  const pendingHeading = queued
    ? waitingForWorker ? 'Waiting for the secure runner' : 'Queued for secure execution'
    : 'Running in the sandbox'
  return (
    <section className={`execution-console is-${run.status.toLowerCase()}`} aria-live="polite">
      <header>
        <span>{pending ? <Loader2 className="lc-spin" size={15} /> : run.status === 'PASSED' ? <CheckCircle2 size={15} /> : <AlertCircle size={15} />}<strong>{pending ? pendingHeading : `${run.passedTests} / ${run.totalTests} tests passed`}</strong></span>
        <small>{friendlyLanguage(run.language)} · {run.status.replace('_', ' ')}</small>
      </header>
      {waitingForWorker && <p className="execution-pending-note">Your submission is safely queued, but no worker has claimed it yet. Verify that the worker is running and authenticated; this page will keep checking automatically.</p>}
      {run.failureMessage && <p>{run.failureMessage}</p>}
      {run.results.length > 0 && <div className="execution-case-list">{run.results.map((result) => (
        <article key={result.position} className={result.status === 'PASSED' ? 'is-passed' : 'is-failed'}>
          <span>{result.status === 'PASSED' ? <CheckCircle2 size={14} /> : <XCircle size={14} />} Test {result.position}{!result.visible && <em>hidden</em>}</span>
          <strong>{result.status.replaceAll('_', ' ')}</strong>
          {result.visible && <div><small>Input</small><pre>{result.input}</pre><small>Expected</small><pre>{result.expectedOutput}</pre><small>Your output</small><pre>{result.actualOutput || 'No output'}</pre></div>}
          {result.diagnostic && <pre className="execution-diagnostic">{result.diagnostic}</pre>}
        </article>
      ))}</div>}
    </section>
  )
}

type RubricState = { clarification: number; approach: number; correctness: number; reflection: string }

function RubricDialog({ remaining, rubric, setRubric, submitting, error, onClose, onSubmit }: { remaining: number; rubric: RubricState; setRubric: React.Dispatch<React.SetStateAction<RubricState>>; submitting: boolean; error: string | null; onClose: () => void; onSubmit: () => void }) {
  return <div className="lab-rubric-backdrop" role="presentation"><section className="lab-rubric-dialog" role="dialog" aria-modal="true" aria-labelledby="rubric-title">
    <header><div><span className="eyebrow">Focused self-review</span><h2 id="rubric-title">Reflect on the three signals that matter here.</h2></div>{remaining > 0 && <button type="button" aria-label="Return to interview" onClick={onClose}><X size={18} /></button>}</header>
    <p>Use 1 for missing, 3 for interview-ready with gaps, and 5 for consistently strong. Reference guidance appears only after submission.</p>
    <div className="lab-rubric-fields">{rubricFields.map((field) => <label key={field.key}><span><strong>{field.label}</strong><small>{field.hint}</small></span><select value={rubric[field.key]} onChange={(event) => setRubric((current) => ({ ...current, [field.key]: Number(event.target.value) }))}>{[1, 2, 3, 4, 5].map((score) => <option value={score} key={score}>{score} / 5</option>)}</select></label>)}</div>
    <label className="lab-reflection"><span>What will you do differently next time?</span><textarea maxLength={5000} value={rubric.reflection} onChange={(event) => setRubric((current) => ({ ...current, reflection: event.target.value }))} /></label>
    {error && <div className="assessment-error" role="alert">{error}</div>}
    <footer><button type="button" disabled={submitting} onClick={onSubmit}>{submitting ? <Loader2 className="lc-spin" size={15} /> : <FileCheck2 size={15} />} Submit and open review</button></footer>
  </section></div>
}

function CodingReview({ session, onBack }: { session: CodingInterviewSession; onBack: () => void }) {
  const [runs, setRuns] = useState<CodingExecution[]>([])
  const [runsLoaded, setRunsLoaded] = useState(false)
  useEffect(() => {
    setRunsLoaded(false)
    void api.getCodingRuns(session.id)
      .then(setRuns)
      .catch(() => setRuns([]))
      .finally(() => setRunsLoaded(true))
  }, [session.id])
  const bestRun = useMemo(() => runs.reduce<CodingExecution | null>((best, run) => !best || run.passedTests > best.passedTests ? run : best, null), [runs])
  return <main className="coding-review-page"><div className="shell coding-review-shell">
    <button type="button" className="result-back" onClick={onBack}><ArrowLeft size={15} /> Interview Lab</button>
    <section className="coding-review-hero">
      <div className="coding-review-score" aria-label={bestRun ? `${bestRun.passedTests} of ${bestRun.totalTests} tests passed` : 'No verified score'}><strong>{bestRun?.passedTests ?? '—'}</strong><span>/{bestRun?.totalTests ?? session.prompt.totalTests}</span></div>
      <div><span className="eyebrow">Coding interview review</span><h1>{session.prompt.title}</h1><p>{session.prompt.topic} · {friendlyLanguage(session.language)} · {session.status === 'TIMED_OUT' ? 'Timer completed' : 'Submitted'}.</p><small className="coding-review-score-label">{!runsLoaded ? 'Loading verified score…' : bestRun ? `Best verified run · ${formatDate(bestRun.createdAt)}` : 'No completed code run—there is no verified score yet.'}</small></div>
    </section>
    <div className="coding-review-grid">
      <section><span className="eyebrow">Your reasoning</span><h2>What you recorded</h2><div className="review-text-block"><strong>Approach</strong><pre>{session.approachNotes || 'No approach notes saved.'}</pre></div><div className="review-text-block"><strong>Complexity</strong><pre>{session.complexityAnalysis || 'No complexity analysis saved.'}</pre></div></section>
      <section className="expected-solution"><span className="eyebrow">Reference direction</span><h2>Expected reasoning</h2><p>{session.prompt.expectedApproach}</p><div><span><strong>{session.prompt.expectedTimeComplexity}</strong><small>expected time</small></span><span><strong>{session.prompt.expectedSpaceComplexity}</strong><small>expected space</small></span></div><p className="review-disclaimer">This is a strong direction, not the only valid implementation.</p></section>
    </div>
    <section className="coding-review-code"><header><Code2 size={16} /><strong>Your submitted code</strong></header><pre><code>{session.solutionCode || '// No code saved.'}</code></pre></section>
    <section className="coding-review-rubric"><header><span className="eyebrow">Your reflection</span><h2>Self-assessed interview signals</h2></header><div>{rubricFields.map((field) => <span key={field.key}><strong>{field.label}</strong><em>{session.rubric?.[field.key] ?? '—'} / 5</em></span>)}</div>{session.reflection && <blockquote><strong>Next-round reflection</strong>{session.reflection}</blockquote>}</section>
    {runs.length > 0 && <section className="review-run-history"><header><TerminalSquare size={16} /><strong>Execution history</strong></header>{runs.map((run) => <div key={run.id}><span>{run.status === 'PASSED' ? <Check size={14} /> : <RotateCcw size={14} />}{formatDate(run.createdAt)} · {friendlyLanguage(run.language)}</span><strong>{run.passedTests}/{run.totalTests}</strong></div>)}</section>}
  </div></main>
}
