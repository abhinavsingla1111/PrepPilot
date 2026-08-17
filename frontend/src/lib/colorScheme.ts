export const COLOR_SCHEMES = ['light', 'dark'] as const

export type ColorScheme = (typeof COLOR_SCHEMES)[number]

const STORAGE_KEY = 'preppilot-color-scheme'
const DEFAULT_TRANSITION_DURATION_MS = 700
const MAX_TRANSITION_DURATION_MS = 5_000
const THEME_COLORS: Record<ColorScheme, string> = {
  light: '#f5f5f7',
  dark: '#000000',
}

type ThemeViewTransition = {
  finished: Promise<void>
}

type ViewTransitionDocument = Document & {
  startViewTransition?: (update: () => void) => ThemeViewTransition
}

type ThemeTransitionDirection = 'left-to-right' | 'right-to-left'

function getTransitionDurationMs(): number {
  const configuredValue = import.meta.env.VITE_THEME_TRANSITION_MS?.trim()
  const configuredDuration = configuredValue ? Number(configuredValue) : Number.NaN

  if (
    !Number.isFinite(configuredDuration)
    || !Number.isInteger(configuredDuration)
    || configuredDuration < 0
    || configuredDuration > MAX_TRANSITION_DURATION_MS
  ) {
    return DEFAULT_TRANSITION_DURATION_MS
  }

  return configuredDuration
}

export const THEME_TRANSITION_DURATION_MS = getTransitionDurationMs()

export function isColorScheme(value: string | null): value is ColorScheme {
  return value === 'light' || value === 'dark'
}

export function getInitialColorScheme(): ColorScheme {
  try {
    const stored = window.localStorage.getItem(STORAGE_KEY)
    if (isColorScheme(stored)) return stored
  } catch {
    // Storage can be unavailable in privacy-restricted browser contexts.
  }

  return window.matchMedia?.('(prefers-color-scheme: light)').matches ? 'light' : 'dark'
}

export function applyColorScheme(scheme: ColorScheme) {
  document.documentElement.dataset.colorScheme = scheme
  document.documentElement.style.colorScheme = scheme
  document.querySelector<HTMLMetaElement>('meta[name="theme-color"]')?.setAttribute('content', THEME_COLORS[scheme])
}

export function transitionColorScheme(scheme: ColorScheme, onApplied: () => void) {
  const applyScheme = () => {
    applyColorScheme(scheme)
    onApplied()
  }
  const prefersReducedMotion = window.matchMedia?.('(prefers-reduced-motion: reduce)').matches ?? false
  const startViewTransition = (document as ViewTransitionDocument).startViewTransition?.bind(document)

  if (!startViewTransition || prefersReducedMotion || THEME_TRANSITION_DURATION_MS === 0) {
    applyScheme()
    return
  }

  const root = document.documentElement
  const direction: ThemeTransitionDirection = scheme === 'dark' ? 'left-to-right' : 'right-to-left'
  root.dataset.themeTransitionDirection = direction

  const clearDirection = () => {
    if (root.dataset.themeTransitionDirection === direction) {
      delete root.dataset.themeTransitionDirection
    }
  }

  try {
    const transition = startViewTransition(applyScheme)
    // Clean up transient state whether the animation finishes or is interrupted.
    void transition.finished.then(clearDirection, clearDirection)
  } catch {
    clearDirection()
    applyScheme()
  }
}

export function saveColorScheme(scheme: ColorScheme) {
  try {
    window.localStorage.setItem(STORAGE_KEY, scheme)
  } catch {
    // The active page still keeps the selected scheme when persistence is blocked.
  }
}

export function initializeColorScheme(): ColorScheme {
  const scheme = getInitialColorScheme()
  document.documentElement.style.setProperty(
    '--theme-transition-duration',
    `${THEME_TRANSITION_DURATION_MS}ms`,
  )
  applyColorScheme(scheme)
  return scheme
}
