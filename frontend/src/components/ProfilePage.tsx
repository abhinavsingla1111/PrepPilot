import { useEffect, useRef, useState } from 'react'
import { Camera, Check, Link2, Loader2, LogOut, Pencil, Trash2 } from 'lucide-react'
import { ApiError, api } from '../lib/api'
import type { CurrentUser, UserProfile } from '../types/api'

interface ProfilePageProps {
  user: CurrentUser | null
  onLogin: () => void
  onLogout: () => void
  onProfileChange: (profile: UserProfile) => void
}

const MOBILE_PATTERN = /^[1-9]\d{9}$/
const LEETCODE_PATTERN = /^[A-Za-z0-9_-]{1,30}$/

async function fileToAvatarDataUrl(file: File): Promise<string> {
  const rawDataUrl = await new Promise<string>((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => resolve(reader.result as string)
    reader.onerror = () => reject(new Error('read-failed'))
    reader.readAsDataURL(file)
  })

  const image = await new Promise<HTMLImageElement>((resolve, reject) => {
    const element = new Image()
    element.onload = () => resolve(element)
    element.onerror = () => reject(new Error('image-failed'))
    element.src = rawDataUrl
  })

  const size = 240
  const canvas = document.createElement('canvas')
  canvas.width = size
  canvas.height = size
  const context = canvas.getContext('2d')
  if (!context) return rawDataUrl

  const ratio = Math.max(size / image.width, size / image.height)
  const drawWidth = image.width * ratio
  const drawHeight = image.height * ratio
  context.drawImage(image, (size - drawWidth) / 2, (size - drawHeight) / 2, drawWidth, drawHeight)
  return canvas.toDataURL('image/jpeg', 0.85)
}

function ProfileRow({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <div className="profile-row">
      <span className="profile-row-label">{label}</span>
      <div className="profile-row-value">{children}</div>
    </div>
  )
}

