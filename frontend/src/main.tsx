import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App'
import { initializeColorScheme } from './lib/colorScheme'
import './theme.css'
import './styles.css'

const requestedTheme = import.meta.env.VITE_UI_THEME
document.documentElement.dataset.theme = requestedTheme === 'lime' ? 'lime' : 'ocean'
initializeColorScheme()

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
