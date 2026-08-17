import { Menu, X } from 'lucide-react'
import { useState } from 'react'
import type { CurrentUser } from '../types/api'
import { Brand } from './Brand'
import { ThemeToggle } from './ThemeToggle'

interface HeaderProps {
  user: CurrentUser | null
  activeRoute: string
  displayName: string | null
  avatarUrl: string | null
  onLogin: () => void
}

export function Header({ user, activeRoute, displayName, avatarUrl, onLogin }: HeaderProps) {
  const [menuOpen, setMenuOpen] = useState(false)

  const closeMenu = () => setMenuOpen(false)

  return (
    <header className="site-header">
      <div className="shell header-inner">
        <Brand />
        <div className={`header-nav-wrap ${menuOpen ? 'is-open' : ''}`}>
          <nav className="primary-nav" aria-label="Primary navigation">
            <a
              href="#questions"
              className={activeRoute === 'questions' ? 'is-active' : ''}
              aria-current={activeRoute === 'questions' ? 'page' : undefined}
              onClick={closeMenu}
            >
              DSA questions
            </a>
            <a
              href="#cheatsheet"
              className={activeRoute === 'cheatsheet' ? 'is-active' : ''}
              aria-current={activeRoute === 'cheatsheet' ? 'page' : undefined}
              onClick={closeMenu}
            >
              Cheat sheet
            </a>
            <a
              href="#fundamentals"
              className={activeRoute === 'fundamentals' || activeRoute === 'oop' ? 'is-active' : ''}
              aria-current={activeRoute === 'fundamentals' || activeRoute === 'oop' ? 'page' : undefined}
              onClick={closeMenu}
            >
              Fundamentals
            </a>
            <a
              href="#systemdesign"
              className={activeRoute === 'systemdesign' ? 'is-active' : ''}
              aria-current={activeRoute === 'systemdesign' ? 'page' : undefined}
              onClick={closeMenu}
            >
              System design
            </a>
            <a
              href="#practice"
              className={activeRoute === 'practice' ? 'is-active' : ''}
              aria-current={activeRoute === 'practice' ? 'page' : undefined}
              onClick={closeMenu}
            >
              Knowledge checks
            </a>
            <a
              href="#interview-lab"
              className={activeRoute === 'interview-lab' ? 'is-active' : ''}
              aria-current={activeRoute === 'interview-lab' ? 'page' : undefined}
              onClick={closeMenu}
            >
              Interview lab
            </a>
          </nav>
          <div className="header-actions">
            {user ? (
              <div className="user-menu">
                <a
                  className={`user-identity ${activeRoute === 'profile' ? 'is-active' : ''}`}
                  href="#profile"
                  onClick={closeMenu}
                  aria-label="Open your profile"
                >
                  {avatarUrl ? (
                    <img className="avatar avatar-img" src={avatarUrl} alt="" aria-hidden="true" />
                  ) : (
                    <span className="avatar" aria-hidden="true">
                      {(displayName?.charAt(0) ?? user.email.charAt(0)).toUpperCase()}
                    </span>
                  )}
                  <span className="user-email">{displayName ?? user.email}</span>
                </a>
              </div>
            ) : (
              <>
                <button className="text-button" type="button" onClick={onLogin}>Log in</button>
                <button className="button button-small" type="button" onClick={onLogin}>Start preparing</button>
              </>
            )}
          </div>
        </div>
        <ThemeToggle />
        <button
          className="mobile-menu-button"
          type="button"
          aria-label={menuOpen ? 'Close navigation' : 'Open navigation'}
          aria-expanded={menuOpen}
          onClick={() => setMenuOpen((open) => !open)}
        >
          {menuOpen ? <X size={20} /> : <Menu size={20} />}
        </button>
      </div>
    </header>
  )
}