export function ProfilePage({ user, onLogin, onLogout, onProfileChange }: ProfilePageProps) {
  const [profile, setProfile] = useState<UserProfile | null>(null)
  const [loaded, setLoaded] = useState(false)
  const [editing, setEditing] = useState(false)
  const [fullName, setFullName] = useState('')
  const [age, setAge] = useState('')
  const [mobile, setMobile] = useState('')
  const [leetcode, setLeetcode] = useState('')
  const [avatarUrl, setAvatarUrl] = useState<string | null>(null)
  const [saving, setSaving] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [notice, setNotice] = useState<string | null>(null)
  const fileInputRef = useRef<HTMLInputElement>(null)

  const hydrate = (data: UserProfile) => {
    setFullName(data.fullName ?? '')
    setAge(data.age != null ? String(data.age) : '')
    setMobile(data.mobile ?? '')
    setLeetcode(data.leetcodeUsername ?? '')
    setAvatarUrl(data.avatarUrl)
  }

  useEffect(() => {
    window.scrollTo({ top: 0 })
    if (!user) {
      setLoaded(true)
      return
    }
    api.getProfile()
      .then((data) => {
        setProfile(data)
        hydrate(data)
      })
      .catch(() => setError('Could not load your profile.'))
      .finally(() => setLoaded(true))
  }, [user])

  if (!user) {
    return (
      <main className="profile-page">
        <div className="shell profile-shell">
          <div className="profile-signed-out">
            <h1>Your profile</h1>
            <p>Sign in to view and edit your profile.</p>
            <button type="button" className="button" onClick={onLogin}>Sign in</button>
          </div>
        </div>
      </main>
    )
  }

  const startEditing = () => {
    if (profile) hydrate(profile)
    setError(null)
    setNotice(null)
    setEditing(true)
  }

  const cancelEditing = () => {
    if (profile) hydrate(profile)
    setError(null)
    setEditing(false)
  }

  const onPickFile = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0]
    event.target.value = ''
    if (!file) return
    if (!file.type.startsWith('image/')) {
      setError('Please choose an image file.')
      return
    }
    try {
      setAvatarUrl(await fileToAvatarDataUrl(file))
      setError(null)
    } catch {
      setError('Could not process that image.')
    }
  }

  const onMobileChange = (value: string) => {
    setMobile(value.replace(/\D/g, '').slice(0, 10))
  }

  const onSubmit = async (event: React.FormEvent) => {
    event.preventDefault()
    setError(null)
    setNotice(null)

    if (mobile && !MOBILE_PATTERN.test(mobile)) {
      setError('Enter a valid 10-digit mobile number that does not start with 0.')
      return
    }
    const ageValue = age.trim() ? Number(age) : null
    if (ageValue !== null && (!Number.isInteger(ageValue) || ageValue < 1 || ageValue > 120)) {
      setError('Enter a valid age between 1 and 120.')
      return
    }
    const newLeet = leetcode.trim()
    if (newLeet && !LEETCODE_PATTERN.test(newLeet)) {
      setError('Enter a valid LeetCode username.')
      return
    }

    setSaving(true)
    try {
      const updated = await api.updateProfile({
        fullName: fullName.trim() || null,
        age: ageValue,
        mobile: mobile || null,
        avatarUrl: avatarUrl ?? null,
      })

      let leetUsername = updated.leetcodeUsername
      const currentLeet = profile?.leetcodeUsername ?? ''
      if (newLeet !== currentLeet) {
        if (newLeet) {
          const status = await api.linkLeetCode(newLeet)
          leetUsername = status.username
        } else {
          await api.unlinkLeetCode()
          leetUsername = null
        }
      }

      const merged: UserProfile = { ...updated, leetcodeUsername: leetUsername }
      setProfile(merged)
      hydrate(merged)
      onProfileChange(merged)
      setNotice('Profile updated.')
      setEditing(false)
    } catch (caught) {
      setError(caught instanceof ApiError ? caught.message : 'Could not update your profile.')
    } finally {
      setSaving(false)
    }
  }

  const displayName = profile?.fullName?.trim() || null
  const initial = (displayName?.charAt(0) ?? user.email.charAt(0)).toUpperCase()
  const shownAvatar = editing ? avatarUrl : profile?.avatarUrl ?? null

  return (
    <main className="profile-page">
      <div className="shell profile-shell">
        <header className="profile-header">
          <h1>Your profile</h1>
          <p>Your account details. Name, age, and mobile number are optional.</p>
        </header>

        {!loaded ? (
          <p className="profile-status">Loading…</p>
        ) : (
          <form className="profile-card" onSubmit={onSubmit}>
            <div className="profile-avatar-row">
              <div className="profile-avatar">
                {shownAvatar ? (
                  <img src={shownAvatar} alt="Profile" />
                ) : (
                  <span className="profile-avatar-fallback">{initial}</span>
                )}
                {editing && (
                  <button
                    type="button"
                    className="profile-avatar-edit"
                    onClick={() => fileInputRef.current?.click()}
                    aria-label="Change profile picture"
                  >
                    <Camera size={15} />
                  </button>
                )}
              </div>
              <div className="profile-avatar-meta">
                <strong>{displayName ?? 'Add your name'}</strong>
                <span>{user.email}</span>
                {editing && avatarUrl && (
                  <button
                    type="button"
                    className="profile-remove-avatar"
                    onClick={() => setAvatarUrl(null)}
                    aria-label="Remove profile picture"
                    title="Remove photo"
                  >
                    <Trash2 size={15} />
                  </button>
                )}
              </div>
              {!editing && (
                <button type="button" className="button button-small profile-edit-btn" onClick={startEditing}>
                  <Pencil size={14} /> Edit
                </button>
              )}
              <input ref={fileInputRef} type="file" accept="image/*" className="sr-only" onChange={onPickFile} />
            </div>

            <div className="profile-fields">
              <ProfileRow label="Full name">
                {editing ? (
                  <input
                    type="text"
                    value={fullName}
                    onChange={(event) => setFullName(event.target.value)}
                    placeholder="Your name"
                    maxLength={80}
                    autoComplete="name"
                  />
                ) : (
                  <span className="profile-value">{profile?.fullName || '—'}</span>
                )}
              </ProfileRow>

              <ProfileRow label="Age">
                {editing ? (
                  <input
                    type="number"
                    value={age}
                    onChange={(event) => setAge(event.target.value)}
                    placeholder="Optional"
                    min={1}
                    max={120}
                  />
                ) : (
                  <span className="profile-value">{profile?.age ?? '—'}</span>
                )}
              </ProfileRow>

              <ProfileRow label="Mobile number">
                {editing ? (
                  <div className="profile-mobile">
                    <span className="profile-mobile-code">+91</span>
                    <input
                      type="tel"
                      inputMode="numeric"
                      value={mobile}
                      onChange={(event) => onMobileChange(event.target.value)}
                      placeholder="10-digit number"
                      autoComplete="tel-national"
                    />
                  </div>
                ) : (
                  <span className="profile-value">{profile?.mobile ? `+91 ${profile.mobile}` : '—'}</span>
                )}
              </ProfileRow>

              <ProfileRow label="Email">
                <span className="profile-value">{user.email}</span>
              </ProfileRow>

              <ProfileRow label="LeetCode username">
                {editing ? (
                  <input
                    type="text"
                    value={leetcode}
                    onChange={(event) => setLeetcode(event.target.value)}
                    placeholder="your-leetcode-username"
                    autoComplete="off"
                    spellCheck={false}
                  />
                ) : profile?.leetcodeUsername ? (
                  <a
                    className="profile-leetcode"
                    href={`https://leetcode.com/u/${profile.leetcodeUsername}/`}
                    target="_blank"
                    rel="noreferrer"
                  >
                    <Link2 size={14} /> @{profile.leetcodeUsername}
                  </a>
                ) : (
                  <span className="profile-value">Not linked</span>
                )}
              </ProfileRow>
            </div>

            {notice && <p className="profile-notice"><Check size={14} /> {notice}</p>}
            {error && <p className="profile-error">{error}</p>}

            {editing && (
              <div className="profile-actions">
                <button type="button" className="text-button" onClick={cancelEditing} disabled={saving}>
                  Cancel
                </button>
                <button type="submit" className="button" disabled={saving}>
                  {saving ? <Loader2 size={15} className="lc-spin" /> : null} Update profile
                </button>
              </div>
            )}
          </form>
        )}

        {loaded && (
          <div className="profile-signout">
            <button type="button" className="profile-signout-btn" onClick={onLogout}>
              <LogOut size={15} /> Log out
            </button>
          </div>
        )}
      </div>
    </main>
  )
}
