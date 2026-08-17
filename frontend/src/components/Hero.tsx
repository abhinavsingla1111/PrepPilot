import {
  ArrowUpRight,
  Clock3,
  Code2,
  Cpu,
  ExternalLink,
  Sparkles,
} from 'lucide-react'
import type { CurrentUser, DashboardSummary, Difficulty } from '../types/api'

interface HeroProps {
  user: CurrentUser | null
  summary: DashboardSummary | null
  onGetStarted: () => void
}

const difficultyLabel: Record<Difficulty, string> = {
  EASY: 'Easy',
  MEDIUM: 'Medium',
  HARD: 'Hard',
}

export function Hero({ user, summary, onGetStarted }: HeroProps) {
  const signedIn = Boolean(user)
  const hasSummary = signedIn && summary !== null
  const percent = hasSummary && summary!.totalProblems > 0
    ? Math.round((summary!.solved / summary!.totalProblems) * 100)
    : 68
  const recommendation = hasSummary ? summary!.recommended[0] : null

  return (
    <main id="top">
      <section className="hero shell">
        <div className="hero-copy">
          <div className="hero-kicker"><span /> Learn · solve · check · review</div>
          <h1>Turn interview prep into a <em>clear system.</em></h1>
          <p className="hero-lede">
            Build the fundamentals, practise high-signal DSA, design real systems, and test yourself under interview conditions—all in one focused workspace.
          </p>
          <div className="hero-actions">
            {signedIn ? (
              <>
                <a className="button button-large" href="#questions">
                  Continue preparing <ArrowUpRight size={18} />
                </a>
                <a className="secondary-button" href="#fundamentals">
                  Open fundamentals <ArrowUpRight size={18} />
                </a>
              </>
            ) : (
              <>
                <button className="button button-large" type="button" onClick={onGetStarted}>
                  Start preparing free <ArrowUpRight size={18} />
                </button>
                <a className="secondary-button" href="#fundamentals">
                  Explore the workspace <ArrowUpRight size={18} />
                </a>
              </>
            )}
          </div>
          <div className="hero-snapshot" aria-label="PrepPilot content at a glance">
            <div><strong>145</strong><span>DSA questions</span></div>
            <div><strong>900</strong><span>interview MCQs</span></div>
            <div><strong>8</strong><span>fundamental tracks</span></div>
          </div>
        </div>

        <div className="hero-dashboard" aria-label="PrepPilot interview workspace preview">
          <div className="dashboard-glow" />
          <div className="dashboard-head">
            <div>
              <span className="dashboard-label">{hasSummary ? 'Your interview workspace' : 'Your preparation loop'}</span>
              <strong>{hasSummary ? `${summary!.solved} DSA questions solved` : 'One focused next step at a time'}</strong>
            </div>
            <span className="live-pill"><i /> READY</span>
          </div>

          <div className="dashboard-main">
            <div
              className="progress-orbit"
              style={{ background: `conic-gradient(var(--green) 0 ${percent}%, #263027 ${percent}% 100%)` }}
            >
              <div><strong>{percent}</strong><span>%</span><small>{hasSummary ? 'DSA complete' : 'sample progress'}</small></div>
            </div>
            <div className="dashboard-stats">
              <div>
                <span>{hasSummary ? 'Solved' : 'Question library'}</span>
                <strong>{hasSummary ? summary!.solved : 145}</strong>
                <small>{hasSummary ? `of ${summary!.totalProblems}` : 'curated problems'}</small>
              </div>
              <div>
                <span>{hasSummary ? 'This week' : 'Practice tracks'}</span>
                <strong>{hasSummary ? `+${summary!.solvedThisWeek}` : 9}</strong>
                <small>{hasSummary ? 'problems' : '900 MCQs'}</small>
              </div>
            </div>
          </div>

          <div className="workspace-next">
            <a href="#fundamentals" className="workspace-next-item is-featured">
              <span className="workspace-next-icon"><Cpu size={17} /></span>
              <span><small>New in Fundamentals</small><strong>Operating Systems</strong><em>10 chapters · visual flows</em></span>
              <ArrowUpRight size={15} />
            </a>
            <a href="#interview-lab" className="workspace-next-item">
              <span className="workspace-next-icon"><Code2 size={17} /></span>
              <span><small>Interview Lab</small><strong>45-minute coding round</strong><em>Autosave · rubric · review</em></span>
              <ArrowUpRight size={15} />
            </a>
            {recommendation ? (
              <a className="workspace-next-item" href={recommendation.url} target="_blank" rel="noreferrer">
                <span className="workspace-next-icon"><Sparkles size={16} /></span>
                <span><small>Recommended DSA</small><strong>{recommendation.title}</strong><em>{recommendation.topic} · {difficultyLabel[recommendation.difficulty]}</em></span>
                <ExternalLink size={14} />
              </a>
            ) : (
              <a href="#practice" className="workspace-next-item">
                <span className="workspace-next-icon"><Clock3 size={17} /></span>
                <span><small>Knowledge Checks</small><strong>20 questions in 10–15 minutes</strong><em>Topic-paced · Mistake Book</em></span>
                <ArrowUpRight size={15} />
              </a>
            )}
          </div>
        </div>
      </section>

      <section className="principles-strip" id="method">
        <div className="shell principles-grid">
          <div><span>01</span><p><strong>Learn the “why”</strong>Build a mental model with concise notes and visual flows.</p></div>
          <div><span>02</span><p><strong>Apply the pattern</strong>Use curated DSA and complete system-design walkthroughs.</p></div>
          <div><span>03</span><p><strong>Simulate pressure</strong>Take balanced 10–15 minute Knowledge Checks or enter the Interview Lab.</p></div>
          <div><span>04</span><p><strong>Review every gap</strong>Let the Mistake Book schedule the exact answers that need another look.</p></div>
        </div>
      </section>
    </main>
  )
}
