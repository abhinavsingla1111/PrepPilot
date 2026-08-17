import type { CurrentUser } from '../types/api'
import { Brand } from './Brand'

interface FooterProps {
  user: CurrentUser | null
  onLogin: () => void
  onFeedback: () => void
}

export function Footer({ user, onLogin, onFeedback }: FooterProps) {
  const openFeedback = () => user ? onFeedback() : onLogin()

  return (
    <footer className="site-footer">
      <div className="footer-glow" aria-hidden="true" />
      <div className="shell footer-inner">
        <div className="footer-main">
          <div className="footer-brand-block">
            <Brand />
            <p>One calm workspace for deliberate interview practice.</p>
          </div>
          <nav className="footer-nav" aria-label="Footer navigation">
            <div><span>Practice</span><a href="#questions">DSA questions</a><a href="#practice">Knowledge checks</a><a href="#interview-lab">Interview Lab</a><a href="#mistakes">Mistake Book</a></div>
            <div><span>Learn</span><a href="#cheatsheet">Cheat sheet</a><a href="#fundamentals">Fundamentals</a><a href="#systemdesign">System design</a></div>
            <div><span>Connect</span><button type="button" onClick={openFeedback}>Suggestions</button>{user && <a href="#profile">Your profile</a>}</div>
          </nav>
        </div>

        <div className="footer-bottom">
          <p>© {new Date().getFullYear()} PrepPilot. Built for focused practice.</p>
          <p className="disclaimer">Independent study tool · Not affiliated with LeetCode</p>
        </div>
      </div>
    </footer>
  )
}
