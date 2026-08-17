import { cpp } from '@codemirror/lang-cpp'
import { java } from '@codemirror/lang-java'
import { python } from '@codemirror/lang-python'
import { indentWithTab } from '@codemirror/commands'
import { HighlightStyle, indentUnit, syntaxHighlighting } from '@codemirror/language'
import { EditorState } from '@codemirror/state'
import { EditorView, keymap } from '@codemirror/view'
import { tags } from '@lezer/highlight'
import CodeMirror from '@uiw/react-codemirror'
import { useEffect, useMemo, useState } from 'react'
import { isColorScheme, type ColorScheme } from '../lib/colorScheme'
import type { CodingLanguage } from '../types/api'

const MAX_SOURCE_LENGTH = 50_000
const EDITOR_INDENT = '  '

const editorThemeRules = {
  '&': {
    color: 'var(--code-text)',
    backgroundColor: 'var(--surface-code)',
    fontSize: '13px',
  },
  '&.cm-focused': { outline: 'none' },
  '.cm-scroller': {
    fontFamily: 'ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace',
    lineHeight: '1.65',
    overflow: 'auto',
  },
  '.cm-content': {
    minHeight: '410px',
    padding: '18px 0 28px',
    caretColor: 'var(--accent-blue)',
  },
  '.cm-line': { padding: '0 20px' },
  '.cm-gutters': {
    paddingTop: '18px',
    color: 'var(--text-faint)',
    backgroundColor: 'var(--surface-code)',
    border: 'none',
    borderRight: '1px solid var(--line)',
  },
  '.cm-activeLine': { backgroundColor: 'rgba(var(--accent-blue-rgb), 0.04)' },
  '.cm-activeLineGutter': {
    color: 'var(--accent-blue)',
    backgroundColor: 'rgba(var(--accent-blue-rgb), 0.09)',
  },
  '.cm-cursor, .cm-dropCursor': { borderLeftColor: 'var(--accent-blue)' },
  '&.cm-focused .cm-selectionBackground, .cm-selectionBackground, ::selection': {
    backgroundColor: 'rgba(var(--accent-blue-rgb), 0.24) !important',
  },
  '.cm-matchingBracket': {
    color: 'var(--syntax-bracket-0)',
    backgroundColor: 'rgba(var(--accent-blue-rgb), 0.12)',
    outline: '1px solid rgba(var(--accent-blue-rgb), 0.3)',
  },
  '.cm-tooltip': {
    color: 'var(--text-soft)',
    backgroundColor: 'var(--panel-raised)',
    border: '1px solid var(--line-strong)',
  },
} as const

const editorThemes: Record<ColorScheme, ReturnType<typeof EditorView.theme>> = {
  dark: EditorView.theme(editorThemeRules, { dark: true }),
  light: EditorView.theme(editorThemeRules, { dark: false }),
}

const editorHighlighting = HighlightStyle.define([
  { tag: [tags.comment, tags.lineComment, tags.blockComment], color: 'var(--syntax-comment)', fontStyle: 'italic' },
  { tag: [tags.keyword, tags.controlKeyword, tags.operatorKeyword, tags.modifier], color: 'var(--syntax-keyword)' },
  { tag: [tags.string, tags.special(tags.string), tags.regexp], color: 'var(--syntax-string)' },
  { tag: [tags.number, tags.bool, tags.null], color: 'var(--syntax-number)' },
  { tag: [tags.typeName, tags.className, tags.namespace], color: 'var(--syntax-type)' },
  { tag: [tags.function(tags.variableName), tags.definition(tags.function(tags.variableName))], color: 'var(--syntax-function)' },
  { tag: [tags.variableName, tags.propertyName, tags.labelName], color: 'var(--syntax-variable)' },
  { tag: [tags.meta, tags.annotation], color: 'var(--syntax-meta)' },
  { tag: [tags.bracket, tags.angleBracket, tags.squareBracket, tags.paren], color: 'var(--syntax-bracket-1)' },
  { tag: tags.invalid, color: 'var(--hard)', textDecoration: 'underline' },
])

function languageExtension(language: CodingLanguage) {
  switch (language) {
    case 'JAVA': return java()
    case 'CPP': return cpp()
    case 'PYTHON': return python()
  }
}

function readAppliedColorScheme(): ColorScheme {
  const scheme = document.documentElement.dataset.colorScheme ?? null
  return isColorScheme(scheme) ? scheme : 'dark'
}

function useAppliedColorScheme() {
  const [scheme, setScheme] = useState<ColorScheme>(readAppliedColorScheme)

  useEffect(() => {
    const root = document.documentElement
    const observer = new MutationObserver(() => setScheme(readAppliedColorScheme()))
    observer.observe(root, { attributes: true, attributeFilter: ['data-color-scheme'] })
    return () => observer.disconnect()
  }, [])

  return scheme
}

interface CodeEditorProps {
  language: CodingLanguage
  value: string
  onChange: (value: string) => void
}

export function CodeEditor({ language, value, onChange }: CodeEditorProps) {
  const colorScheme = useAppliedColorScheme()
  const extensions = useMemo(
    () => [
      languageExtension(language),
      EditorState.tabSize.of(EDITOR_INDENT.length),
      indentUnit.of(EDITOR_INDENT),
      keymap.of([indentWithTab]),
      editorThemes[colorScheme],
      syntaxHighlighting(editorHighlighting),
    ],
    [colorScheme, language],
  )

  return (
    <CodeMirror
      aria-label="Solution code"
      basicSetup={{
        autocompletion: false,
        bracketMatching: true,
        closeBrackets: true,
        foldGutter: true,
        highlightActiveLine: true,
        highlightActiveLineGutter: true,
        highlightSelectionMatches: true,
        lineNumbers: true,
      }}
      className="coding-editor"
      extensions={extensions}
      height="440px"
      onChange={(nextValue) => onChange(nextValue.slice(0, MAX_SOURCE_LENGTH))}
      theme="none"
      value={value}
    />
  )
}
