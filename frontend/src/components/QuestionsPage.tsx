import { useEffect } from 'react'
import { QuestionExplorer } from './QuestionExplorer'
import type { CurrentUser } from '../types/api'

interface QuestionsPageProps {
  user: CurrentUser | null
  onLogin: () => void
}

export function QuestionsPage({ user, onLogin }: QuestionsPageProps) {
  useEffect(() => {
    window.scrollTo({ top: 0 })
  }, [])

  return (
    <main className="questions-page">
      <QuestionExplorer user={user} onLogin={onLogin} pageSize={20} />
    </main>
  )
}
