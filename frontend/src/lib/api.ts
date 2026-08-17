import type { AssessmentAnswerSaved, AssessmentAttempt, AssessmentAttemptSummary, AssessmentTopic, Cheatsheet, CodingExecution, CodingInterviewDraft, CodingInterviewSession, CodingInterviewSubmission, CodingInterviewSummary, CodingLanguage, CodingRunnerStatus, CurrentUser, DashboardSummary, FeedbackResponse, LeetCodeStatus, PageResponse, Problem, ProgressItem, RequestOtpResponse, ReviewItem, ReviewRating, ReviewSummary, UpdateProfileInput, UserProfile } from '../types/api'

const API_BASE = import.meta.env.VITE_API_URL?.replace(/\/$/, '') ?? ''

export class ApiError extends Error {
  constructor(
    message: string,
    public readonly status: number,
  ) {
    super(message)
  }
}

async function request<T>(path: string, init?: RequestInit): Promise<T> {
  const headers = new Headers(init?.headers)
  if (!(init?.body instanceof FormData) && !headers.has('Content-Type')) {
    headers.set('Content-Type', 'application/json')
  }
  const response = await fetch(`${API_BASE}${path}`, {
    credentials: 'include',
    ...init,
    headers,
  })

  if (!response.ok) {
    const body = await response.json().catch(() => null) as { message?: string } | null
    throw new ApiError(body?.message ?? 'Something went wrong. Please try again.', response.status)
  }

  if (response.status === 204) return undefined as T
  return response.json() as Promise<T>
}

