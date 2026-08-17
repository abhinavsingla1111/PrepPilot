import { useCallback, useEffect, useState } from 'react'
import { AuthModal } from './components/AuthModal'
import { CheatSheet } from './components/CheatSheet'
import { ClosingSection } from './components/ClosingSection'
import { ConfirmDialog } from './components/ConfirmDialog'
import { Footer } from './components/Footer'
import { FeedbackModal } from './components/FeedbackModal'
import { Fundamentals } from './components/Fundamentals'
import { Header } from './components/Header'
import { Hero } from './components/Hero'
import { InterviewLab } from './components/InterviewLab'
import { MistakeBook } from './components/MistakeBook'
import { ProfilePage } from './components/ProfilePage'
import { PracticeTests } from './components/PracticeTests'
import { QuestionsPage } from './components/QuestionsPage'
import { SystemDesign } from './components/SystemDesign'
import { api } from './lib/api'
import type { CurrentUser, DashboardSummary, UserProfile } from './types/api'

export default function App() {
  const [user, setUser] = useState<CurrentUser | null>(null)
  const [authOpen, setAuthOpen] = useState(false)
  const [route, setRoute] = useState(() => window.location.hash.replace(/^#/, ''))
  const [summary, setSummary] = useState<DashboardSummary | null>(null)
  const [profile, setProfile] = useState<UserProfile | null>(null)
  const [logoutOpen, setLogoutOpen] = useState(false)
  const [loggingOut, setLoggingOut] = useState(false)
  const [feedbackOpen, setFeedbackOpen] = useState(false)

  useEffect(() => {
    api.getMe().then(setUser).catch(() => setUser(null))
  }, [])

  useEffect(() => {
    const onHashChange = () => setRoute(window.location.hash.replace(/^#/, ''))
    window.addEventListener('hashchange', onHashChange)
    return () => window.removeEventListener('hashchange', onHashChange)
  }, [])

  useEffect(() => {
    if (!user) {
      setSummary(null)
      setProfile(null)
      return
    }
    api.getSummary().then(setSummary).catch(() => setSummary(null))
    api.getProfile().then(setProfile).catch(() => setProfile(null))
  }, [user])

  const openAuth = useCallback(() => setAuthOpen(true), [])
  const closeAuth = useCallback(() => setAuthOpen(false), [])
  const requestLogout = useCallback(() => setLogoutOpen(true), [])
  const confirmLogout = useCallback(async () => {
    setLoggingOut(true)
    try {
      await api.logout()
    } finally {
      setUser(null)
      setLoggingOut(false)
      setLogoutOpen(false)
    }
  }, [])

  const showCheatSheet = route === 'cheatsheet'
  const showFundamentals = route === 'fundamentals' || route === 'oop'
  const showSystemDesign = route === 'systemdesign'
  const showQuestions = route === 'questions'
  const showProfile = route === 'profile'
  const showPractice = route === 'practice'
  const showMistakes = route === 'mistakes'
  const showInterviewLab = route === 'interview-lab'

  return (
    <div className="site-frame">
      <Header
        user={user}
        activeRoute={route}
        displayName={profile?.fullName ?? null}
        avatarUrl={profile?.avatarUrl ?? null}
        onLogin={openAuth}
      />
      {showCheatSheet ? (
        <CheatSheet />
      ) : showFundamentals ? (
        <Fundamentals initialDoc={route === 'oop' ? 'oop-overview' : 'overview'} />
      ) : showSystemDesign ? (
        <SystemDesign />
      ) : showQuestions ? (
        <QuestionsPage user={user} onLogin={openAuth} />
      ) : showPractice ? (
        <PracticeTests user={user} onLogin={openAuth} />
      ) : showMistakes ? (
        <MistakeBook user={user} onLogin={openAuth} />
      ) : showInterviewLab ? (
        <InterviewLab user={user} onLogin={openAuth} />
      ) : showProfile ? (
        <ProfilePage user={user} onLogin={openAuth} onLogout={requestLogout} onProfileChange={setProfile} />
      ) : (
        <>
          <Hero user={user} summary={summary} onGetStarted={openAuth} />
          <ClosingSection />
        </>
      )}
      <Footer user={user} onLogin={openAuth} onFeedback={() => setFeedbackOpen(true)} />
      <FeedbackModal isOpen={feedbackOpen} user={user} onClose={() => setFeedbackOpen(false)} />
      <AuthModal isOpen={authOpen} onClose={closeAuth} onAuthenticated={setUser} />
      <ConfirmDialog
        open={logoutOpen}
        title="Sign out of PrepPilot?"
        message={user ? `You're signed in as ${user.email}. You'll need a new one-time code to sign back in.` : 'You will be signed out.'}
        confirmLabel="Sign out"
        busy={loggingOut}
        onConfirm={confirmLogout}
        onCancel={() => setLogoutOpen(false)}
      />
    </div>
  )
}
