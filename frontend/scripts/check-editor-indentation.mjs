import { indentWithTab, insertNewlineAndIndent } from '@codemirror/commands'
import { cpp } from '@codemirror/lang-cpp'
import { java } from '@codemirror/lang-java'
import { python } from '@codemirror/lang-python'
import { indentUnit } from '@codemirror/language'
import { EditorSelection, EditorState } from '@codemirror/state'

const INDENT = '  '

function applyCommand(state, command) {
  let nextState
  const handled = command({
    state,
    dispatch(transaction) {
      nextState = transaction.state
    },
  })
  if (!handled || !nextState) throw new Error('Editor command was not handled')
  return nextState
}

function stateAtEnd(doc, language) {
  return EditorState.create({
    doc,
    selection: EditorSelection.cursor(doc.length),
    extensions: [
      language,
      EditorState.tabSize.of(INDENT.length),
      indentUnit.of(INDENT),
    ],
  })
}

const enterCases = [
  ['Java', 'class Demo {\n  void solve() {', java(), 4],
  ['C++', 'class Demo {\npublic:\n  void solve() {', cpp(), 4],
  ['Python', 'class Demo:\n  def solve(self):\n    for value in []:', python(), 6],
]

for (const [name, doc, language, expectedSpaces] of enterCases) {
  const nextState = applyCommand(
    stateAtEnd(doc, language),
    insertNewlineAndIndent,
  )
  const finalLine = nextState.doc.line(nextState.doc.lines).text
  if (finalLine !== ' '.repeat(expectedSpaces)) {
    throw new Error(`${name} Enter indentation was ${finalLine.length}, expected ${expectedSpaces}`)
  }
}

for (const [name, , language] of enterCases) {
  const nextState = applyCommand(stateAtEnd('value', language), indentWithTab.run)
  if (nextState.doc.toString() !== `${INDENT}value`) {
    throw new Error(`${name} Tab did not insert one two-space indentation unit`)
  }
}

console.log('Editor indentation checks passed for Java, C++, and Python.')
