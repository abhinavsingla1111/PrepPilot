/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_API_URL?: string
  readonly VITE_UI_THEME?: 'ocean' | 'lime'
  readonly VITE_THEME_TRANSITION_MS?: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}
