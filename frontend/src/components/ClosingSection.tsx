import {
  ArrowUpRight,
  BookMarked,
  BrainCircuit,
  Braces,
  CheckCircle2,
  Clock3,
  Code2,
  Layers3,
  Network,
  RotateCcw,
} from 'lucide-react'

export function ClosingSection() {
  return (
    <>
      <section className="workspace-section shell" id="workspace">
        <header className="workspace-intro">
          <div>
            <span className="eyebrow">One workspace. The complete loop.</span>
            <h2>Everything you added now works together.</h2>
          </div>
          <p>
            Start with the concept, apply it in context, test recall under a timer, then return to the exact answers that need work.
          </p>
        </header>

        <div className="feature-bento">
          <a className="feature-card feature-card-practice" href="#practice">
            <div className="feature-card-head">
              <span className="feature-icon"><Clock3 size={20} /></span>
              <span className="feature-stat">9 tracks · 900 MCQs</span>
            </div>
            <div className="feature-card-copy">
              <small>Knowledge checks</small>
              <h3>Interview pressure, without the interview.</h3>
              <p>Twenty balanced questions in a topic-aware 10–15 minute window, followed by explanations, saved history, and an automatic Mistake Book revision queue.</p>
            </div>
            <div className="test-preview" aria-hidden="true">
              <div><span>Operating Systems</span><strong>09:42</strong></div>
              <div className="test-preview-progress"><i /></div>
              <div className="test-preview-options">
                <span><b>A</b> TCP</span><span className="is-selected"><b>B</b> QUIC</span>
              </div>
              <div className="test-preview-foot"><span>Question 7 of 20</span><span>Easy + medium + hard</span></div>
            </div>
            <span className="feature-link">Choose a knowledge check <ArrowUpRight size={15} /></span>
          </a>

          <a className="feature-card feature-card-fundamentals" href="#fundamentals">
            <div className="feature-card-head">
              <span className="feature-icon"><Network size={20} /></span>
              <span className="feature-stat">8 learning tracks</span>
            </div>
            <div className="feature-card-copy">
              <small>Fundamentals</small>
              <h3>Learn it well enough to explain it.</h3>
              <p>Java, Spring Boot, React, Python, databases, OOP, Computer Networks, and Operating Systems—from first principles to interview follow-ups.</p>
            </div>
            <div className="network-mini-flow" aria-hidden="true">
              <span>DNS</span><i /><span>TCP</span><i /><span>TLS</span><i /><span>HTTP</span>
            </div>
            <span className="feature-link">Open Fundamentals <ArrowUpRight size={15} /></span>
          </a>

          <a className="feature-card feature-card-dsa" href="#questions">
            <div className="feature-card-head">
              <span className="feature-icon"><Code2 size={20} /></span>
              <span className="feature-stat">145 questions</span>
            </div>
            <div className="feature-card-copy">
              <small>DSA question library</small>
              <h3>High-signal patterns, neatly organized.</h3>
              <p>Filter by topic and difficulty, open the original problem, and keep your progress visible.</p>
            </div>
            <span className="feature-link">Browse questions <ArrowUpRight size={15} /></span>
          </a>

          <a className="feature-card feature-card-design" href="#systemdesign">
            <div className="feature-card-head">
              <span className="feature-icon"><Layers3 size={20} /></span>
              <span className="feature-stat">HLD + LLD</span>
            </div>
            <div className="feature-card-copy">
              <small>System design</small>
              <h3>See the whole system—not disconnected boxes.</h3>
              <p>Requirements, estimates, APIs, data models, diagrams, trade-offs, failure modes, and interview FAQs.</p>
            </div>
            <span className="feature-link">Study a design <ArrowUpRight size={15} /></span>
          </a>

          <a className="feature-card feature-card-cheats" href="#cheatsheet">
            <div className="feature-card-head">
              <span className="feature-icon"><BookMarked size={20} /></span>
              <span className="feature-stat">Fast recall</span>
            </div>
            <div className="feature-card-copy">
              <small>Cheat sheets</small>
              <h3>Refresh syntax without losing your flow.</h3>
              <p>Compact, systematic references for the concepts and code patterns you need at your fingertips.</p>
            </div>
            <span className="feature-link">Open cheat sheets <ArrowUpRight size={15} /></span>
          </a>
        </div>
      </section>

      <section className="learning-loop-section">
        <div className="shell learning-loop-inner">
          <div className="learning-loop-copy">
            <span className="eyebrow">A repeatable preparation loop</span>
            <h2>Know what to do next.</h2>
            <p>No scattered bookmarks or random quizzes. Every mode has a clear purpose and naturally leads to the next one.</p>
            <a className="secondary-button" href="#fundamentals">Start with a concept <ArrowUpRight size={17} /></a>
          </div>
          <ol className="learning-loop" aria-label="PrepPilot learning loop">
            <li><span><BrainCircuit size={18} /></span><div><small>01 · Understand</small><strong>Build the mental model</strong><p>Read the focused explanation and trace the visual flow.</p></div></li>
            <li><span><Braces size={18} /></span><div><small>02 · Apply</small><strong>Solve in context</strong><p>Use the idea in DSA or an end-to-end design.</p></div></li>
            <li><span><CheckCircle2 size={18} /></span><div><small>03 · Validate</small><strong>Test under a timer</strong><p>Mix easy, medium, and hard questions without hints.</p></div></li>
            <li><span><RotateCcw size={18} /></span><div><small>04 · Retain</small><strong>Review the misses</strong><p>Use explanations and attempt history to close the gap.</p></div></li>
          </ol>
        </div>
      </section>
    </>
  )
}
