import { ArrowLeft, ArrowRight, Check, KeyRound, Mail, ShieldCheck, X } from 'lucide-react'
import { type FormEvent, useEffect, useRef, useState } from 'react'
import { api, ApiError } from '../lib/api'
import type { CurrentUser } from '../types/api'

interface AuthModalProps {
  isOpen: boolean
  onClose: () => void
  onAuthenticated: (user: CurrentUser) => void
}

type Step = 'email' | 'code'

export function AuthModal({ isOpen, onClose, onAuthenticated }: AuthModalProps) {
  const [step, setStep] = useState<Step>('email')
  const [email, setEmail] = useState('')
  const [code, setCode] = useState('')
  const [devOtp, setDevOtp] = useState<string | null>(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const emailInputRef = useRef<HTMLInputElement>(null)

  useEffect(() => {
    if (!isOpen) return
    const onKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') onClose()
    }
    document.body.classList.add('modal-open')
    window.addEventListener('keydown', onKeyDown)
    window.setTimeout(() => emailInputRef.current?.focus(), 50)
    return () => {
      document.body.classList.remove('modal-open')
      window.removeEventListener('keydown', onKeyDown)
    }
  }, [isOpen, onClose])

  if (!isOpen) return null

  const requestCode = async (event: FormEvent) => {
    event.preventDefault()
    setLoading(true)
    setError('')
    try {
      const response = await api.requestOtp(email.trim())
      setDevOtp(response.devOtp ?? null)
      setStep('code')
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not send your code. Is the API running?')
    } finally {
      setLoading(false)
    }
  }

  const verifyCode = async (event: FormEvent) => {
    event.preventDefault()
    setLoading(true)
    setError('')
    try {
      const user = await api.verifyOtp(email.trim(), code)
      onAuthenticated(user)
      onClose()
      setStep('email')
      setCode('')
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'That code could not be verified.')
    } finally {
      setLoading(false)
    }
  }

  const reset = () => {
    setStep('email')
    setCode('')
    setError('')
    setDevOtp(null)
  }

  return (
    <div className="modal-backdrop" role="presentation" onMouseDown={(event) => {
      if (event.target === event.currentTarget) onClose()
    }}>
      <section className="auth-modal" role="dialog" aria-modal="true" aria-labelledby="auth-title">
        <button className="modal-close" type="button" onClick={onClose} aria-label="Close login">
          <X size={19} />
        </button>

        <div className="auth-art" aria-hidden="true">
          <div className="auth-art-grid" />
          <div className="auth-signal signal-one"><span>01</span><i /></div>
          <div className="auth-signal signal-two"><span>n</span><i /></div>
          <div className="auth-signal signal-three"><span>log n</span><i /></div>
          <div className="auth-orbit"><ShieldCheck size={28} /></div>
          <p>One focused session<br />at a time.</p>
        </div>

        <div className="auth-content">
          <div className="auth-icon">{step === 'email' ? <Mail size={21} /> : <KeyRound size={21} />}</div>
          {step === 'email' ? (
            <>
              <span className="eyebrow">Welcome to PrepPilot</span>
              <h2 id="auth-title">Pick up where you left off.</h2>
              <p>We’ll email a one-time code. No password to remember, and no LeetCode credentials are ever stored.</p>
              <form onSubmit={requestCode}>
                <label htmlFor="email">Email address</label>
                <div className="field-wrap">
                  <Mail size={17} aria-hidden="true" />
                  <input
                    ref={emailInputRef}
                    id="email"
                    name="email"
                    type="email"
                    autoComplete="email"
                    placeholder="you@example.com"
                    value={email}
                    onChange={(event) => setEmail(event.target.value)}
                    required
                  />
                </div>
                {error && <div className="form-error" role="alert">{error}</div>}
                <button className="button auth-submit" type="submit" disabled={loading}>
                  {loading ? 'Sending…' : 'Continue with email'}
                  {!loading && <ArrowRight size={17} />}
                </button>
              </form>
              <div className="privacy-note"><ShieldCheck size={15} /> Secure, passwordless sign-in</div>
            </>
          ) : (
            <>
              <button className="back-button" type="button" onClick={reset}><ArrowLeft size={15} /> Change email</button>
              <span className="eyebrow">Check your inbox</span>
              <h2 id="auth-title">Enter your 6-digit code.</h2>
              <p>We sent a short-lived sign-in code to <strong>{email}</strong>.</p>
              {devOtp && (
                <button className="dev-code" type="button" onClick={() => setCode(devOtp)}>
                  <span><Check size={14} /> Local preview code</span>
                  <strong>{devOtp}</strong>
                </button>
              )}
              <form onSubmit={verifyCode}>
                <label htmlFor="otp">One-time code</label>
                <input
                  className="otp-field"
                  id="otp"
                  name="otp"
                  inputMode="numeric"
                  autoComplete="one-time-code"
                  pattern="[0-9]{6}"
                  maxLength={6}
                  placeholder="000000"
                  value={code}
                  onChange={(event) => setCode(event.target.value.replace(/\D/g, '').slice(0, 6))}
                  autoFocus
                  required
                />
                {error && <div className="form-error" role="alert">{error}</div>}
                <button className="button auth-submit" type="submit" disabled={loading || code.length !== 6}>
                  {loading ? 'Verifying…' : 'Verify and continue'}
                  {!loading && <ArrowRight size={17} />}
                </button>
              </form>
            </>
          )}
        </div>
      </section>
    </div>
  )
}
