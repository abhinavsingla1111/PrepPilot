import { useCallback, useEffect, useState } from 'react'
import { flushSync } from 'react-dom'
import {
  applyColorScheme,
  getInitialColorScheme,
  isColorScheme,
  saveColorScheme,
  transitionColorScheme,
  type ColorScheme,
} from '../lib/colorScheme'

function readAppliedScheme(): ColorScheme {
  const applied = document.documentElement.dataset.colorScheme ?? null
  return isColorScheme(applied) ? applied : getInitialColorScheme()
}

export function useColorScheme() {
  const [scheme, setScheme] = useState<ColorScheme>(readAppliedScheme)

  useEffect(() => {
    applyColorScheme(scheme)
    saveColorScheme(scheme)
  }, [scheme])

  const toggleScheme = useCallback(() => {
    const currentScheme = readAppliedScheme()
    const nextScheme = currentScheme === 'dark' ? 'light' : 'dark'

    transitionColorScheme(nextScheme, () => {
      flushSync(() => setScheme(nextScheme))
      saveColorScheme(nextScheme)
    })
  }, [])

  return { scheme, toggleScheme }
}
