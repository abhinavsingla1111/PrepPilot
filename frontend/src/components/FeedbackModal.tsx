import { AlertTriangle, Check, ImagePlus, Lightbulb, MessageSquareText, ShieldCheck, Trash2, X } from 'lucide-react'
import { type ChangeEvent, type FormEvent, useEffect, useRef, useState } from 'react'
import { api, ApiError } from '../lib/api'
import type { CurrentUser, FeedbackResponse, FeedbackSeverity, FeedbackType } from '../types/api'

const MAX_IMAGE_BYTES = 8 * 1024 * 1024

const SEVERITIES: Array<{ value: FeedbackSeverity; label: string; hint: string }> = [
  { value: 'LOW', label: 'Low', hint: 'Small polish' },
  { value: 'MEDIUM', label: 'Medium', hint: 'Gets in the way' },
  { value: 'HIGH', label: 'High', hint: 'Blocks a workflow' },
  { value: 'CRITICAL', label: 'Critical', hint: 'App is unusable' },
]

interface FeedbackModalProps {
  isOpen: boolean
  user: CurrentUser | null
  onClose: () => void
}

export function FeedbackModal({ isOpen, user, onClose }: FeedbackModalProps) {
  const [type, setType] = useState<FeedbackType>('IMPROVEMENT')
  const [severity, setSeverity] = useState<FeedbackSeverity>('MEDIUM')
  const [subject, setSubject] = useState('')
  const [description, setDescription] = useState('')
  const [image, setImage] = useState<File | null>(null)
  const [previewUrl, setPreviewUrl] = useState('')
  const [error, setError] = useState('')
  const [sending, setSending] = useState(false)
  const [submitted, setSubmitted] = useState<FeedbackResponse | null>(null)
  const subjectRef = useRef<HTMLInputElement>(null)
  const fileRef = useRef<HTMLInputElement>(null)

  useEffect(() => {
    if (!isOpen) return
    const onKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape') onClose()
    }
    document.body.classList.add('modal-open')
    window.addEventListener('keydown', onKeyDown)
    window.setTimeout(() => subjectRef.current?.focus(), 60)
    return () => {
      document.body.classList.remove('modal-open')
      window.removeEventListener('keydown', onKeyDown)
    }
  }, [isOpen, onClose])

  useEffect(() => {
    if (!image) {
      setPreviewUrl('')
      return
    }
    const nextUrl = URL.createObjectURL(image)
    setPreviewUrl(nextUrl)
    return () => URL.revokeObjectURL(nextUrl)
  }, [image])

  if (!isOpen || !user) return null

  const chooseImage = (event: ChangeEvent<HTMLInputElement>) => {
    const next = event.target.files?.[0] ?? null
    setError('')
    if (!next) return
    if (!next.type.startsWith('image/')) {
      setError('Please choose an image file.')
      event.target.value = ''
      return
    }
    if (next.size > MAX_IMAGE_BYTES) {
      setError('Please choose an image smaller than 8 MB.')
      event.target.value = ''
      return
    }
    setImage(next)
  }

  const removeImage = () => {
    setImage(null)
    if (fileRef.current) fileRef.current.value = ''
  }

  const submit = async (event: FormEvent) => {
    event.preventDefault()
    setError('')
    setSending(true)
    const form = new FormData()
    form.append('type', type)
    form.append('severity', severity)
    form.append('subject', subject.trim())
    form.append('description', description.trim())
    if (image) form.append('image', image)

    try {
      setSubmitted(await api.submitFeedback(form))
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Your feedback could not be sent. Please try again.')
    } finally {
      setSending(false)
    }
  }

  const closeAndReset = () => {
    onClose()
    window.setTimeout(() => {
      setType('IMPROVEMENT')
      setSeverity('MEDIUM')
      setSubject('')
      setDescription('')
      setImage(null)
      setError('')
      setSubmitted(null)
    }, 180)
  }

  return (
    <div className="modal-backdrop feedback-backdrop" role="presentation" onMouseDown={(event) => {
      if (event.target === event.currentTarget) closeAndReset()
    }}>
      <section className="feedback-modal" role="dialog" aria-modal="true" aria-labelledby="feedback-title">
        <button className="modal-close" type="button" onClick={closeAndReset} aria-label="Close feedback form">
          <X size={19} />
        </button>

        {submitted ? (
          <div className="feedback-success">
            <span className="feedback-success-icon"><Check size={27} /></span>
            <span className="eyebrow">Signal received</span>
            <h2 id="feedback-title">Thanks for helping PrepPilot improve.</h2>
            <p>Your request is safely stored with reference <strong>{submitted.id.slice(0, 8).toUpperCase()}</strong>.</p>
            {submitted.emailStatus === 'FAILED' && (
              <p className="feedback-email-note">The email copy could not be delivered, but your request is saved for review.</p>
            )}
            <button className="button" type="button" onClick={closeAndReset}>Back to learning</button>
          </div>
        ) : (
          <>
            <header className="feedback-modal-head">
              <span className="feedback-modal-icon"><MessageSquareText size={21} /></span>
              <div>
                <span className="eyebrow">Help shape PrepPilot</span>
                <h2 id="feedback-title">Send a signal.</h2>
                <p>Report friction or share an idea. Context and screenshots help us understand it quickly.</p>
              </div>
            </header>

            <form className="feedback-form" onSubmit={submit}>
              <fieldset className="feedback-type-picker">
                <legend>What kind of signal is this?</legend>
                <button type="button" className={type === 'ISSUE' ? 'is-selected' : ''} onClick={() => setType('ISSUE')} aria-pressed={type === 'ISSUE'}>
                  <AlertTriangle size={17} /><span><strong>Something’s broken</strong><small>Report an issue</small></span>
                </button>
                <button type="button" className={type === 'IMPROVEMENT' ? 'is-selected' : ''} onClick={() => setType('IMPROVEMENT')} aria-pressed={type === 'IMPROVEMENT'}>
                  <Lightbulb size={17} /><span><strong>I have an idea</strong><small>Suggest an improvement</small></span>
                </button>
              </fieldset>

              <label htmlFor="feedback-subject">Short summary</label>
              <input ref={subjectRef} id="feedback-subject" maxLength={120} value={subject} onChange={(event) => setSubject(event.target.value)} placeholder="What should we know?" required />

              <label htmlFor="feedback-description">Tell us what happened or what you’d improve</label>
              <textarea id="feedback-description" maxLength={4000} rows={5} value={description} onChange={(event) => setDescription(event.target.value)} placeholder="Include what you expected, what happened, and any steps that help us reproduce it…" required />
              <span className="feedback-counter">{description.length} / 4000</span>

              <fieldset className="severity-picker">
                <legend>How much does this affect you?</legend>
                <div>
                  {SEVERITIES.map((item) => (
                    <button key={item.value} type="button" className={severity === item.value ? `is-selected severity-${item.value.toLowerCase()}` : ''} onClick={() => setSeverity(item.value)} aria-pressed={severity === item.value}>
                      <strong>{item.label}</strong><small>{item.hint}</small>
                    </button>
                  ))}
                </div>
              </fieldset>

              <div className="feedback-upload">
                <input ref={fileRef} id="feedback-image" type="file" accept="image/jpeg,image/png,image/gif,image/webp,image/bmp,image/tiff,image/avif,image/heic,image/heif" onChange={chooseImage} />
                {image ? (
                  <div className="feedback-file">
                    <div className="feedback-preview">{previewUrl && <img src={previewUrl} alt="Selected attachment preview" />}</div>
                    <span><strong>{image.name}</strong><small>{(image.size / 1024 / 1024).toFixed(2)} MB · ready to attach</small></span>
                    <button type="button" onClick={removeImage} aria-label="Remove selected image"><Trash2 size={16} /></button>
                  </div>
                ) : (
                  <label htmlFor="feedback-image"><ImagePlus size={19} /><span><strong>Add a screenshot</strong><small>JPEG, PNG, GIF, WebP, BMP, TIFF, AVIF or HEIC · up to 8 MB</small></span></label>
                )}
              </div>

              {error && <div className="form-error" role="alert">{error}</div>}
              <div className="feedback-form-footer">
                <span><ShieldCheck size={14} /> Sent as {user.email}</span>
                <button className="button" type="submit" disabled={sending || !subject.trim() || !description.trim()}>
                  {sending ? 'Sending…' : 'Send feedback'}
                </button>
              </div>
            </form>
          </>
        )}
      </section>
    </div>
  )
}
