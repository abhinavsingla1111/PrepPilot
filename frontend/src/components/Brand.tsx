import { Braces } from 'lucide-react'

export function Brand() {
  return (
    <a className="brand" href="#top" aria-label="PrepPilot home">
      <span className="brand-mark" aria-hidden="true"><Braces size={19} strokeWidth={2.3} /></span>
      <span className="brand-name"><span>preppilot</span></span>
    </a>
  )
}
