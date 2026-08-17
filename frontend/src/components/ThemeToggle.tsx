import { Moon, Sun } from 'lucide-react'
import { useColorScheme } from '../hooks/useColorScheme'

export function ThemeToggle() {
  const { scheme, toggleScheme } = useColorScheme()
  const nextScheme = scheme === 'dark' ? 'light' : 'dark'

  return (
    <button
      className={`theme-toggle is-${scheme}`}
      type="button"
      aria-label={`Switch to ${nextScheme} theme`}
      aria-pressed={scheme === 'dark'}
      title={`Switch to ${nextScheme} theme`}
      onClick={toggleScheme}
    >
      <span aria-hidden="true"><Sun size={14} /></span>
      <span aria-hidden="true"><Moon size={14} /></span>
    </button>
  )
}
