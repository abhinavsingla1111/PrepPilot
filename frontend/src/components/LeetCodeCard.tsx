import { useCallback, useEffect, useState } from 'react'
import { Check, Link2, Loader2, RefreshCw, Unlink } from 'lucide-react'
import { ApiError, api } from '../lib/api'
import type { CurrentUser, LeetCodeStatus } from '../types/api'

interface LeetCodeCardProps {
  user: CurrentUser | null
  onProgressChanged: () => void
}

export function LeetCodeCard({ user, onProgressChanged }: LeetCodeCardProps) {
  const [status, setStatus] = useState<LeetCodeStatus | null>(null)
  const [loaded, setLoaded] = useState(false)
  const [username, setUsername] = useState('')
  const [busy, setBusy] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [notice, setNotice] = useState<string | null>(null)

  const loadStatus = useCallback(() => {
    api.getLeetCodeStatus()
      .then(setStatus)
      .catch(() => setStatus(null))
      .finally(() => setLoaded(true))
  }, [])

  useEffect(() => {
    if (!user) {
      setStatus(null)
      setLoaded(false)
      setNotice(null)
      setError(null)
      return
    }
    setLoaded(false)
    loadStatus()
  }, [user, loadStatus])

  if (!user) return null

  const linked = Boolean(status?.username)

  const runAction = async (action: () => Promise<LeetCodeStatus>, successNotice?: (result: LeetCodeStatus) => string) => {
    setBusy(true)
    setError(null)
    setNotice(null)
    try {
      const result = await action()
      setStatus(result)
      if (successNotice) setNotice(successNotice(result))
      onProgressChanged()
    } catch (err) {
      setError(err instanceof ApiError ? err.message : 'Something went wrong. Please try again.')
    } finally {
      setBusy(false)
    }
  }

  const onLink = (event: React.FormEvent) => {
    event.preventDefault()
    const trimmed = username.trim()
    if (!trimmed) return
    void runAction(
      () => api.linkLeetCode(trimmed),
      (result) => `Linked. Marked ${result.syncedSolved} recent solve${result.syncedSolved === 1 ? '' : 's'} from LeetCode.`,
    )
  }

  const onSync = () =>
    void runAction(
      () => api.syncLeetCode(),
      (result) => `Synced. ${result.syncedSolved} new solve${result.syncedSolved === 1 ? '' : 's'} marked.`,
    )

  const onUnlink = () => {
    setUsername('')
    void runAction(() => api.unlinkLeetCode(), () => 'LeetCode account unlinked.')
  }

  return (
    <div className="lc-card">
      <div className="lc-card-head">
        <span className="lc-badge"><Link2 size={16} aria-hidden="true" /></span>
        <div>
          <strong>LeetCode account</strong>
          <small>{linked ? 'Sync your solved problems automatically.' : 'Link your username to pull in what you\u2019ve already solved.'}</small>
        </div>
      </div>

      {!loaded ? (
        <p className="lc-muted">Loading&hellip;</p>
      ) : linked && status ? (
        <>
          <div className="lc-linked-row">
            <span className="lc-username">@{status.username}</span>
            <div className="lc-actions">
              <button type="button" className="lc-btn" onClick={onSync} disabled={busy}>
                {busy ? <Loader2 size={14} className="lc-spin" /> : <RefreshCw size={14} />} Sync
              </button>
              <button type="button" className="lc-btn lc-btn-ghost" onClick={onUnlink} disabled={busy}>
                <Unlink size={14} /> Unlink
              </button>
            </div>
          </div>

          {status.stats ? (
            <div className="lc-stats">
              <div className="lc-stat lc-stat-total">
                <span className="lc-stat-value">{status.stats.total}</span>
                <span className="lc-stat-label">Solved</span>
              </div>
              <div className="lc-stat">
                <span className="lc-stat-value easy">{status.stats.easy}</span>
                <span className="lc-stat-label">Easy</span>
              </div>
              <div className="lc-stat">
                <span className="lc-stat-value medium">{status.stats.medium}</span>
                <span className="lc-stat-label">Medium</span>
              </div>
              <div className="lc-stat">
                <span className="lc-stat-value hard">{status.stats.hard}</span>
                <span className="lc-stat-label">Hard</span>
              </div>
            </div>
          ) : (
            <p className="lc-muted">Couldn&apos;t load live stats right now &mdash; try Sync.</p>
          )}
        </>
      ) : (
        <form className="lc-link-form" onSubmit={onLink}>
          <input
            type="text"
            value={username}
            onChange={(event) => setUsername(event.target.value)}
            placeholder="your-leetcode-username"
            aria-label="LeetCode username"
            autoComplete="off"
            spellCheck={false}
            disabled={busy}
          />
          <button type="submit" className="lc-btn lc-btn-primary" disabled={busy || !username.trim()}>
            {busy ? <Loader2 size={14} className="lc-spin" /> : <Link2 size={14} />} Link
          </button>
        </form>
      )}

      {notice && <p className="lc-notice"><Check size={13} aria-hidden="true" /> {notice}</p>}
      {error && <p className="lc-error">{error}</p>}
      {loaded && !linked && (
        <p className="lc-hint">Your LeetCode profile must be public. We only read public solve stats &mdash; no password needed.</p>
      )}
    </div>
  )
}
