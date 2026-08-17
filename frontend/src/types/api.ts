export type Difficulty = 'EASY' | 'MEDIUM' | 'HARD'

export interface Problem {
  id: number
  leetcodeId: number
  slug: string
  title: string
  url: string
  difficulty: Difficulty
  topic: string
}

export interface PageResponse<T> {
  content: T[]
  number: number
  size: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
}

export interface CurrentUser {
  id: string
  email: string
}

export interface RequestOtpResponse {
  message: string
  expiresInSeconds: number
  devOtp?: string
}

export interface ProgressItem {
  problemId: number
  status: 'NOT_STARTED' | 'IN_PROGRESS' | 'SOLVED'
  solvedAt?: string
}

export interface Cheatsheet {
  title: string
  content: string
}

export interface LeetCodeStats {
  total: number
  easy: number
  medium: number
  hard: number
}

export interface LeetCodeStatus {
  username: string | null
  stats: LeetCodeStats | null
  syncedSolved: number
}

export interface RecommendedProblem {
  id: number
  leetcodeId: number
  title: string
  url: string
  difficulty: Difficulty
  topic: string
}

export interface DashboardSummary {
  solved: number
  totalProblems: number
  solvedThisWeek: number
  recommended: RecommendedProblem[]
}

export interface UserProfile {
  email: string
  leetcodeUsername: string | null
  fullName: string | null
  age: number | null
  mobile: string | null
  avatarUrl: string | null
}

export interface UpdateProfileInput {
  fullName?: string | null
  age?: number | null
  mobile?: string | null
  avatarUrl?: string | null
}

export type AssessmentStatus = 'IN_PROGRESS' | 'COMPLETED' | 'TIMED_OUT'

export interface AssessmentTopic {
  slug: string
  name: string
  totalQuestions: number
  attemptsUsed: number
  maxAttempts: number
  durationMinutes: number
  bestScore: number | null
  activeAttemptId: string | null
}

export interface AssessmentQuestion {
  id: string
  position: number
  difficulty: Difficulty
  prompt: string
  options: string[]
  selectedOption: number | null
  correctOption: number | null
  correct: boolean | null
  justification: string | null
}

export interface AssessmentAttempt {
  id: string
  topic: string
  topicName: string
  attemptNumber: number
  status: AssessmentStatus
  startedAt: string
  expiresAt: string
  submittedAt: string | null
  score: number | null
  totalQuestions: number
  answeredQuestions: number
  questions: AssessmentQuestion[]
}

export interface AssessmentAttemptSummary {
  id: string
  attemptNumber: number
  status: AssessmentStatus
  score: number | null
  startedAt: string
  submittedAt: string | null
}

export interface AssessmentAnswerSaved {
  questionId: string
  selectedOption: number
  answeredQuestions: number
}

export type FeedbackType = 'ISSUE' | 'IMPROVEMENT'
export type FeedbackSeverity = 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL'

export interface FeedbackResponse {
  id: string
  status: 'NEW' | 'REVIEWING' | 'RESOLVED'
  emailStatus: 'PENDING' | 'SENT' | 'FAILED' | 'SKIPPED'
  createdAt: string
}

export type ReviewRating = 'FORGOT' | 'DIFFICULT' | 'REMEMBERED' | 'EASY'

export interface ReviewSummary {
  due: number
  total: number
  mastered: number
}

export interface ReviewItem {
  id: string
  topic: string
  topicName: string
  prompt: string
  options: string[]
  originalAnswer: number | null
  correctOption: number
  justification: string
  dueAt: string
  intervalDays: number
  repetitionCount: number
  lastRating: ReviewRating | null
  lastReviewedAt: string | null
  due: boolean
  mastered: boolean
}

export type CodingLanguage = 'JAVA' | 'PYTHON' | 'CPP'
export type CodingInterviewStatus = 'IN_PROGRESS' | 'COMPLETED' | 'TIMED_OUT'

export interface CodingInterviewPrompt {
  slug: string
  title: string
  difficulty: Difficulty
  topic: string
  companyTags: string
  prompt: string
  constraints: string
  examples: string
  inputFormat: string
  outputFormat: string
  starterCode: Record<CodingLanguage, string>
  sampleTests: Array<{ input: string; expectedOutput: string; explanation: string }>
  totalTests: number
  expectedApproach: string | null
  expectedTimeComplexity: string | null
  expectedSpaceComplexity: string | null
}

export interface CodingInterviewRubric {
  clarification: number | null
  approach: number | null
  correctness: number | null
  total: number | null
}

export interface CodingInterviewDraft {
  language: CodingLanguage
  solutionCode: string
  approachNotes: string
  complexityAnalysis: string
}

export interface CodingInterviewSubmission extends CodingInterviewDraft {
  clarification: number
  approach: number
  correctness: number
  reflection: string
}

export interface CodingInterviewSession extends CodingInterviewDraft {
  id: string
  status: CodingInterviewStatus
  startedAt: string
  expiresAt: string
  submittedAt: string | null
  prompt: CodingInterviewPrompt
  rubric: CodingInterviewRubric | null
  reflection: string
}

export interface CodingInterviewSummary {
  id: string
  title: string
  difficulty: Difficulty
  topic: string
  status: CodingInterviewStatus
  language: CodingLanguage
  startedAt: string
  submittedAt: string | null
  bestPassedTests: number | null
  totalTests: number | null
}

export type CodingExecutionStatus = 'QUEUED' | 'RUNNING' | 'PASSED' | 'FAILED' | 'ERROR'
export type CodingExecutionCaseStatus = 'PASSED' | 'WRONG_ANSWER' | 'TIME_LIMIT' | 'COMPILE_ERROR' | 'RUNTIME_ERROR' | 'INTERNAL_ERROR'

export interface CodingExecutionCaseResult {
  position: number
  visible: boolean
  status: CodingExecutionCaseStatus
  input: string | null
  expectedOutput: string | null
  actualOutput: string | null
  diagnostic: string
  timeSeconds: string | null
  memoryKilobytes: number | null
}

export interface CodingExecution {
  id: string
  sessionId: string
  status: CodingExecutionStatus
  language: CodingLanguage
  totalTests: number
  passedTests: number
  results: CodingExecutionCaseResult[]
  failureMessage: string | null
  createdAt: string
  startedAt: string | null
  completedAt: string | null
}

export interface CodingRunnerStatus {
  enabled: boolean
  emailResults: boolean
  dailyLimit: number
  remainingToday: number
}