export const api = {
  getMe: () => request<CurrentUser>('/api/v1/auth/me'),
  requestOtp: (email: string) => request<RequestOtpResponse>('/api/v1/auth/otp/request', {
    method: 'POST',
    body: JSON.stringify({ email }),
  }),
  verifyOtp: (email: string, code: string) => request<CurrentUser>('/api/v1/auth/otp/verify', {
    method: 'POST',
    body: JSON.stringify({ email, code }),
  }),
  logout: () => request<void>('/api/v1/auth/logout', { method: 'POST' }),
  getProblems: (params: URLSearchParams) => request<PageResponse<Problem>>(`/api/v1/problems?${params}`),
  getTopics: () => request<string[]>('/api/v1/problems/topics'),
  getProgress: () => request<ProgressItem[]>('/api/v1/progress'),
  getSummary: () => request<DashboardSummary>('/api/v1/progress/summary'),
  setProgress: (problemId: number, status: ProgressItem['status']) =>
    request<ProgressItem>(`/api/v1/progress/${problemId}`, {
      method: 'PUT',
      body: JSON.stringify({ status }),
    }),
  getCheatsheet: (language: string) => request<Cheatsheet>(`/api/v1/cheatsheets/${language}`),
  getOop: (language: string) => request<Cheatsheet>(`/api/v1/cheatsheets/oop/${language}`),
  getSystemDesign: (docId: string) => request<Cheatsheet>(`/api/v1/systemdesign/${docId}`),
  getFundamentals: (docId: string) => request<Cheatsheet>(`/api/v1/fundamentals/${docId}`),
  getLeetCodeStatus: () => request<LeetCodeStatus>('/api/v1/leetcode/link'),
  linkLeetCode: (username: string) =>
    request<LeetCodeStatus>('/api/v1/leetcode/link', {
      method: 'PUT',
      body: JSON.stringify({ username }),
    }),
  unlinkLeetCode: () => request<LeetCodeStatus>('/api/v1/leetcode/link', { method: 'DELETE' }),
  syncLeetCode: () => request<LeetCodeStatus>('/api/v1/leetcode/link/sync', { method: 'POST' }),
  getProfile: () => request<UserProfile>('/api/v1/profile'),
  updateProfile: (input: UpdateProfileInput) =>
    request<UserProfile>('/api/v1/profile', {
      method: 'PUT',
      body: JSON.stringify(input),
    }),
  getAssessmentTopics: () => request<AssessmentTopic[]>('/api/v1/assessments/topics'),
  startAssessment: (topic: string) =>
    request<AssessmentAttempt>(`/api/v1/assessments/topics/${encodeURIComponent(topic)}/attempts`, {
      method: 'POST',
    }),
  getAssessmentAttempt: (attemptId: string) =>
    request<AssessmentAttempt>(`/api/v1/assessments/attempts/${encodeURIComponent(attemptId)}`),
  saveAssessmentAnswer: (attemptId: string, questionId: string, selectedOption: number) =>
    request<AssessmentAnswerSaved>(
      `/api/v1/assessments/attempts/${encodeURIComponent(attemptId)}/questions/${encodeURIComponent(questionId)}`,
      {
        method: 'PUT',
        body: JSON.stringify({ selectedOption }),
      },
    ),
  submitAssessment: (attemptId: string) =>
    request<AssessmentAttempt>(`/api/v1/assessments/attempts/${encodeURIComponent(attemptId)}/submit`, {
      method: 'POST',
    }),
  getAssessmentHistory: (topic: string) =>
    request<AssessmentAttemptSummary[]>(
      `/api/v1/assessments/topics/${encodeURIComponent(topic)}/history`,
    ),
  getReviewSummary: () => request<ReviewSummary>('/api/v1/reviews/summary'),
  getReviews: (dueOnly = true) => request<ReviewItem[]>(`/api/v1/reviews?dueOnly=${dueOnly}`),
  rateReview: (reviewId: string, rating: ReviewRating) =>
    request<ReviewItem>(`/api/v1/reviews/${encodeURIComponent(reviewId)}/rating`, {
      method: 'POST',
      body: JSON.stringify({ rating }),
    }),
  startCodingInterview: (language: CodingLanguage) =>
    request<CodingInterviewSession>('/api/v1/interviews/coding/sessions', {
      method: 'POST',
      body: JSON.stringify({ language }),
    }),
  getCodingInterviewHistory: () =>
    request<CodingInterviewSummary[]>('/api/v1/interviews/coding/sessions'),
  getCodingInterview: (sessionId: string) =>
    request<CodingInterviewSession>(`/api/v1/interviews/coding/sessions/${encodeURIComponent(sessionId)}`),
  saveCodingInterviewDraft: (sessionId: string, draft: CodingInterviewDraft) =>
    request<CodingInterviewSession>(`/api/v1/interviews/coding/sessions/${encodeURIComponent(sessionId)}/draft`, {
      method: 'PUT',
      body: JSON.stringify(draft),
    }),
  submitCodingInterview: (sessionId: string, submission: CodingInterviewSubmission) =>
    request<CodingInterviewSession>(`/api/v1/interviews/coding/sessions/${encodeURIComponent(sessionId)}/submit`, {
      method: 'POST',
      body: JSON.stringify(submission),
    }),
  getCodingRunnerStatus: () => request<CodingRunnerStatus>('/api/v1/interviews/coding/runner'),
  runCodingInterview: (sessionId: string, language: CodingLanguage, sourceCode: string) =>
    request<CodingExecution>(`/api/v1/interviews/coding/sessions/${encodeURIComponent(sessionId)}/runs`, {
      method: 'POST',
      body: JSON.stringify({ language, sourceCode }),
    }),
  getCodingRuns: (sessionId: string) =>
    request<CodingExecution[]>(`/api/v1/interviews/coding/sessions/${encodeURIComponent(sessionId)}/runs`),
  getCodingRun: (runId: string) =>
    request<CodingExecution>(`/api/v1/interviews/coding/runs/${encodeURIComponent(runId)}`),
  submitFeedback: (form: FormData) => request<FeedbackResponse>('/api/v1/feedback', {
    method: 'POST',
    body: form,
  }),
}
